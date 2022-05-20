package id.vee.android.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import id.vee.android.R
import id.vee.android.databinding.FragmentProfileDetailBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileDetailFragment : Fragment() {
    private var _binding: FragmentProfileDetailBinding? = null
    private val binding get() = _binding

    private val viewModel: ProfileViewModel by viewModel()


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
            viewModel.getUserData()
            viewModelListener(viewModel)
        }
        binding?.btnChangePassword?.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_navigation_profile_detail_to_navigation_change_password))
    }

    private fun viewModelListener(viewModel: ProfileViewModel) {
        viewModel.userResponse.observe(viewLifecycleOwner) { userData ->
            if (userData != null) {
                binding?.apply {
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