package id.vee.android.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import id.vee.android.R
import id.vee.android.databinding.FragmentChangePasswordBinding
import id.vee.android.domain.model.Token
import id.vee.android.utils.checkEmptyEditText
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChangePasswordFragment : Fragment() {
    private var _binding: FragmentChangePasswordBinding? = null
    private val binding get() = _binding

    private val viewModel: ProfileViewModel by viewModel()
    private var userToken: Token? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentChangePasswordBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).supportActionBar?.title =
            getString(R.string.change_password_title)

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
                btnUpdatePassword.setOnClickListener {
                    if (!checkEmptyEditText(edtCurrentPassword) && !checkEmptyEditText(
                            edtNewPassword
                        ) && !checkEmptyEditText(edtNewPasswordConfirm)
                    ) {
                        return@setOnClickListener
                    }
                    changePassword()
                }
                btnCreatePassword.setOnClickListener {
                    if (!checkEmptyEditText(
                            edtNewPassword
                        ) && !checkEmptyEditText(edtNewPasswordConfirm)
                    ) {
                        return@setOnClickListener
                    }
                    addPassword()
                }
            }
        }
    }

    private fun viewModelListener(viewModel: ProfileViewModel) {
        viewModel.apply {
            tokenResponse.observe(viewLifecycleOwner) { tokenData ->
                userToken = tokenData
                viewModel.userDetail(tokenData)
            }
            viewModel.userResponse.observe(viewLifecycleOwner) { userData ->
                binding?.apply {
                    if (userData.passNull) {
                        currentPasswordLabel.visibility = View.GONE
                        edtCurrentPassword.visibility = View.GONE
                        btnUpdatePassword.visibility = View.GONE
                        btnCreatePassword.visibility = View.VISIBLE
                        (activity as AppCompatActivity).supportActionBar?.title = getString(
                            R.string.create_password
                        )
                    }
                }
            }
            addPasswordResponse.observe(viewLifecycleOwner) { response ->
                binding?.apply {
                    if (response.status == "success") {
                        activity?.let {
                            AlertDialog.Builder(it)
                                .setTitle(getString(R.string.success))
                                .setMessage(getString(R.string.success_create_password))
                                .setPositiveButton(getString(R.string.positive_dialog_btn_text)) { dialog, _ ->
                                    dialog.dismiss()
                                }
                                .show()
                        }
                        currentPasswordLabel.visibility = View.VISIBLE
                        edtCurrentPassword.visibility = View.VISIBLE
                        btnCreatePassword.visibility = View.GONE
                        btnUpdatePassword.visibility = View.VISIBLE

                        edtNewPassword.setText("")
                        edtNewPasswordConfirm.setText("")

                        (activity as AppCompatActivity).supportActionBar?.title = getString(
                            R.string.update_password
                        )
                    } else {
                        activity?.let {
                            AlertDialog.Builder(it)
                                .setTitle(getString(R.string.error))
                                .setMessage(getString(R.string.error_create_password))
                                .setPositiveButton(getString(R.string.positive_dialog_btn_text)) { dialog, _ ->
                                    dialog.dismiss()
                                }
                                .show()
                        }
                        btnCreatePassword.visibility = View.VISIBLE
                        btnCreatePassword.isEnabled = true
                    }
                }
            }

            updatePasswordResponse.observe(viewLifecycleOwner) { response ->
                if (response.status == "success") {
                    activity?.let {
                        AlertDialog.Builder(it)
                            .setTitle(getString(R.string.success))
                            .setMessage(getString(R.string.success_update_password))
                            .setPositiveButton(getString(R.string.positive_dialog_btn_text)) { dialog, _ ->
                                dialog.dismiss()
                            }
                            .show()
                    }
                    binding?.apply{
                        edtNewPassword.setText("")
                        edtNewPasswordConfirm.setText("")
                    }
                } else {
                    activity?.let {
                        AlertDialog.Builder(it)
                            .setTitle(getString(R.string.error))
                            .setMessage(getString(R.string.current_password_wrong))
                            .setPositiveButton(getString(R.string.positive_dialog_btn_text)) { dialog, _ ->
                                dialog.dismiss()
                            }
                            .show()
                    }
                }
                binding?.apply {
                    btnUpdatePassword.isEnabled = true
                    btnUpdatePassword.text = resources.getText(R.string.update_password)
                }
            }
        }
    }

    private fun changePassword() {
        binding?.apply {
            btnUpdatePassword.isEnabled = false
            btnUpdatePassword.text = getString(R.string.loading_btn)
            val currentPassword = edtCurrentPassword.text.toString()
            val password = edtNewPassword.text.toString()
            val passwordConfirm = edtNewPasswordConfirm.text.toString()
            if (password != passwordConfirm) {
                edtNewPasswordConfirm.error = getString(R.string.password_confirm_not_match_error)
                edtNewPasswordConfirm.requestFocus()
                return
            }
            viewModel.updatePassword(
                userToken?.accessToken ?: "",
                currentPassword,
                password,
                passwordConfirm
            )
        }
    }

    private fun addPassword() {
        binding?.apply {
            btnCreatePassword.isEnabled = false
            btnCreatePassword.text = getString(R.string.loading_btn)
            val password = edtNewPassword.text.toString()
            val passwordConfirm = edtNewPasswordConfirm.text.toString()
            if (password != passwordConfirm) {
                edtNewPasswordConfirm.error = getString(R.string.password_confirm_not_match_error)
                edtNewPasswordConfirm.requestFocus()
                return
            }
            viewModel.addPassword(
                userToken?.accessToken ?: "",
                password,
                passwordConfirm
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}