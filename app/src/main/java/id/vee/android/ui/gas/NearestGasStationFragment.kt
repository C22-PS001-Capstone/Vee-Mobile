package id.vee.android.ui.gas

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import id.vee.android.R
import id.vee.android.databinding.FragmentAddActivityBinding
import id.vee.android.databinding.FragmentNearestGasStationBinding
import id.vee.android.ui.activity.ActivityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class NearestGasStationFragment : Fragment() {
    private var _binding: FragmentNearestGasStationBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ActivityViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNearestGasStationBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).supportActionBar?.title =
            getString(R.string.nearest_gas_station)
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
        context?.apply {
            viewModelListener(viewModel, this)
        }
    }

    private fun viewModelListener(viewModel: ActivityViewModel, context: Context) {
        viewModel.tokenResponse.observe(viewLifecycleOwner) { tokenData ->

        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}