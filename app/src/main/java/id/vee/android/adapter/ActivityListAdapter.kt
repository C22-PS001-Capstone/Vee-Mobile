package id.vee.android.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import id.vee.android.adapter.ActivityListAdapter.MyViewHolder
import id.vee.android.databinding.RowStoriesBinding
import id.vee.android.domain.model.Activity
import id.vee.android.utils.formatDate

class ActivityListAdapter(private val onItemClick: (Activity) -> Unit) :
    ListAdapter<Activity, MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = RowStoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val activity = getItem(position)
        holder.bind(activity)
        holder.itemView.setOnClickListener {
            onItemClick(activity)
        }
    }

    class MyViewHolder(private val binding: RowStoriesBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(activity: Activity) {
            binding.storyDate.text = formatDate(activity.date)
            binding.storyKm.text = activity.km.toString()
            binding.storyLiter.text = activity.liter.toString()
            binding.storyPrice.text = activity.price.toString()
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<Activity> =
            object : DiffUtil.ItemCallback<Activity>() {
                override fun areItemsTheSame(
                    oldActivity: Activity,
                    newActivity: Activity
                ): Boolean {
                    return oldActivity.id == newActivity.id
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(
                    oldActivity: Activity,
                    newActivity: Activity
                ): Boolean {
                    return oldActivity == newActivity
                }
            }
    }
}