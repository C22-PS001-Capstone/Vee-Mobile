package id.vee.android.ui.notification

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import id.vee.android.databinding.ActivityNotificationBinding
import id.vee.android.utils.BottomNavigationActivity

class NotificationActivity : BottomNavigationActivity() {
    override var _binding: ActivityNotificationBinding? = null
    override val binding get() = _binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityNotificationBinding.inflate(layoutInflater)
        binding?.apply {
            setContentView(root)
            val vNavView: BottomNavigationView = bottomNavigationView
            setBottomNav(vNavView)
        }
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = "Notification"
        }
    }
}