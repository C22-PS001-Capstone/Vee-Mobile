package id.vee.android.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import id.vee.android.databinding.ActivitySplashBinding
import id.vee.android.ui.GeneralViewModel
import id.vee.android.ui.MainActivity
import id.vee.android.ui.welcome.WelcomeActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivitySplashBinding.inflate(layoutInflater)
    }
    private val viewModel: GeneralViewModel by viewModel()

    private var isTokenValid: Boolean? = null
    private var isUserValid: Boolean? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()

        lifecycleScope.launch(Dispatchers.Default) {
            delay(TIMEOUT)
            viewModel.getUserData()
            viewModel.getToken()
        }
        viewModel.userResponse.observe(this@SplashActivity) { user ->
            Log.d("Splash", "onCreate: $user")
            isUserValid = user != null
            checkNavigation()
        }
        viewModel.tokenResponse.observe(this@SplashActivity) { token ->
            Log.d("Splash", "onCreate: token")
            isTokenValid = token != null
            checkNavigation()
        }
        viewModel.getThemeSettings()
        viewModel.themeResponse.observe(
            this
        ) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    private fun checkNavigation() {
        Log.d("validation", "checkNavigation: TEstttt")
        isTokenValid?.let { tokenValid ->
            Log.d("validation", "checkNavigation: TEstttt")
            isUserValid?.let { isUserValid ->
                Log.d("validation", "checkNavigation: TEstttt")
                if (tokenValid && isUserValid) {
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
    }

    companion object {
        const val TIMEOUT: Long = 1000
    }
}