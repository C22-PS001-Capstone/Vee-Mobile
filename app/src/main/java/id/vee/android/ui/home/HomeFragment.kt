package id.vee.android.ui.home

import android.content.Intent
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import id.vee.android.R
import id.vee.android.adapter.ActivityListAdapter
import id.vee.android.data.Resource
import id.vee.android.databinding.FragmentHomeBinding
import id.vee.android.domain.model.GasStations
import id.vee.android.domain.model.Token
import id.vee.android.utils.CustomLinearLayoutManager
import id.vee.android.utils.checkTokenAvailability
import id.vee.android.utils.formatDate
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding

    private var userToken: Token? = null

    private val viewModel: HomeViewModel by viewModel()

    private var currentLocation: Location? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        changeTitle(getString(R.string.dummy))
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showRobo(false)
        val storyAdapter = ActivityListAdapter {
            val direction = HomeFragmentDirections.actionNavigationHomeToDetailActivityFragment(it)
            findNavController().navigate(direction)
        }
        context?.apply {
            viewModel.getUserData()
            viewModel.getToken()
            viewModel.getLiveLocation()
            viewModelListener()
            binding?.apply {
                viewModel.gasStationsResponse.observe(viewLifecycleOwner) { responses ->
                    if (responses != null) {
                        when (responses) {
                            is Resource.Loading -> {
                                progressBarNearest.visibility = View.VISIBLE
                            }
                            is Resource.Success -> {
                                progressBarNearest.visibility = View.GONE
                                if (responses.data?.isNotEmpty() == true) {
                                    val data = responses.data
                                    dataGasStation.visibility = View.VISIBLE
                                    tvNoDataGasStations.visibility = View.GONE
                                    setGasStationHomeData(data.sortedBy { it.distance }.take(3))
                                } else {
                                    dataGasStation.visibility = View.GONE
                                    tvNoDataGasStations.visibility = View.VISIBLE
                                }
                            }
                            is Resource.Error -> {
                                Timber.e(responses.message)
                                dataGasStation.visibility = View.GONE
                                tvNoDataGasStations.visibility = View.VISIBLE
                            }
                        }
                    }
                }
                rvStories.apply {
                    layoutManager = CustomLinearLayoutManager(context)
                    setHasFixedSize(true)
                    adapter = storyAdapter
                }
                viewModel.activityResponse.observe(viewLifecycleOwner) { responses ->
                    if (responses != null) {
                        Timber.d("viewModelListener: ${responses.data}")
                        when (responses) {
                            is Resource.Loading -> {
                                rvStories.visibility = View.GONE
                                progressBar.visibility = View.VISIBLE
                            }
                            is Resource.Success -> {
                                val initMonth = SimpleDateFormat(
                                    getString(R.string.month_format),
                                    Locale.getDefault())
                                viewModel.getRobo(initMonth.toString())
                                rvStories.visibility = View.VISIBLE
                                progressBar.visibility = View.GONE
                                if (responses.data?.isNotEmpty() == true) {
                                    val activities = responses.data
                                    activities.mapIndexed { index, activity ->
                                        activity.isMonthShow =
                                            (index > 0 && responses.data[index].date?.formatDate("MMM") != responses.data[index - 1].date?.formatDate(
                                                "MMM"
                                            )) || index == 0
                                    }
                                    storyAdapter.submitList(activities)
                                } else {
                                    storyAdapter.submitList(null)
                                }
                            }
                            is Resource.Error -> {
                                rvStories.visibility = View.GONE
                                progressBar.visibility = View.GONE
                                // Show error
                            }
                        }
                    }
                }
                viewModel.roboResponse.observe(viewLifecycleOwner){ robo ->
                    if (robo != null) {
                        showRobo(true)
                        robo.forEach {
                            dashboardMonth.text =  resources.getString(R.string.robo_this_month_label, it.date?.formatDate("MMM"))
                            dashboardFillUps.text = resources.getString(R.string.robo_fillups_label, it.price)
                            dashboardExpenses.text = resources.getString(R.string.robo_expenses_label, it.price)
                        }
                    }
                }
            }
        }
    }

    private fun getVendorImage(vendor: String?, view: ImageView) {
        return when (vendor) {
            "Pertamina" -> view.setImageResource(R.drawable.pertamina)
            "Shell" -> view.setImageResource(R.drawable.shell)
            else -> view.setImageResource(R.drawable.other_vendor_gasstaions)
        }
    }

    private fun setGasStationHomeData(data: List<GasStations>) {
        binding?.apply {
            val vendorImage = listOf(ivVendor1, ivVendor2, ivVendor3)
            val vendorText = listOf(tvVendor1, tvVendor2, tvVendor3)
            val vendorDistance = listOf(tvDistance1, tvDistance2, tvDistance3)
            val listGasStation = listOf(gasStation1, gasStation2, gasStation3)
            data.forEachIndexed { index, gasStations ->
                getVendorImage(gasStations.vendor, vendorImage[index])
                vendorText[index].text = gasStations.vendor
                vendorDistance[index].text = gasStations.distance.toString()
                vendorDistance[index].apply {
                    gasStations.distance?.let { distance ->
                        val distanceText = StringBuilder()
                        if (distance.toDouble() < 0.3) {
                            setTextColor(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.limegreen
                                )
                            )
                            distanceText.append("${distance.toDouble() * 1000} m")
                        } else if (distance.toDouble() < 0.5) {
                            setTextColor(ContextCompat.getColor(requireContext(), R.color.primary))
                            distanceText.append("${distance.toDouble() * 1000} m")
                        } else {
                            setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                            distanceText.append("${distance.toDouble()} km")
                        }
                        text = distanceText
                    }
                }
                listGasStation[index].setOnClickListener {
                    // TODO: Navigate to detail
                }
            }
        }
    }

    private fun viewModelListener() {
        viewModel.userResponse.observe(viewLifecycleOwner) { userData ->
            if (userData != null) {
                changeTitle(userData.firstName)
            }
        }
        viewModel.tokenResponse.observe(viewLifecycleOwner) { tokenData ->
            userToken = tokenData
            getLatestData()
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

    private fun getLatestData() {
        userToken?.let {
            checkTokenAvailability(viewModel, it, viewLifecycleOwner) { tokenData ->
                viewModel.getActivity(tokenData.accessToken)
                viewModel.getGasStations(
                    tokenData.accessToken,
                    0.0,
                    0.0
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        getLatestData()
    }

    private fun showRobo(state: Boolean) {
        binding?.apply {
            roboTitle.visibility = if (state) View.VISIBLE else View.GONE
            roboLayout.visibility = if (state) View.VISIBLE else View.GONE
        }
    }

    private fun changeTitle(title: String) {
        (activity as AppCompatActivity).supportActionBar?.title =
            resources.getString(R.string.welcome, title)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}