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
import id.vee.android.databinding.RowGasStationBinding
import id.vee.android.domain.model.GasStations
import java.util.*

class GasStationListAdapter :
    ListAdapter<GasStations, GasStationListAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = RowGasStationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val gasStation = getItem(position)
        holder.bind(gasStation, holder.itemView.context)
    }

    class MyViewHolder(private val binding: RowGasStationBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(gasStations: GasStations, context: Context) {
            binding.vendorGasStation.text = gasStations.vendor
            binding.nameGasStation.text = gasStations.name
            binding.distanceGasStation.text = gasStations.distance
            try {
                if (gasStations.lat != 0.0 && gasStations.lon != 0.0) {
                    val geocoder = Geocoder(context, Locale.getDefault())
                    val addresses = geocoder.getFromLocation(gasStations.lat, gasStations.lon, 1)
                    binding.addressGasStations.text = addresses[0].getAddressLine(0)
                } else {
                    binding.addressGasStations.visibility = View.GONE
                }
            } catch (IOException: Exception) {
                binding.addressGasStations.visibility = View.GONE
            }
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<GasStations> =
            object : DiffUtil.ItemCallback<GasStations>() {
                override fun areItemsTheSame(
                    oldGasStations: GasStations,
                    newGasStations: GasStations
                ): Boolean {
                    return oldGasStations.id == newGasStations.id
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(
                    oldGasStations: GasStations,
                    newGasStations: GasStations
                ): Boolean {
                    return oldGasStations == newGasStations
                }
            }
    }
}
