package id.vee.android.ui.login

import android.app.Activity
import android.content.Intent
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
import id.vee.android.ui.MainActivity
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

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Log In"
            elevation = 0f
        }

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
            if (response.status == "success" && response.data != null) {
                val data = response.data
                Log.d(TAG, "onCreate: Testing")
                viewModel.saveToken(data)
                viewModel.userDetail(data)
            } else {
                showLoginFailed()
            }
            Log.d(TAG, "onCreate: $response")
        }
        viewModel.responseDetail.observe(this) { response ->
            if (response.status == "success" && response.data != null && response.data.user != null) {
                viewModel.saveUser(response.data.user)
                val intent = (Intent(this, MainActivity::class.java))
                intent.flags =
                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            } else {
                showLoginFailed()
            }
        }
    }
    private fun showLoginFailed(){
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