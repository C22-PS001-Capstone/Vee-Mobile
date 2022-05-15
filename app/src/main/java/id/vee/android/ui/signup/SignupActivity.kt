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
        setContentView(binding?.root)
    }
}