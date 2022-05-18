package id.vee.android.ui

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
        supportActionBar?.apply {
            elevation = 0f
        }

        binding.apply {
            setContentView(root)

            val navView: BottomNavigationView = bottomNavigationView
            val navController = findNavController(R.id.nav_host_fragment_activity_main)

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

        // Permission
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                AlertDialog.Builder(this).apply {
                    setTitle(getString(R.string.missing_permission))
                    setMessage(getString(R.string.missing_permission_description))
                    setPositiveButton(getString(R.string.yes)) { _, _ -> }
                    show()
                }
            }
        }
    }
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }
    companion object {
        val REQUIRED_PERMISSIONS = arrayOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )
        const val REQUEST_CODE_PERMISSIONS = 10
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