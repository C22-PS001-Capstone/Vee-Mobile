package id.vee.android.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import id.vee.android.R
import id.vee.android.adapter.GasStationListAdapter.MyViewHolder.Companion.DIFF_CALLBACK
import id.vee.android.databinding.RowGasStationBinding
import id.vee.android.domain.model.GasStations
import java.util.*

class GasStationListAdapter :
    ListAdapter<GasStations, GasStationListAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            RowGasStationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val gasStation = getItem(position)
        holder.bind(gasStation, holder.itemView.context)
    }

    class MyViewHolder(private val binding: RowGasStationBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {
        @SuppressLint("SetTextI18n")
        fun bind(gasStations: GasStations, context: Context) {
            binding.vendorGasStation.text = gasStations.vendor
            binding.nameGasStation.text = gasStations.name
            gasStations.distance?.toDouble()?.let { distance ->
                binding.distanceGasStation.apply {
                    when {
                        distance < 0.3 -> {
                            text = "${distance * 1000} m"
                            setTextColor(
                                ContextCompat.getColor(
                                    context,
                                    R.color.limegreen
                                )
                            )
                        }
                        distance < 0.5 -> {
                            text = "${distance * 1000} m"
                            setTextColor(
                                ContextCompat.getColor(
                                    context,
                                    R.color.primary
                                )
                            )
                        }
                        else -> {
                            text = "$distance km"
                            setTextColor(
                                ContextCompat.getColor(
                                    context,
                                    R.color.black
                                )
                            )
                        }
                    }
                }
                when (gasStations.vendor) {
                    "Pertamina" -> binding.ivVendor.setImageResource(R.drawable.pertamina)
                    "Shell" -> binding.ivVendor.setImageResource(R.drawable.shell)
                    else -> binding.ivVendor.setImageResource(R.drawable.other_vendor_gasstaions)
                }
                try {
                    if (gasStations.lat != 0.0 && gasStations.lon != 0.0) {
                        val geocoder = Geocoder(context, Locale.getDefault())
                        val addresses =
                            geocoder.getFromLocation(gasStations.lat, gasStations.lon, 1)
                        val address = StringBuilder()
                        addresses[0]?.apply {
                            address.append(this.locality)
                            address.append(", ")
                            address.append(this.adminArea)
                        }
                        binding.addressGasStations.text = address
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

}
