package id.vee.android.ui.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import id.vee.android.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        binding?.apply {
            setContentView(root)
        }
    }
}