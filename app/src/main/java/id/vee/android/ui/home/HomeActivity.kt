package id.vee.android.ui.home

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import id.vee.android.R
import id.vee.android.databinding.ActivityMainBinding
import id.vee.android.utils.BottomNavigationActivity

class HomeActivity : BottomNavigationActivity() {
    override var _binding: ActivityMainBinding? = null
    override val binding get() = _binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        binding?.apply {
            setContentView(root)

            val vNavView: BottomNavigationView = bottomNavigationView
            setBottomNav(vNavView)
        }
        title = getString(R.string.welcome)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}