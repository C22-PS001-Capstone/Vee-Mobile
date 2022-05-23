package id.vee.android.ui.profile

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import id.vee.android.R
import id.vee.android.databinding.FragmentProfileBinding
import id.vee.android.domain.model.Token
import id.vee.android.ui.welcome.WelcomeActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding

    private val viewModel: ProfileViewModel by viewModel()

    private var userToken: Token? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.title_profile)

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
        context?.apply {
            viewModel.getToken()
            viewModelListener(viewModel)
            binding?.apply {
                btnProfile.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_navigation_profile_to_navigation_profile_detail))
                btnLanguage.setOnClickListener { startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS)) }
                btnTheme.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_navigation_profile_to_navigation_theme))
                btnLogout.setOnClickListener {
                    btnLogout.isEnabled = false
                    viewModel.logout(userToken?.refreshToken ?: "")
                }
            }
        }
    }

    private fun viewModelListener(viewModel: ProfileViewModel) {
        viewModel.tokenResponse.observe(viewLifecycleOwner) {
            if (it != null) {
                userToken = it
                Log.d(TAG, "viewModelListener: $it")
            }
        }
        viewModel.logoutResponse.observe(viewLifecycleOwner) {
            if (it != null) {
                Log.d(TAG, "viewModelListener: $it")
                if (it.status == "success") {
                    val intent = Intent(activity, WelcomeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
                binding?.btnLogout?.isEnabled = true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "ProfileFragment"
    }
}