package id.vee.android.ui.login

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import id.vee.android.R
import id.vee.android.databinding.ActivityLoginBinding
import id.vee.android.utils.isValidEmail
import id.vee.android.vm.ViewModelFactory

class LoginActivity : AppCompatActivity() {
    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    private val viewModel: LoginViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            setContentView(root)
            btnLoginActivity.setOnClickListener {
                val vEmail = edtEmail.text.toString()
                val vPassword = edtPassword.text.toString()
                loginUser(vEmail, vPassword)
            }
        }
        viewModel.response.observe(this) { response ->
            if (response.status == "success") {
                AlertDialog.Builder(this)
                    .setTitle("Login Success")
                    .setMessage("Login success but still waiting the api get user detail")
                    .setPositiveButton("OK") { dialog, _ ->
                        dialog.dismiss()
                        finish()
                    }
                    .show()
            } else {
                AlertDialog.Builder(this)
                    .setTitle("Login Failed")
                    .setMessage("Email or Password is wrong")
                    .setPositiveButton("OK") { dialog, _ ->
                        dialog.dismiss()
                        binding.btnLoginActivity.isEnabled = true
                        binding.btnLoginActivity.text = resources.getText(R.string.log_in)
                    }
                    .show()
            }
            Log.d(TAG, "onCreate: $response")
        }
    }

    private fun loginUser(email: String, password: String) {
        binding.apply {
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
            btnLoginActivity.isEnabled = false
            btnLoginActivity.text = "Loading..."
        }
        viewModel.login(email, password)
    }

    companion object {
        private const val TAG = "LoginActivity"
    }
}