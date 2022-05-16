package id.vee.android.ui.profile

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import id.vee.android.databinding.ActivityProfileBinding
import id.vee.android.utils.BottomNavigationActivity
import id.vee.android.utils.BottomNavigationState

class ProfileActivity : BottomNavigationActivity() {
    override var _binding: ActivityProfileBinding? = null
    override val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityProfileBinding.inflate(layoutInflater)
        binding?.apply {
            setContentView(root)
            val vNavView: BottomNavigationView = bottomNavigationView
            bottomNavigationState = BottomNavigationState.PROFILE
            setBottomNav(vNavView)
        }
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = "Profile"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}