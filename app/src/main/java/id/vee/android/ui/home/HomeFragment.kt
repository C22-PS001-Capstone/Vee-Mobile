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
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*


@Suppress("UNCHECKED_CAST")
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
        val dateFormat = SimpleDateFormat(
            getString(R.string.month_format),
            Locale.getDefault()
        )
        val monthFormat = SimpleDateFormat(
            "MMMM",
            Locale.getDefault()
        )
        val date = Date()
        val initMonth = dateFormat.format(date)
        val monthString = monthFormat.format(date)
        context?.apply {
            viewModel.getUserData()
            viewModel.getToken()
            viewModel.getLiveLocation()
            viewModel.getRobo(initMonth.toString())
            viewModelListener()
            binding?.apply {
                viewModel.gasStationsResponse.observe(viewLifecycleOwner) { responses ->
                    if (responses != null) {
                        when (responses) {
                            is Resource.Loading -> {
                                progressBarNearest.visibility = View.VISIBLE
                                tvNoDataGasStations.visibility = View.GONE
                                dataGasStation.visibility = View.GONE
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
                    setHasFixedSize(false)
                    adapter = storyAdapter
                }
                viewModel.activityResponse.observe(viewLifecycleOwner) { responses ->
                    if (responses != null) {
                        Timber.d("viewModelListener: ${responses.data}")
                        when (responses) {
                            is Resource.Loading -> {
                                showRobo(false)
                                rvStories.visibility = View.GONE
                                progressBar.visibility = View.VISIBLE
                                noActivityImage.visibility = View.GONE
                            }
                            is Resource.Success -> {
                                rvStories.visibility = View.VISIBLE
                                progressBar.visibility = View.GONE
                                if (responses.data?.isNotEmpty() == true) {
                                    val activities = responses.data
                                    activities.mapIndexed { index, activity ->
                                        activity.isMonthShow =
                                            (index > 0 && responses.data[index].date.formatDate("MMM") != responses.data[index - 1].date.formatDate(
                                                "MMM"
                                            )) || index == 0
                                    }
                                    storyAdapter.submitList(activities)
                                    noActivityImage.visibility = View.GONE
                                } else {
                                    noActivityImage.visibility = View.VISIBLE
                                    storyAdapter.submitList(null)
                                }
                            }
                            is Resource.Error -> {
                                rvStories.visibility = View.GONE
                                progressBar.visibility = View.GONE
                                noActivityImage.visibility = View.GONE
                            }
                        }
                    }
                }
                viewModel.roboResponse.observe(viewLifecycleOwner) { robo ->
                    if (robo != null) {
                        showRobo(true)
                        val newNumber = NumberFormat.getInstance(Locale.GERMANY)
                        val liter = newNumber.format(robo.liter)
                        val price = newNumber.format(robo.price)
                        dashboardMonth.text =
                            resources.getString(R.string.robo_this_month_label, monthString)
                        dashboardFillUps.text =
                            resources.getString(R.string.robo_fillups_value, liter.toString())
                        dashboardExpenses.text =
                            resources.getString(R.string.robo_expenses_value, price.toString())
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
                    // Navigation to google map and take to direction
                    var lat = gasStations.lat
                    var lng = gasStations.lon
                    currentLocation?.let {
                        lat = it.latitude
                        lng = it.longitude
                    }
                    val gmmIntentUri =
                        Uri.parse("http://maps.google.com/maps?saddr=${lat},${lng}&daddr=${gasStations.lat},${gasStations.lon}")
                    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                    mapIntent.setPackage("com.google.android.apps.maps")
                    context?.apply {
                        mapIntent.resolveActivity(packageManager)?.let {
                            startActivity(mapIntent)
                        }
                    }
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
                checkTokenAvailability(viewModel, tokenData, viewLifecycleOwner) {
                    currentLocation?.latitude?.let { lat ->
                        currentLocation?.longitude?.let { lng ->
                            viewModel.getGasStations(
                                it.accessToken,
                                lat,
                                lng
                            )
                        }
                    }
                    viewModel.getForecast(it.accessToken)
                }
            }
        }
        viewModel.forecastResponse.observe(viewLifecycleOwner) { forecastData ->
            binding?.apply {
                if (forecastData.status == "success") {
                    forecastData.data?.let { forecast ->
                        val nextForecast = forecast.forecast.first()
                        val averageForecast = forecast.forecast.average()
                        val newNumber = NumberFormat.getInstance(Locale.GERMANY)
                        forecastFillUp.text =
                            resources.getString(
                                R.string.robo_expenses_value,
                                newNumber.format(nextForecast.toInt()).toString()
                            )
                        forecastAverageFillUp.text =
                            resources.getString(
                                R.string.robo_expenses_value,
                                newNumber.format(averageForecast.toInt()).toString()
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

        val dateFormat = SimpleDateFormat(
            getString(R.string.month_format),
            Locale.getDefault()
        )
        val date = Date()
        val initMonth = dateFormat.format(date)
        viewModel.getUserData()
        viewModel.getToken()
        viewModel.getLiveLocation()
        viewModel.getRobo(initMonth.toString())
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