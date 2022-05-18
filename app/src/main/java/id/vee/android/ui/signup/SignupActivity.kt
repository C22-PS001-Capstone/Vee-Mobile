package id.vee.android.ui.signup

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import id.vee.android.R
import id.vee.android.databinding.ActivitySignupBinding
import id.vee.android.ui.login.LoginActivity
import id.vee.android.utils.isValidEmail
import id.vee.android.vm.ViewModelFactory

class SignupActivity : AppCompatActivity() {

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivitySignupBinding.inflate(layoutInflater)
    }
    private val viewModel: SignupViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var actionBar = getSupportActionBar()

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
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
                    .setTitle("Success")
                    .setMessage("Your account has been created")
                    .setPositiveButton("OK") { dialog, _ ->
                        startActivity(
                            Intent(this@SignupActivity, LoginActivity::class.java)
                        )
                        finish()
                    }
                    .show()
            } else {
                AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage(response.message)
                    .setPositiveButton("OK") { dialog, _ ->
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
                edtFirstName.error = "First name is required"
                edtFirstName.requestFocus()
                return
            }
            if (lastName.isEmpty()) {
                edtLastName.error = "Last name is required"
                edtLastName.requestFocus()
                return
            }
            if (email.isEmpty()) {
                edtEmail.error = "Email is required"
                edtEmail.requestFocus()
                return
            }
            if (!email.isValidEmail()) {
                edtEmail.error = "Email is not valid"
                edtEmail.requestFocus()
                return
            }
            if (password.isEmpty()) {
                edtPassword.error = "Password is required"
                edtPassword.requestFocus()
                return
            }
            if (password.length < 6) {
                edtPassword.error = "Password must be at least 6 characters"
                edtPassword.requestFocus()
                return
            }
            if (password != passwordConfirm) {
                edtPasswordConfirm.error = "Password confirmation is not match"
                edtPasswordConfirm.requestFocus()
                return
            }
            btnSignupActivity.isEnabled = false
            btnSignupActivity.text = "Signing up..."
            viewModel.signup(firstName, lastName, email, password, passwordConfirm)
        }
    }

    companion object {
        private const val TAG = "SignupActivity"
    }
}