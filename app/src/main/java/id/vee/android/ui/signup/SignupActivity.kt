package id.vee.android.ui.signup

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import id.vee.android.R
import id.vee.android.databinding.ActivitySignupBinding
import id.vee.android.ui.login.LoginActivity
import id.vee.android.utils.isValidEmail
import kotlinx.coroutines.DelicateCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel

@DelicateCoroutinesApi
class SignupActivity : AppCompatActivity() {

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivitySignupBinding.inflate(layoutInflater)
    }
    private val viewModel: SignupViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.sign_up)
            elevation = 0f
        }

        binding.apply {
            setContentView(root)
            btnSignupActivity.setOnClickListener {
                val vFirstName = edtFirstName.text.toString()
                val vLastName = edtLastName.text.toString()
                val vEmail = edtEmail.text.toString()
                val vPassword = edtPassword.text.toString()
                val vPasswordConfirm = edtPasswordConfirm.text.toString()
                registerUser(vFirstName, vLastName, vEmail, vPassword, vPasswordConfirm)
            }
        }
        viewModel.response.observe(this) { response ->
            if (response.status == "success") {
                AlertDialog.Builder(this)
                    .setTitle(getString(R.string.success))
                    .setMessage(getString(R.string.success_account_created))
                    .setPositiveButton(getString(R.string.positive_dialog_btn_text)) { _, _ ->
                        startActivity(
                            Intent(this@SignupActivity, LoginActivity::class.java)
                        )
                        finish()
                    }
                    .show()
            } else {
                AlertDialog.Builder(this)
                    .setTitle(getString(R.string.error))
                    .setMessage(getString(R.string.sign_up_error))
                    .setPositiveButton(getString(R.string.positive_dialog_btn_text)) { dialog, _ ->
                        dialog.dismiss()
                        binding.btnSignupActivity.isEnabled = true
                        binding.btnSignupActivity.text = resources.getText(R.string.sign_up)
                    }
                    .show()
            }
        }
    }

    private fun registerUser(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        passwordConfirm: String
    ) {
        binding.apply {
            if (firstName.isEmpty()) {
                edtFirstName.error = getString(R.string.first_name_is_required_error)
                edtFirstName.requestFocus()
                return
            }
            if (lastName.isEmpty()) {
                edtLastName.error = getString(R.string.last_name_is_required_error)
                edtLastName.requestFocus()
                return
            }
            if (email.isEmpty()) {
                edtEmail.error = getString(R.string.email_is_required_error)
                edtEmail.requestFocus()
                return
            }
            if (!email.isValidEmail()) {
                edtEmail.error = getString(R.string.email_is_not_valid_error)
                edtEmail.requestFocus()
                return
            }
            if (password.isEmpty()) {
                edtPassword.error = getString(R.string.password_is_required_error)
                edtPassword.requestFocus()
                return
            }
            if (password.length < 6) {
                edtPassword.error = getString(R.string.password_hint)
                edtPassword.requestFocus()
                return
            }
            if (password != passwordConfirm) {
                edtPasswordConfirm.error = getString(R.string.password_confirm_not_match_error)
                edtPasswordConfirm.requestFocus()
                return
            }
            btnSignupActivity.isEnabled = false
            btnSignupActivity.text = getString(R.string.signing_up_btn)
            viewModel.signup(firstName, lastName, email, password, passwordConfirm)
        }
    }
}