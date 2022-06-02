package id.vee.android.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import id.vee.android.R
import id.vee.android.databinding.ActivityMainBinding
import id.vee.android.service.NearestGasStationReceiver
import id.vee.android.utils.toMD5
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.util.concurrent.TimeUnit


@SuppressLint("UnspecifiedImmutableFlag")
class MainActivity : AppCompatActivity() {
    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val geofencingClient: GeofencingClient by lazy {
        LocationServices.getGeofencingClient(this)
    }
    private val locationRequest: LocationRequest by lazy {
        // Update interval while web server has cache
        LocationRequest.create().apply {
            interval = TimeUnit.SECONDS.toMillis(1)
            maxWaitTime = TimeUnit.SECONDS.toMillis(1)
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }
    private lateinit var locationCallback: LocationCallback

    private var lastLocation: Location? = null
    private var lastGeofenceLocation: Location? = null

    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }
    private val viewModel: MainViewModel by viewModel()

    private val mGeofenceList: MutableList<Geofence> = mutableListOf()

    private val radiusMeter: Float = 150f

    private var hashedData = ""

    private var isBatterySaver = false

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.action_bar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            R.id.menu_notification -> {
                val navController = findNavController(R.id.nav_host_fragment_activity_main)
                navController.navigateUp()
                navController.navigate(R.id.navigation_notification)
            }
        }
        return super.onOptionsItemSelected(item)
    }


    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.apply {
            elevation = 0f
        }

        binding.apply {
            setContentView(root)

            val navView: BottomNavigationView = bottomNavigationView
            val navController = findNavController(R.id.nav_host_fragment_activity_main)

            val appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.navigation_home,
                    R.id.navigation_gas_station,
                    R.id.navigation_activity,
                    R.id.navigation_profile
                )
            )
            fabAddActivity.setOnClickListener {
                navController.navigateUp()
                navController.navigate(R.id.navigation_add_activity)
            }
            setupActionBarWithNavController(navController, appBarConfiguration)
            navView.setupWithNavController(navController)

            navController.addOnDestinationChangedListener { _, destination, _ ->
                if (destination.id == R.id.navigation_add_activity) {
                    bottomAppBar.visibility = View.GONE
                } else {
                    bottomAppBar.visibility = View.VISIBLE
                }
            }
        }
        getMyLocation()
        createLocationRequest()
        createLocationCallback()
        startLocationUpdates()
        viewModel.getLiveLocation()
        viewModel.getLocalStations()
        viewModel.getBatterySaverSettings()
        viewModel.locationResponse.observe(this) {
            Timber.d("locationResponse: $it")
        }
        viewModel.stationsResponse.observe(this) { gasStations ->
            if (gasStations.isNotEmpty() && hashedData != gasStations.toString()
                    .toMD5()
            ) {
                val lastGasStationLocation = Location("LastLocation").apply {
                    latitude = gasStations.last().lat
                    longitude = gasStations.last().lon
                }
                hashedData = gasStations.toString().toMD5()
                mGeofenceList.clear()
                gasStations.forEach {
                    val id = "${it.name}"
                    mGeofenceList.add(
                        Geofence.Builder()
                            .setRequestId(id)
                            .setCircularRegion(
                                it.lat,
                                it.lon,
                                radiusMeter
                            )
                            .setExpirationDuration(TimeUnit.HOURS.toMillis(1))
//                            .setLoiteringDelay(5 * 60 * 1000) //5 minutes for staying in geofence
                            .setLoiteringDelay(5 * 1000) // 5 seconds for staying in geofence
                            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_DWELL)
                            .build()
                    )
                }
                if (lastGeofenceLocation == null) {
                    addGeofence()
                } else {
                    lastGeofenceLocation?.let {
                        if (it.distanceTo(lastGasStationLocation) > 1000) {
                            addGeofence()
                        }
                    }
                }
                lastGeofenceLocation = Location("Geofence").apply {
                    latitude = gasStations[gasStations.size - 1].lat
                    longitude = gasStations[gasStations.size - 1].lon
                }
            }
        }
        viewModel.batterySaverResponse.observe(this) {
            isBatterySaver = it
            if (!it) {
                startLocationUpdates()
            } else {
                stopLocationUpdates()
            }
            getMyLocation()
        }
    }

    private val resolutionLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult()
        ) { result ->
            when (result.resultCode) {
                RESULT_OK ->
                    Timber.i("onActivityResult: All location settings are satisfied.")
                RESULT_CANCELED ->
                    Toast.makeText(
                        this@MainActivity,
                        getString(R.string.gps_warning),
                        Toast.LENGTH_SHORT
                    ).show()
            }
        }

    private fun createLocationRequest() {
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        val client = LocationServices.getSettingsClient(this)
        client.checkLocationSettings(builder.build())
            .addOnSuccessListener {
                getMyLocation()
            }
            .addOnFailureListener { exception ->
                if (exception is ResolvableApiException) {
                    try {
                        resolutionLauncher.launch(
                            IntentSenderRequest.Builder(exception.resolution).build()
                        )
                    } catch (sendEx: IntentSender.SendIntentException) {
                        Toast.makeText(this, sendEx.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }


    // Geofencing
    private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(this, NearestGasStationReceiver::class.java)
        intent.action = NearestGasStationReceiver.ACTION_GEOFENCE_EVENT
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_MUTABLE)
        } else {
            PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }
    }

    @SuppressLint("MissingPermission")
    private fun addGeofence() {
        val geofencingRequest = GeofencingRequest.Builder()
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .addGeofences(mGeofenceList)
            .build()
        geofencingClient.removeGeofences(geofencePendingIntent).run {
            addOnCompleteListener {
                geofencingClient.addGeofences(geofencingRequest, geofencePendingIntent).run {
                    addOnSuccessListener {
                        Timber.d("Geofence added")
                    }
                    addOnFailureListener {
                        Timber.d("Geofence failed : ${it.message}")
                    }
                }
            }
        }
    }

    private val requestBackgroundLocationPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }

    private val runningQOrLater = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

    @TargetApi(Build.VERSION_CODES.Q)
    private val requestLocationPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                if (runningQOrLater) {
                    requestBackgroundLocationPermissionLauncher.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                } else {
                    getMyLocation()
                }
            }
        }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    override fun onResume() {
        super.onResume()
        startLocationUpdates()
    }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    @TargetApi(Build.VERSION_CODES.Q)
    private fun checkForegroundAndBackgroundLocationPermission(): Boolean {
        val foregroundLocationApproved = checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        val backgroundPermissionApproved =
            if (runningQOrLater) {
                checkPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
            } else {
                true
            }
        return foregroundLocationApproved && backgroundPermissionApproved
    }

    @SuppressLint("MissingPermission")
    private fun getMyLocation() {
        if (!checkForegroundAndBackgroundLocationPermission()) {
            requestLocationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    if (location.latitude != 0.0 && location.longitude != 0.0) {
                        viewModel.updateLocation(location.latitude, location.longitude)
                    }
                    Timber.d("Get last location success $location")
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        getString(R.string.error_location),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun createLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation
                for (location in locationResult.locations) {
                    if (lastLocation == null) {
                        lastLocation = location
                    }
                    val distance = location.distanceTo(lastLocation)
                    Timber.d("Distance : $distance")
                    if (distance > 100 || location.latitude == 0.0) {
                        Timber.d("location: ${location.latitude}, ${location.longitude}")
                        lastLocation = location
                        viewModel.updateLocation(location.latitude, location.longitude)
                    }
                }
            }

            override fun onLocationAvailability(availability: LocationAvailability) {
                Timber.d("onLocationAvailability: ${availability.isLocationAvailable}")
            }
        }
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun startLocationUpdates() {
        try {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
            Timber.d("Location update started")
        } catch (exception: SecurityException) {
            Timber.e("Error : %s", exception.message)
        }
    }

}