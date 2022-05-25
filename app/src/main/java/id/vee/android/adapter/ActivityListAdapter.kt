package id.vee.android.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import id.vee.android.adapter.ActivityListAdapter.MyViewHolder
import id.vee.android.databinding.RowStoriesBinding
import id.vee.android.domain.model.Activity
import id.vee.android.utils.formatDate
import java.util.*

class ActivityListAdapter(private val onItemClick: (Activity) -> Unit) :
    ListAdapter<Activity, MyViewHolder>(DIFF_CALLBACK) {
    private var listMonth = ArrayList<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = RowStoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val activity = getItem(position)
        holder.bind(activity, holder.itemView.context, listMonth)
        holder.itemView.setOnClickListener {
            onItemClick(activity)
        }
    }

    fun clearMonth() {
        listMonth.clear()
    }

    class MyViewHolder(private val binding: RowStoriesBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(activity: Activity, context: Context, listMonth: ArrayList<String>) {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses = geocoder.getFromLocation(activity.lat, activity.lon, 1)
            binding.storyDate.text = activity.date.formatDate()
            binding.storyKm.text = activity.km.toString()
            binding.storyLiter.text = activity.liter.toString()
            binding.storyPrice.text = activity.price.toString()
            if (activity.lat != 0.0 && activity.lon != 0.0) {
                binding.storyAddress.text = addresses[0].getAddressLine(0)
            } else {
                binding.storyAddress.visibility = View.GONE
            }
            val formattedMonth = activity.date.formatDate("MMM").toString()
            if (!listMonth.contains(formattedMonth)) {
                binding.monthName.text = activity.date.formatDate("MMM")
                listMonth.add(formattedMonth)
            } else {
                binding.monthName.visibility = View.GONE
            }
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