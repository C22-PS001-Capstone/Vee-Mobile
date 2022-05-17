package id.vee.android.ui.login

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import id.vee.android.BuildConfig
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

        // Google sign-in
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestId()
            .requestProfile()
            .requestEmail()
            .requestIdToken(BuildConfig.GOOGLE_CLIENT_ID)
            .requestServerAuthCode(BuildConfig.GOOGLE_CLIENT_ID)
            .build()
        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.apply {
            setContentView(root)
            btnLoginActivity.setOnClickListener {
                val vEmail = edtEmail.text.toString()
                val vPassword = edtPassword.text.toString()
                loginUser(vEmail, vPassword)
            }
            googleSignIn.setOnClickListener {
                val signInIntent = mGoogleSignInClient.signInIntent
                resultLauncher.launch(signInIntent)
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

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account = task.getResult(ApiException::class.java)!!
                    account.idToken?.let { loginWithGoogleId(it) }
                } catch (e: ApiException) {
                    Log.w(TAG, "Google sign in failed: ", e)
                    Toast.makeText(this, "Google sign in failed", Toast.LENGTH_SHORT).show()
                }
            }
        }

    private fun loginWithGoogleId(idToken: String) {
        Log.d(TAG, "loginWithGoogleId: $idToken")
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