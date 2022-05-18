package id.vee.android.ui.activity

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import id.vee.android.R
import id.vee.android.data.local.entity.TokenEntity
import id.vee.android.databinding.FragmentAddActivityBinding
import id.vee.android.utils.MyDatePickerDialog
import id.vee.android.utils.checkEmptyEditText
import id.vee.android.utils.padStart
import id.vee.android.vm.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

class AddActivityFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentAddActivityBinding? = null
    private val binding get() = _binding

    private var userToken: TokenEntity? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddActivityBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).supportActionBar?.title = "Add Activity"
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val currentTime: Date = Calendar.getInstance().time
        val formattedDate = SimpleDateFormat("dd-mm-yyyy", Locale.getDefault()).format(currentTime)

        context?.apply {
            val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
            val viewModel: ActivityViewModel by viewModels {
                factory
            }
            viewModel.getToken()
            viewModel.tokenResponse.observe(viewLifecycleOwner) { tokenData ->
                userToken = tokenData
            }
            binding?.apply {
                btnDpd.setOnClickListener(this@AddActivityFragment)
                edtDate.setText(formattedDate)
                btnAddActivity.setOnClickListener {
                    if (!checkEmptyEditText(edtDate) && !checkEmptyEditText(edtDistance) && !checkEmptyEditText(
                            edtLitre
                        ) && !checkEmptyEditText(edtExpense)
                    ) {
                        return@setOnClickListener
                    }
                    viewModel.insertActivity(
                        userToken?.accessToken ?: "",
                        edtDate.text.toString(),
                        edtDistance.text.toString().toInt(),
                        edtLitre.text.toString().toInt(),
                        edtExpense.text.toString().toInt()
                    )
                }
                viewModel.actionResponse.observe(viewLifecycleOwner) { response ->
                    Log.d(TAG, "onViewCreated: $response")
                }
            }
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            getMyLastLocation(this)
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    // Precise location access granted.
                    context?.let { getMyLastLocation(it) }
                }
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    // Only approximate location access granted.
                    context?.let { getMyLastLocation(it) }
                }
                else -> {
                    // No location access granted.
                }
            }
        }

    private fun checkPermission(permission: String, context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getMyLastLocation(context: Context) {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, context) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION, context)
        ) {
            fusedLocationClient.locationAvailability.addOnSuccessListener { locationAvailability ->
                if(locationAvailability.isLocationAvailable){
                    val locationTask: Task<Location> = fusedLocationClient.lastLocation
                    Log.d(TAG, "getMyLastLocation: ${locationTask.exception}")
                    Log.d(TAG, "getMyLastLocation: ${context}")
                    locationTask.addOnSuccessListener { location ->
                        if (location != null) {
                            val lat = location.latitude
                            val lng = location.longitude
                            Log.d(TAG, "getMyLastLocation: $lat, $lng")
                        }
                    }
                    locationTask.addOnFailureListener { exception ->
                        Log.d(TAG, "getMyLastLocation exception: ${exception.message}")
                    }
                    Log.d(TAG, "getMyLastLocation: location available")
                }
            }
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    Log.d(TAG, "getMyLastLocation: $location")
                } else {
                    Toast.makeText(
                        context,
                        "Location is not found. Try Again",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_dpd -> {
                activity?.takeIf { !it.isFinishing && !it.isDestroyed }?.let { activity ->
                    MyDatePickerDialog(activity, ::showDate).show()
                }
            }
        }
    }

    private fun showDate(year: Int, month: Int, day: Int) {
        Log.d("Picked Date", "$year-$month-$day")
        val formatedDate = "${day.padStart(2)}-${month.padStart(2)}-$year"
        // Implement to text view
        binding?.apply {
            edtDate.setText(formatedDate)
        }
    }

    companion object {
        private const val TAG = "AddActivityFragment"
    }
}