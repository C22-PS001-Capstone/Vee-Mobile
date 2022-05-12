package id.vee.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import id.vee.android.databinding.ActivityNotificationBinding

class NotificationActivity : AppCompatActivity() {
    private var _binding: ActivityNotificationBinding? = null
    private val binding get() = _binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding?.root)
    }
}