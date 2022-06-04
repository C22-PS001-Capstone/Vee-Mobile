package id.vee.android.ui.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import id.vee.android.R
import id.vee.android.adapter.NotificationListAdapter
import id.vee.android.databinding.FragmentNotificationBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class NotificationFragment : Fragment() {
    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding

    private val viewModel: NotificationViewModel by viewModel()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNotificationBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).supportActionBar?.title =
            getString(R.string.title_notification)

        setupBackButton()

        return binding?.root
    }

    private fun setupBackButton() {
        if (activity is AppCompatActivity) {
            (activity as AppCompatActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getNotification()
        val notificationAdapter = NotificationListAdapter()
        binding?.apply {
            progressBar.visibility = View.VISIBLE
            rvNotification.apply {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = notificationAdapter
            }
            viewModel.notificationResponse.observe(viewLifecycleOwner) { listNotification ->
                if (listNotification.isEmpty()) {
                    notificationAdapter.submitList(null)
                    emptyNotificationImage.visibility = View.VISIBLE
                    emptyNotificationText.visibility = View.VISIBLE
                } else {
                    notificationAdapter.submitList(listNotification)
                    emptyNotificationImage.visibility = View.GONE
                    emptyNotificationText.visibility = View.GONE
                }
                progressBar.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}