package id.vee.android.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import id.vee.android.ui.gas.NearestGasStationActivity
import id.vee.android.ui.notification.NotificationActivity
import id.vee.android.ui.profile.ProfileActivity
import id.vee.android.R
import id.vee.android.databinding.ActivityListBinding
import id.vee.android.ui.home.HomeActivity

class ListActivity : AppCompatActivity() {
    private var _binding: ActivityListBinding? = null
    private val binding get() = _binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityListBinding.inflate(layoutInflater)
        binding?.apply{
            setContentView(root)
            val vNavView: BottomNavigationView = bottomNavigationView

            setBottomNav(vNavView)
        }
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = "Activity"
        }
    }

    private fun setBottomNav(navView: BottomNavigationView) {
        navView.selectedItemId = R.id.navigation_activity
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
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}