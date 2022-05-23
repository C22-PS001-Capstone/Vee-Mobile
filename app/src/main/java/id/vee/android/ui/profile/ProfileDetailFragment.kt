package id.vee.android.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import id.vee.android.R
import id.vee.android.databinding.FragmentProfileDetailBinding
import id.vee.android.domain.model.Token
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileDetailFragment : Fragment() {
    private var _binding: FragmentProfileDetailBinding? = null
    private val binding get() = _binding

    private val viewModel: ProfileViewModel by viewModel()
    private var userToken: Token? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentProfileDetailBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).supportActionBar?.title =
            getString(R.string.title_profile_detail)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.apply {
            viewModel.apply {
                getUserData()
                getToken()
            }
            viewModelListener(viewModel)
            binding?.apply {
                val vCurrentFirstName = edtFirstName.text.toString()
                val vCurrentLastName = edtLastName.text.toString()
                updateName(userToken?.refreshToken?: "", vCurrentFirstName, vCurrentLastName)
            }
        }
        binding?.btnChangePassword?.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_navigation_profile_detail_to_navigation_change_password))
    }

    private fun updateName(token: String, currentFirstName: String, currentLastName: String) {
        binding?.apply {
            if (currentFirstName.isEmpty()) {
                edtFirstName.error = getString(R.string.first_name_is_required_error)
                edtFirstName.requestFocus()
                return
            }
            if (currentLastName.isEmpty()) {
                edtLastName.error = getString(R.string.last_name_is_required_error)
                edtLastName.requestFocus()
                return
            }
            btnSaveProfile.isEnabled = false
            btnSaveProfile.text = getString(R.string.save_profile_btn)
            viewModel.updateName(token, currentFirstName, currentLastName)
        }
    }

    private fun viewModelListener(viewModel: ProfileViewModel) {
        viewModel.apply {
            userResponse.observe(viewLifecycleOwner) { userData ->
                if (userData != null) {
                    binding?.apply {
                        edtFirstName.setText(userData.firstName)
                        edtLastName.setText(userData.lastName)
                        edtEmail.setText(userData.email)
                    }
                }
            }
            tokenResponse.observe(viewLifecycleOwner) {
                if (it != null) {
                    userToken = it
                    Log.d(TAG, "viewModelListener: $it")
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "ProfileDetailFragment"
    }
}