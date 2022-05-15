package id.vee.android.utils

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import id.vee.android.R
import id.vee.android.ui.activity.ListActivity
import id.vee.android.ui.gas.NearestGasStationActivity
import id.vee.android.ui.home.HomeActivity
import id.vee.android.ui.notification.NotificationActivity
import id.vee.android.ui.profile.ProfileActivity

enum class BottomNavigationState {
    HOME,
    ACTIVITY,
    NEAREST_GAS_STATION,
    PROFILE
}

open class BottomNavigationActivity : AppCompatActivity() {
    protected open val _binding: ViewBinding? = null
    protected open val binding get() = _binding
    protected open var bottomNavigationState: BottomNavigationState = BottomNavigationState.HOME

    protected fun setBottomNav(navView: BottomNavigationView) {
        navView.selectedItemId = when (bottomNavigationState) {
            BottomNavigationState.HOME -> R.id.navigation_home
            BottomNavigationState.ACTIVITY -> R.id.navigation_activity
            BottomNavigationState.NEAREST_GAS_STATION -> R.id.navigation_gas_station
            BottomNavigationState.PROFILE -> R.id.navigation_profile
        }
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