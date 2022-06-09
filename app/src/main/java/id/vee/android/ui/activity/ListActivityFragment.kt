package id.vee.android.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import id.vee.android.R
import id.vee.android.adapter.ActivityPagedAdapter
import id.vee.android.adapter.LoadingStateAdapter
import id.vee.android.databinding.FragmentListActivityBinding
import id.vee.android.domain.model.Token
import id.vee.android.utils.checkTokenAvailability
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
        val storyPagedAdapter = ActivityPagedAdapter {
            val direction =
                ListActivityFragmentDirections.actionNavigationActivityToDetailActivityFragment(it)
            findNavController().navigate(direction)
        }
        viewModelListener()
        binding?.apply {
            rvStories.apply {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = storyPagedAdapter.withLoadStateFooter(
                    footer = LoadingStateAdapter {
                        storyPagedAdapter.retry()
                    }
                )
            }
            storyPagedAdapter.apply {
                addLoadStateListener { loadState ->
                    if (loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached && itemCount < 1) {
                        showStoryNotAvailable()
                        progressBar.visibility = View.GONE
                        rvStories.visibility = View.VISIBLE
                    } else if (loadState.source.refresh is LoadState.Loading) {
                        progressBar.visibility = View.VISIBLE
                        rvStories.visibility = View.GONE
                    } else {
                        showStoryNotAvailable(false)
                        progressBar.visibility = View.GONE
                        rvStories.visibility = View.VISIBLE
                    }
                }
            }
            viewModel.pagedActivityResponse.observe(viewLifecycleOwner) { responses ->
                storyPagedAdapter.submitData(lifecycle, responses)
            }
        }
    }

    private fun showStoryNotAvailable(state: Boolean = true) {
        binding?.apply {
            storiesNotAvailable.visibility = if (state) View.VISIBLE else View.GONE
            rvStories.visibility = if (state) View.GONE else View.VISIBLE
        }
    }

    private fun viewModelListener() {
        viewModel.tokenResponse.observe(viewLifecycleOwner) { tokenData ->
            userToken = tokenData
            userToken?.let { dataToken ->
                checkTokenAvailability(viewModel, dataToken, viewLifecycleOwner) { newToken ->
                    viewModel.getPagedActivity(newToken.accessToken)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}