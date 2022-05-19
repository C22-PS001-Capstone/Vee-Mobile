package id.vee.android.ui.profile

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import id.vee.android.R
import id.vee.android.data.local.ThemePreferences
import id.vee.android.databinding.FragmentProfileDetailBinding
import id.vee.android.databinding.FragmentThemeBinding
import id.vee.android.ui.home.HomeViewModel

class ThemeFragment : Fragment() {
    private var _binding: FragmentThemeBinding? = null
    private val binding get() = _binding!!
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentThemeBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).supportActionBar?.title = "Theme"

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*val pref = ThemePreferences.getInstance(dataStore)
        val settingsViewModel =
            ViewModelProvider(this, ViewModelFactory(pref))[ThemeViewModel::class.java]
        settingsViewModel.getThemeSettings().observe(
            this
        ) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding?.switchTheme?.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding?.switchTheme?.isChecked = false
            }
        }

        binding?.switchTheme?.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            settingsViewModel.saveThemeSetting(isChecked)
        }*/
//    }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}