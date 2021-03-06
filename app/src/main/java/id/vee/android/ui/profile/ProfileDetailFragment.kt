package id.vee.android.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import id.vee.android.R
import id.vee.android.databinding.FragmentProfileDetailBinding
import id.vee.android.domain.model.Token
import id.vee.android.utils.DataMapper
import id.vee.android.utils.checkEmptyEditText
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
                btnSaveProfile.setOnClickListener {
                    if (!checkEmptyEditText(edtFirstName) && !checkEmptyEditText(edtLastName)
                    ) {
                        return@setOnClickListener
                    }
                    updateName(viewModel)
                }
            }
        }
        binding?.apply {
            btnChangePassword.setOnClickListener(
                Navigation.createNavigateOnClickListener(
                    R.id.action_navigation_profile_detail_to_navigation_change_password
                )
            )
        }
    }

    private fun updateName(viewModel: ProfileViewModel) {
        binding?.apply {
            btnSaveProfile.isEnabled = false
            btnSaveProfile.text = getString(R.string.save_profile_btn)
            val currentFirstName = edtFirstName.text.toString()
            val currentLastName = edtLastName.text.toString()
            viewModel.updateName(userToken?.accessToken ?: "", currentFirstName, currentLastName)
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
                        if (userData.passNull) {
                            btnChangePassword.setText(R.string.create)
                        }
                    }
                }
            }
            tokenResponse.observe(viewLifecycleOwner) { tokenData ->
                userToken = tokenData
            }
            updateNameResponse.observe(viewLifecycleOwner) { response ->
                if (response.status == "success") {
                    userToken?.let { viewModel.userDetail(it) }
                } else {
                    activity?.let {
                        AlertDialog.Builder(it)
                            .setTitle(getString(R.string.error))
                            .setMessage(response.message)
                            .setPositiveButton(getString(R.string.positive_dialog_btn_text)) { dialog, _ ->
                                dialog.dismiss()
                            }
                            .show()
                    }
                }
                binding?.apply {
                    btnSaveProfile.isEnabled = true
                    btnSaveProfile.text = resources.getText(R.string.save_profile)
                }
            }
            responseDetail.observe(viewLifecycleOwner) { response ->
                if (response.status == "success" && response.data != null && response.data.user != null) {
                    val mapperData = DataMapper.mapEntityToDomain(response.data.user)
                    viewModel.saveUser(mapperData)
                    activity?.let {
                        AlertDialog.Builder(it)
                            .setTitle(getString(R.string.success))
                            .setMessage(getString(R.string.success_update_profile))
                            .setPositiveButton(getString(R.string.positive_dialog_btn_text)) { dialog, _ ->
                                dialog.dismiss()
                            }
                            .show()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}