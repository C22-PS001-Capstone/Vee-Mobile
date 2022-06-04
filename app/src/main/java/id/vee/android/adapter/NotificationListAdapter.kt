package id.vee.android.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import id.vee.android.adapter.NotificationListAdapter.MyViewHolder
import id.vee.android.databinding.RowNotificationBinding
import id.vee.android.domain.model.Notification

class NotificationListAdapter : ListAdapter<Notification, MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            RowNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val notification = getItem(position)
        holder.bind(notification)
    }

    class MyViewHolder(private val binding: RowNotificationBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(notification: Notification) {
            binding.notificationText.text = notification.notification
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<Notification> =
            object : DiffUtil.ItemCallback<Notification>() {
                override fun areItemsTheSame(
                    oldNotification: Notification,
                    newNotification: Notification
                ): Boolean {
                    return oldNotification.createdAt == newNotification.createdAt
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(
                    oldNotification: Notification,
                    newNotification: Notification
                ): Boolean {
                    return oldNotification == newNotification
                }
            }
    }
}