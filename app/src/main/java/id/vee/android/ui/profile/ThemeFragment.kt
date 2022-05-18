package id.vee.android.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import id.vee.android.R
import id.vee.android.databinding.FragmentProfileDetailBinding
import id.vee.android.databinding.FragmentThemeBinding
import id.vee.android.ui.home.HomeViewModel

class ThemeFragment : Fragment() {
    private var _binding: FragmentThemeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentThemeBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).supportActionBar?.title = "Theme"

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}