package id.vee.android.ui.profile

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import id.vee.android.R
import id.vee.android.databinding.FragmentProfileDetailBinding
import id.vee.android.ui.home.HomeViewModel
import id.vee.android.vm.ViewModelFactory

class ProfileDetailFragment : Fragment() {
    private var _binding: FragmentProfileDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentProfileDetailBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).supportActionBar?.title = "Profile Detail"

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.apply {
            val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
            val viewModel: ProfileViewModel by viewModels {
                factory
            }
            viewModel.getUserData()
            viewModelListener(viewModel, this)
        }
        binding.btnChangePassword.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_navigation_profile_detail_to_navigation_change_password))
    }

    private fun viewModelListener(viewModel: ProfileViewModel, context: Context) {
        viewModel.userResponse.observe(viewLifecycleOwner) { userData ->
            if (userData != null) {
                binding.apply {
                    edtFirstName.setText(userData.firstName)
                    edtLastName.setText(userData.lastName)
                    edtEmail.setText(userData.email)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}