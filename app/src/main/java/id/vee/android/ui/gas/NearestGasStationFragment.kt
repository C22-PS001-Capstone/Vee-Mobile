package id.vee.android.ui.gas

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import id.vee.android.R
import id.vee.android.adapter.GasStationListAdapter
import id.vee.android.data.Resource
import id.vee.android.databinding.FragmentNearestGasStationBinding
import id.vee.android.domain.model.Token
import id.vee.android.utils.checkTokenAvailability
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class NearestGasStationFragment : Fragment() {
    private var _binding: FragmentNearestGasStationBinding? = null
    private val binding get() = _binding

    private var userToken: Token? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var currentLocation: Location? = null

    private val viewModel: GasStationsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNearestGasStationBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).supportActionBar?.title =
            getString(R.string.nearest_gas_station)
        setupBackButton()
        return binding?.root
    }

    private fun setupBackButton() {
        if (activity is AppCompatActivity) {
            (activity as AppCompatActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getToken()
        val gasStationAdapter = GasStationListAdapter()
        context?.apply {
            viewModelListener()
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            getMyLastLocation(this)
            binding?.apply {
                rvGasStations.apply {
                    layoutManager = LinearLayoutManager(context)
                    setHasFixedSize(true)
                    adapter = gasStationAdapter
                }
                viewModel.gasStationsResponse.observe(viewLifecycleOwner) { responses ->
                    gasStationAdapter.submitList(null)
                    if (responses != null) {
                        when (responses) {
                            is Resource.Loading -> {
                                progressBar.visibility = View.VISIBLE
                            }
                            is Resource.Success -> {
                                progressBar.visibility = View.GONE
                                rvGasStations.visibility = View.VISIBLE
                                if (responses.data?.isNotEmpty() == true) {
                                    gasStationAdapter.submitList(responses.data.sortedBy { it.distance })
                                } else {
                                    showGasStationsNotAvailable()
                                }
                                Timber.d(gasStationAdapter.itemCount.toString())
                            }
                            is Resource.Error -> {
                                Timber.e(responses.message)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun viewModelListener() {
        viewModel.tokenResponse.observe(viewLifecycleOwner) { tokenData ->
            userToken = tokenData
            getGasStations()
        }
    }

    private fun getGasStations() {
        var lat = 0.0
        var lon = 0.0
        currentLocation?.apply {
            lat = latitude
            lon = longitude
        }
        binding.apply {
            userToken?.let { it ->
                checkTokenAvailability(viewModel, it, viewLifecycleOwner) {
                    viewModel.getGasStations(
                        it.accessToken,
                        lat,
                        lon
                    )
                }
            }
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
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    currentLocation = location
                } else {
                    Toast.makeText(
                        context,
                        getString(R.string.location_not_found),
                        Toast.LENGTH_SHORT
                    ).show()
                    showLocationNotAvailable()
                }
            }.addOnFailureListener {
                Timber.e(it)
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

    private fun showLocationNotAvailable() {
        binding?.apply {
            locationNotAvailable.visibility = View.VISIBLE
            rvGasStations.visibility = View.GONE
        }
    }

    private fun showGasStationsNotAvailable() {
        binding?.apply {
            gasStationsNotAvailable.visibility = View.VISIBLE
            rvGasStations.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}