package id.vee.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import id.vee.android.databinding.ActivityNearestGasStationBinding

class NearestGasStationActivity : AppCompatActivity() {
    private var _binding: ActivityNearestGasStationBinding? = null
    private val binding get() = _binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityNearestGasStationBinding.inflate(layoutInflater)
        setContentView(binding?.root)
    }
}