package id.vee.android.ui.gas

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import id.vee.android.databinding.FragmentNearestGasStationBinding
import id.vee.android.ui.home.HomeViewModel

class NearestGasStationFragment : Fragment() {
    private var _binding: FragmentNearestGasStationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentNearestGasStationBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).supportActionBar?.title = "Nearest Gas Station"

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}