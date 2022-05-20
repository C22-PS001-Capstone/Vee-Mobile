package id.vee.android.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import id.vee.android.R
import id.vee.android.data.local.ThemePreferences
import id.vee.android.databinding.FragmentHomeBinding
import id.vee.android.vm.ViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding

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
        context?.apply {
            val pref = ThemePreferences.getInstance(this.dataStore)
            val factory: ViewModelFactory = ViewModelFactory.getInstance(this, pref)
            val viewModel: HomeViewModel by viewModels {
                factory
            }
            viewModel.getUserData()
            viewModelListener(viewModel, this)
        }
    }

    private fun viewModelListener(viewModel: HomeViewModel, context: Context) {
        viewModel.userResponse.observe(viewLifecycleOwner) { userData ->
            if (userData != null) {
                changeTitle(userData.firstName)
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