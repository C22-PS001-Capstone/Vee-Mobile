package id.vee.android.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import id.vee.android.R
import id.vee.android.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding.apply {
            setContentView(root)

            val navView: BottomNavigationView = bottomNavigationView
            val navController = findNavController(R.id.nav_host_fragment_activity_main)
            title = getString(R.string.welcome)


            val appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.navigation_home,
                    R.id.navigation_gas_station,
                    R.id.navigation_activity,
                    R.id.navigation_profile,
                )
            )
            setupActionBarWithNavController(navController, appBarConfiguration)
            navView.setupWithNavController(navController)
        }
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        binding.fabAddActivity.setOnClickListener {
            navController.navigateUp()
            navController.navigate(R.id.navigation_add_activity)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.action_bar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            R.id.menu_notification -> {
                val navController = findNavController(R.id.nav_host_fragment_activity_main)
                navController.navigateUp()
                navController.navigate(R.id.navigation_notification)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}