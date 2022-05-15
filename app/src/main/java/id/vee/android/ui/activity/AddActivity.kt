package id.vee.android.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import id.vee.android.*
import id.vee.android.databinding.ActivityFormBinding
import id.vee.android.ui.gas.NearestGasStationActivity
import id.vee.android.ui.home.HomeActivity
import id.vee.android.ui.notification.NotificationActivity
import id.vee.android.ui.profile.ProfileActivity
import id.vee.android.utils.BottomNavigationActivity

class AddActivity : BottomNavigationActivity() {
    override var _binding: ActivityFormBinding? = null
    override val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityFormBinding.inflate(layoutInflater)
        binding?.apply {
            setContentView(root)
            val vNavView: BottomNavigationView = bottomNavigationView

            setBottomNav(vNavView)
        }
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = "Add Activity"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}