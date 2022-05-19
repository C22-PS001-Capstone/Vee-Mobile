package id.vee.android.ui.gas

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import id.vee.android.databinding.FragmentNearestGasStationBinding

class NearestGasStationFragment : Fragment() {
    private var _binding: FragmentNearestGasStationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNearestGasStationBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).supportActionBar?.title = "Nearest Gas Station"

        setupBackButton()

        return binding.root
    }

    private fun setupBackButton() {
        if (activity is AppCompatActivity) {
            (activity as AppCompatActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}