package id.vee.android.ui.activity

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import id.vee.android.databinding.ActivityListBinding
import id.vee.android.utils.BottomNavigationActivity
import id.vee.android.utils.BottomNavigationState

class ListActivity : BottomNavigationActivity() {
    override var _binding: ActivityListBinding? = null
    override val binding get() = _binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityListBinding.inflate(layoutInflater)
        binding?.apply {
            setContentView(root)
            val vNavView: BottomNavigationView = bottomNavigationView
            bottomNavigationState = BottomNavigationState.ACTIVITY
            setBottomNav(vNavView)
        }
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = "Activity"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}