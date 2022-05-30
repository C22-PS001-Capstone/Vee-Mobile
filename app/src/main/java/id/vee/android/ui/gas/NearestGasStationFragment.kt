package id.vee.android.ui.gas

import android.content.Intent
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import id.vee.android.R
import id.vee.android.adapter.GasStationListAdapter
import id.vee.android.data.Resource
import id.vee.android.databinding.FragmentNearestGasStationBinding
import id.vee.android.domain.model.Token
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class NearestGasStationFragment : Fragment() {
    private var _binding: FragmentNearestGasStationBinding? = null
    private val binding get() = _binding

    private var userToken: Token? = null
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
        val gasAdapter = GasStationListAdapter{ gasStation ->
            // TODO: Navigate to detail
        }
        setupRecyclerView(gasAdapter)
        viewModel.getToken()
        viewModel.getLiveLocation()
        viewModelListener()
        binding?.apply {
            viewModel.gasStationsResponse.observe(viewLifecycleOwner) { responses ->
                if (responses != null) {
                    when (responses) {
                        is Resource.Loading -> {
                            progressBar.visibility = View.VISIBLE
                        }
                        is Resource.Success -> {
                            progressBar.visibility = View.GONE
                            val stations = responses.data
                            if (stations?.isNotEmpty() == true) {
                                stations.sortedBy { it.distance }
                                gasAdapter.submitList(null)
                                gasAdapter.submitList(stations)
                            } else {
                                gasAdapter.submitList(null)
                            }
                        }
                        is Resource.Error -> {

                        }
                    }
                }
            }
        }
    }

    private fun setupRecyclerView(gasAdapter: GasStationListAdapter) {
        binding?.apply {
            rvGasStations.apply {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = gasAdapter
            }
        }
    }

    private fun viewModelListener() {
        viewModel.tokenResponse.observe(viewLifecycleOwner) {
            userToken = it
            userToken?.let { token ->
                currentLocation?.latitude?.let { lat ->
                    currentLocation?.longitude?.let { lng ->
                        viewModel.getGasStations(
                            token.accessToken,
                            lat,
                            lng
                        )
                    }
                }
            }
        }
        viewModel.locationResponse.observe(viewLifecycleOwner) {
            currentLocation = it
            currentLocation?.let { location ->
                userToken?.let { token ->
                    viewModel.getGasStations(
                        token.accessToken,
                        location.latitude,
                        location.longitude
                    )
                }
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        _binding = null
    }
}