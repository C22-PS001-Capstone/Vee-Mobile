package id.vee.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import id.vee.android.databinding.ActivityFormBinding

class AddActivity : AppCompatActivity() {
    private var _binding: ActivityFormBinding? = null
    private val binding get() = _binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityFormBinding.inflate(layoutInflater)
        setContentView(binding?.root)
    }
}