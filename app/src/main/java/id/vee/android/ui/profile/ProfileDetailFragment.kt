package id.vee.android.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import id.vee.android.R
import id.vee.android.databinding.FragmentProfileDetailBinding

class ProfileDetailFragment : Fragment() {
    private var _binding: FragmentProfileDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentProfileDetailBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).supportActionBar?.title = "Profile Detail"

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnChangePassword.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_navigation_profile_detail_to_navigation_change_password))

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}