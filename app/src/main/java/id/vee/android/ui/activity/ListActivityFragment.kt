package id.vee.android.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import id.vee.android.R
import id.vee.android.adapter.ActivityListAdapter
import id.vee.android.data.Resource
import id.vee.android.databinding.FragmentListActivityBinding
import id.vee.android.domain.model.Token
import id.vee.android.utils.checkTokenAvailability
import id.vee.android.utils.formatDate
import org.koin.androidx.viewmodel.ext.android.viewModel

class ListActivityFragment : Fragment() {
    private var _binding: FragmentListActivityBinding? = null
    private val binding get() = _binding

    private var userToken: Token? = null

    private val viewModel: ActivityViewModel by viewModel()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListActivityBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.title_activity)
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
        val storyAdapter = ActivityListAdapter {
            val direction =
                ListActivityFragmentDirections.actionNavigationActivityToDetailActivityFragment(it)
            findNavController().navigate(direction)
            // Implement next time
        }
        viewModelListener()
        binding?.apply {
            rvStories.apply {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = storyAdapter
            }
            viewModel.activityResponse.observe(viewLifecycleOwner) { responses ->
                if (responses != null) {
                    storyAdapter.submitList(null)
                    when (responses) {
                        is Resource.Loading -> {
                            progressBar.visibility = View.VISIBLE
                        }
                        is Resource.Success -> {
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
                            } else {
                                storyAdapter.submitList(null)
                                showStoryNotAvailable()
                            }
                        }
                        is Resource.Error -> {
                            showStoryNotAvailable()
                        }
                    }
                }
            }
        }

    }

    private fun showStoryNotAvailable() {
        binding?.apply {
            storiesNotAvailable.visibility = View.VISIBLE
            rvStories.visibility = View.GONE
        }
    }

    private fun viewModelListener() {
        viewModel.tokenResponse.observe(viewLifecycleOwner) { tokenData ->
            userToken = tokenData
            if(tokenData != null){
                checkTokenAvailability(viewModel, tokenData, viewLifecycleOwner) {
                    viewModel.getActivity(tokenData.accessToken)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}