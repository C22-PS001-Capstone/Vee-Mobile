package id.vee.android

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import id.vee.android.databinding.ActivityFormBinding

class AddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFormBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Add Activity"

        val vNavView: BottomNavigationView = binding.bottomNavigationView

        setBottomNav(vNavView)
    }

    private fun setBottomNav(navView: BottomNavigationView) {
        navView.selectedItemId = R.id.navigation_activity
        navView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> {
                    val i = Intent(this, MainActivity::class.java)
                    startActivity(i)
                    true
                }
                R.id.navigation_gas_station -> {
                    val i = Intent(this, NearestGasStationActivity::class.java)
                    startActivity(i)
                    true
                }
                R.id.navigation_activity -> {
                    val i = Intent(this, activity_list::class.java)
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