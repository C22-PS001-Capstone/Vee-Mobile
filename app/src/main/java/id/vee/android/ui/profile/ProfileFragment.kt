package id.vee.android.ui.profile

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import id.vee.android.R
import id.vee.android.databinding.FragmentProfileBinding
import id.vee.android.ui.home.HomeViewModel
import id.vee.android.ui.welcome.WelcomeActivity

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).supportActionBar?.title = "Profile"

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnProfile.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_navigation_profile_to_profileDetailFragment))
        binding.btnLanguage.setOnClickListener { startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS)) }
        binding.btnTheme.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_navigation_profile_to_themeFragment))
        binding.btnLogout.setOnClickListener {
            val intent = Intent(activity, WelcomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}