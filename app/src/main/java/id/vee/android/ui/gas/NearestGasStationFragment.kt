package id.vee.android.ui.gas

import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
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

    private val activityContext by lazy {
        (activity as AppCompatActivity)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNearestGasStationBinding.inflate(inflater, container, false)
        setupBackButton()
        return binding?.root
    }

    private fun setupBackButton() {
        activityContext.supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = getString(R.string.nearest_gas_station)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val gasStationAdapter = GasStationListAdapter()
        viewModelListener()
        context?.let { ctx ->
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(ctx)
            binding?.apply {
                rvGasStations.apply {
                    layoutManager = LinearLayoutManager(ctx)
                    setHasFixedSize(true)
                    adapter = gasStationAdapter
                }
            }
        }
        viewModel.getLiveLocation()
        viewModel.getToken()
        binding?.apply {
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
                            showGasStationsNotAvailable()
                        }
                    }
                }
            }

        }
    }

    private fun viewModelListener() {
        viewModel.locationResponse.observe(viewLifecycleOwner) { location ->
            currentLocation = location
        }
        viewModel.tokenResponse.observe(viewLifecycleOwner) { tokenData ->
            userToken = tokenData
            getGasStations()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getLiveLocation()
        getGasStations()
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