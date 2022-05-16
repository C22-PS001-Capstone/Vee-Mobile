package id.vee.android.ui.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import id.vee.android.databinding.FragmentAddActivityBinding
import id.vee.android.ui.home.HomeViewModel

class AddActivityFragment : Fragment() {
    private var _binding: FragmentAddActivityBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentAddActivityBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).supportActionBar?.title = "Add Activity"

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}