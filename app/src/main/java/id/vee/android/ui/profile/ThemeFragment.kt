package id.vee.android.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import id.vee.android.R
import id.vee.android.data.local.ThemePreferences
import id.vee.android.databinding.FragmentThemeBinding
import id.vee.android.vm.ViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class ThemeFragment : Fragment() {
    private var _binding: FragmentThemeBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentThemeBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.title_theme)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.apply {
            val pref = ThemePreferences.getInstance(this.dataStore)
            val factory: ViewModelFactory = ViewModelFactory.getInstance(this, pref)
            val viewModel: ThemeViewModel by viewModels {
                factory
            }
            viewModel.getThemeSettings()
            viewModelListener(viewModel, this)
            binding?.apply {
                switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
                    viewModel.saveThemeSetting(isChecked)
                }
            }
        }
    }

    private fun viewModelListener(viewModel: ThemeViewModel, context: Context) {
        binding?.apply{
            viewModel.themeResponse.observe(viewLifecycleOwner) { isDarkModeActive: Boolean ->
                if (isDarkModeActive) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    switchTheme.isChecked = true
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    switchTheme.isChecked = false
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}