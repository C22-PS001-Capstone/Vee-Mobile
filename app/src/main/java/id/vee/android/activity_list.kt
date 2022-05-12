package id.vee.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import id.vee.android.databinding.ActivityListBinding

class activity_list : AppCompatActivity() {
    private var _binding: ActivityListBinding? = null
    private val binding get() = _binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding?.root)
    }
}