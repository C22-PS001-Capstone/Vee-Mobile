package id.vee.android.ui.notification

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import id.vee.android.R
import id.vee.android.databinding.ActivityNotificationBinding
import id.vee.android.ui.activity.ListActivity
import id.vee.android.ui.gas.NearestGasStationActivity
import id.vee.android.ui.home.HomeActivity
import id.vee.android.ui.profile.ProfileActivity

class NotificationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNotificationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Notification"

        val vNavView: BottomNavigationView = binding.bottomNavigationView

        setBottomNav(vNavView)
    }

    private fun setBottomNav(navView: BottomNavigationView) {
        navView.selectedItemId = R.id.navigation_home
        navView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> {
                    val i = Intent(this, HomeActivity::class.java)
                    startActivity(i)
                    true
                }
                R.id.navigation_gas_station -> {
                    val i = Intent(this, NearestGasStationActivity::class.java)
                    startActivity(i)
                    true
                }
                R.id.navigation_activity -> {
                    val i = Intent(this, ListActivity::class.java)
                    startActivity(i)
                    true
                }
                R.id.navigation_profile -> {
                    val i = Intent(this, ProfileActivity::class.java)
                    startActivity(i)
                    true
                }
                else -> true
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.action_bar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.notification -> {
                val i = Intent(this, NotificationActivity::class.java)
                startActivity(i)
                true
            }
            else -> true
        }
    }
}