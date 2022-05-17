package id.vee.android.ui.welcome

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import id.vee.android.databinding.ActivitySplashBinding
import id.vee.android.databinding.ActivityWelcomeBinding
import id.vee.android.ui.login.LoginActivity
import id.vee.android.ui.signup.SignupActivity
import id.vee.android.ui.splash.SplashViewModel
import id.vee.android.vm.ViewModelFactory

class WelcomeActivity : AppCompatActivity() {
    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityWelcomeBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding.apply {
            setContentView(root)

            btnLoginActivity.setOnClickListener {
                val intent = Intent(this@WelcomeActivity, LoginActivity::class.java)
                startActivity(intent)
            }
            btnSignupActivity.setOnClickListener {
                val intent = Intent(this@WelcomeActivity, SignupActivity::class.java)
                startActivity(intent)
            }
        }
    }
}