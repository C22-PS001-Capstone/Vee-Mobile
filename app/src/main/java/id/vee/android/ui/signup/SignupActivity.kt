package id.vee.android.ui.signup

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import id.vee.android.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {
    private var _binding: ActivitySignupBinding? = null
    private val binding get() = _binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySignupBinding.inflate(layoutInflater)

        binding?.apply {
            setContentView(root)
            btnSignupActivity.setOnClickListener{
                val vFirstName = edtFirstName.text.toString()
                val vLastName = edtLastName.text.toString()
                val vEmail = edtEmail.text.toString()
                val vPassword = edtPassword.text.toString()
                registerUser(vFirstName, vLastName, vEmail, vPassword)
            }
        }
    }

    private fun registerUser(firstName: String, lastName: String, email: String, password: String) {
        // To do
    }
}