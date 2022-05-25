package id.vee.android.ui.home

import android.os.Bundle
import android.util.Log
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
import id.vee.android.databinding.FragmentHomeBinding
import id.vee.android.domain.model.Token
import id.vee.android.utils.checkTokenAvailability
import org.koin.androidx.viewmodel.ext.android.viewModel


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
                rvStories.apply {
                    layoutManager = LinearLayoutManager(context)
                    setHasFixedSize(true)
                    adapter = storyAdapter
                }
                viewModel.activityResponse.observe(viewLifecycleOwner) { responses ->
                    if (responses != null) {
                        Log.d("ListActivity", "viewModelListener: ${responses.data}")
                        when (responses) {
                            is Resource.Loading -> {
                                rvStories.visibility = View.GONE
                                progressBar.visibility = View.VISIBLE
                            }
                            is Resource.Success -> {
                                rvStories.visibility = View.VISIBLE
                                progressBar.visibility = View.GONE
                                storyAdapter.submitList(responses.data)
                            }
                            is Resource.Error -> {
                                rvStories.visibility = View.GONE
                                Log.d("ERROR", "viewModelListener: ${responses.message}")
                            }
                        }
                    }
                }
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