package id.vee.android.ui.gas

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import id.vee.android.databinding.ActivityNearestGasStationBinding
import id.vee.android.utils.BottomNavigationActivity
import id.vee.android.utils.BottomNavigationState

class NearestGasStationActivity : BottomNavigationActivity() {
    override var _binding: ActivityNearestGasStationBinding? = null
    override val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityNearestGasStationBinding.inflate(layoutInflater)
        binding?.apply {
            setContentView(root)
            val vNavView: BottomNavigationView = bottomNavigationView
            bottomNavigationState = BottomNavigationState.NEAREST_GAS_STATION
            setBottomNav(vNavView)
        }
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = "Nearest Gas Station"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}