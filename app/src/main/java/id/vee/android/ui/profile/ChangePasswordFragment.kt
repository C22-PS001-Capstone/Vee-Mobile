package id.vee.android.ui.profile

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
import id.vee.android.databinding.FragmentChangePasswordBinding
import id.vee.android.vm.ViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class ChangePasswordFragment : Fragment() {
    private var _binding: FragmentChangePasswordBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentChangePasswordBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).supportActionBar?.title = "Change Password"

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.apply {
            val pref = ThemePreferences.getInstance(this.dataStore)

            val factory: ViewModelFactory = ViewModelFactory.getInstance(this, pref)
            val viewModel: ProfileViewModel by viewModels {
                factory
            }
            viewModel.getUserData()
            viewModelListener(viewModel, this)
        }
    }

    private fun viewModelListener(viewModel: ProfileViewModel, context: Context) {
        binding.apply {
            btnUpdatePassword.setOnClickListener {
                val vCurrentPassword = edtCurrentPassword.text.toString()
                val vNewPassword = edtNewPassword.text.toString()
                val vNewPasswordConfirm = edtNewPasswordConfirm.text.toString()
                changePassword(vCurrentPassword, vNewPassword, vNewPasswordConfirm)
            }
        }
    }

    private fun changePassword(
        currentPassword: String,
        newPassword: String,
        newPasswordConfirm: String
    ) {
        binding.apply {
            if (currentPassword.isEmpty()) {
                edtCurrentPassword.error = "Current password is required"
                edtCurrentPassword.requestFocus()
                return
            }
            if (newPassword.length < 6) {
                edtNewPassword.error = "Password must be at least 6 characters"
                edtNewPassword.requestFocus()
                return
            }
            if (newPassword != newPasswordConfirm) {
                edtNewPasswordConfirm.error = "Password confirmation is not match"
                edtNewPasswordConfirm.requestFocus()
                return
            }
            btnUpdatePassword.isEnabled = false
            btnUpdatePassword.text = getString(R.string.loading_btn)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}