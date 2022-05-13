package id.vee.android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import id.vee.android.databinding.ActivityNearestGasStationBinding

class NearestGasStationActivity : AppCompatActivity() {
    private var _binding: ActivityNearestGasStationBinding? = null
    private val binding get() = _binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityNearestGasStationBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Nearest Gas Station"
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