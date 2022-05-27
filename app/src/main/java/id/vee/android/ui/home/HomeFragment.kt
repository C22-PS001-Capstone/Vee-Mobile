package id.vee.android.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
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


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding

    private var userToken: Token? = null

    private val viewModel: HomeViewModel by viewModel()


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
            viewModelListener(viewModel)
            binding?.apply {
                viewModel.gasStationsResponse.observe(viewLifecycleOwner) { responses ->
                    if (responses != null) {
                        when (responses) {
                            is Resource.Loading -> {
                                progressBar.visibility = View.VISIBLE
                            }
                            is Resource.Success -> {
                                progressBar.visibility = View.GONE
                                if (responses.data?.isNotEmpty() == true) {
                                    val data = responses.data
                                    setGasStationHomeData(data)
                                } else
                                    tvNoDataGasStations.isVisible = true
                                    dataGasStation.isVisible = false
                            }
                            is Resource.Error -> {
                                Timber.e(responses.message)
                                // Show error
                            }
                        }
                    }
                }
                rvStories.apply {
                    layoutManager = CustomLinearLayoutManager(context).setScrollEnabled(false)
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
                                    storyAdapter.submitList(responses.data)
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
            }
        }
    }

    private fun setGasStationHomeData(data: List<GasStations>) {
        binding?.apply {
            //data 1
            when (data[0].vendor) {
                "Pertamina" -> ivVendor1.setImageResource(R.drawable.pertamina)
                "Shell" -> ivVendor1.setImageResource(R.drawable.shell)
                else -> ivVendor1.setImageResource(R.drawable.other_vendor_gasstaions)
            }
            tvVendor1.text = data[0].vendor
            val distance1 = data[0].distance
            tvDistance1.text = distance1
            tvDistance1.apply {
                if (distance1?.toDouble()!! < 0.3) {
                    setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                } else if (distance1.toDouble() < 0.5) {
                    setTextColor(ContextCompat.getColor(requireContext(), R.color.primary))
                } else setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
            }

            //data 2
            when (data[1].vendor) {
                "Pertamina" -> ivVendor2.setImageResource(R.drawable.pertamina)
                "Shell" -> ivVendor2.setImageResource(R.drawable.shell)
                else -> ivVendor2.setImageResource(R.drawable.other_vendor_gasstaions)
            }
            tvVendor2.text = data[1].vendor
            val distance2 = data[1].distance
            tvDistance2.text = distance2
            tvDistance2.apply {
                if (distance2?.toDouble()!! < 0.3) {
                    setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                } else if (distance2.toDouble() < 0.5) {
                    setTextColor(ContextCompat.getColor(requireContext(), R.color.primary))
                } else setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
            }

            //data 3
            when (data[2].vendor) {
                "Pertamina" -> ivVendor3.setImageResource(R.drawable.pertamina)
                "Shell" -> ivVendor3.setImageResource(R.drawable.shell)
                else -> ivVendor3.setImageResource(R.drawable.other_vendor_gasstaions)
            }
            tvVendor3.text = data[2].vendor
            val distance3 = data[2].distance
            tvDistance3.text = distance3
            tvDistance3.apply {
                if (distance3?.toDouble()!! < 0.3) {
                    setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                } else if (distance3.toDouble() < 0.5) {
                    setTextColor(ContextCompat.getColor(requireContext(), R.color.primary))
                } else setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
            }
        }
    }

    private fun viewModelListener(viewModel: HomeViewModel) {
        viewModel.userResponse.observe(viewLifecycleOwner) { userData ->
            if (userData != null) {
                changeTitle(userData.firstName)
            }
        }
        viewModel.tokenResponse.observe(viewLifecycleOwner) { tokenData ->
            userToken = tokenData
            if (tokenData != null) {
                checkTokenAvailability(viewModel, tokenData, viewLifecycleOwner) {
                    viewModel.getActivity(tokenData.accessToken)
                }
            }
        }
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