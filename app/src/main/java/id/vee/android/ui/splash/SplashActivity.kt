package id.vee.android.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import id.vee.android.databinding.ActivitySplashBinding
import id.vee.android.ui.MainActivity
import id.vee.android.ui.welcome.WelcomeActivity
import id.vee.android.vm.ViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private var _binding: ActivitySplashBinding? = null
    private val binding get() = _binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        supportActionBar?.hide()

        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        val viewModel: SplashViewModel by viewModels {
            factory
        }
        lifecycleScope.launch(Dispatchers.Default) {
            delay(TIMEOUT)
            viewModel.getUser()
        }
        viewModel.userResponse.observe(this@SplashActivity) { user ->
            Log.d("Splash", "onCreate: $user")
            if (user != null || user?.isLogin == true) {
                val intent = Intent(this@SplashActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this@SplashActivity, WelcomeActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val TIMEOUT: Long = 1000
    }
}