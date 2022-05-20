package id.vee.android.ui.splash

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import id.vee.android.data.local.ThemePreferences
import id.vee.android.databinding.ActivitySplashBinding
import id.vee.android.ui.GeneralViewModel
import id.vee.android.ui.MainActivity
import id.vee.android.ui.welcome.WelcomeActivity
import id.vee.android.vm.ViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivitySplashBinding.inflate(layoutInflater)
    }
    private val viewModel: GeneralViewModel by viewModels {
        val pref = ThemePreferences.getInstance(this.dataStore)
        ViewModelFactory.getInstance(this, pref)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()

        lifecycleScope.launch(Dispatchers.Default) {
            delay(TIMEOUT)
            viewModel.getUserData()
        }
        viewModel.userResponse.observe(this@SplashActivity) { user ->
            Log.d("Splash", "onCreate: $user")
            if (user != null) {
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

    companion object {
        const val TIMEOUT: Long = 1000
    }
}