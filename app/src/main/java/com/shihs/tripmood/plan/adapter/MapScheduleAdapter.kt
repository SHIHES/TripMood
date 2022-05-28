package com.shihs.tripmood.plan.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shihs.tripmood.R
import com.shihs.tripmood.databinding.ItemScheduleInfoMapBinding
import com.shihs.tripmood.dataclass.Schedule
import com.shihs.tripmood.plan.ShowAllLocationViewModel
import com.shihs.tripmood.util.CatalogFilter
import java.text.SimpleDateFormat

class MapScheduleAdapter(
    private val onClickListener: OnClickListener,
    private val viewModel: ShowAllLocationViewModel
) :
    ListAdapter<Schedule, MapScheduleAdapter.ScheduleLocationVH>(DiffUtil) {

    class OnClickListener(val clickListener: (schedule: Schedule) -> Unit) {
        fun onClick(schedule: Schedule) = clickListener(schedule)
    }

    class ScheduleLocationVH(private var binding: ItemScheduleInfoMapBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {

        val layout = binding.daysMapInfoLayout
        val icon = binding.catalogIcon
        
        @SuppressLint("SimpleDateFormat")
        fun bind(item: Schedule, onClickListener: OnClickListener) {
            val fmt = SimpleDateFormat("HH:mm").format(item.time)

            binding.root.setOnClickListener { onClickListener.onClick(item) }
            binding.daysInfoTv.text = itemView.context.getString(R.string.schedule_theDay, item.theDay)
            binding.daysTimeInfoTv.text = fmt
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleLocationVH {
        return ScheduleLocationVH(
            ItemScheduleInfoMapBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ScheduleLocationVH, position: Int) {
        val schedule = getItem(position)
        val context = holder.itemView.context
        holder.bind(getItem(position), onClickListener)

        when (schedule.theDay) {
            1 -> holder.layout.setCardBackgroundColor(context.getColor(R.color.day1))
            2 -> holder.layout.setCardBackgroundColor(context.getColor(R.color.day2))
            3 -> holder.layout.setCardBackgroundColor(context.getColor(R.color.day3))
            4 -> holder.layout.setCardBackgroundColor(context.getColor(R.color.day4))
            5 -> holder.layout.setCardBackgroundColor(context.getColor(R.color.day5))
            6 -> holder.layout.setCardBackgroundColor(context.getColor(R.color.day6))
            7 -> holder.layout.setCardBackgroundColor(context.getColor(R.color.day7))

            else -> holder.layout.setCardBackgroundColor(context.getColor(R.color.black))
        }

        when (schedule.catalog) {
            CatalogFilter.FOOD.value -> {
                holder.icon.setImageResource(R.drawable.dish)
            }
            CatalogFilter.HOTEL.value -> {
                holder.icon.setImageResource(R.drawable.hotel)
            }
            CatalogFilter.VEHICLE.value -> {
                holder.icon.setImageResource(R.drawable.car)
            }
            CatalogFilter.SHOPPING.value -> {
                holder.icon.setImageResource(R.drawable.online_shopping)
            }
            CatalogFilter.ATTRACTION.value -> {
                holder.icon.setImageResource(R.drawable.map)
            }
            else -> {
                holder.icon.setImageResource(R.drawable.ask)
            }
        }
    }

    companion object DiffUtil : androidx.recyclerview.widget.DiffUtil.ItemCallback<Schedule>() {
        override fun areItemsTheSame(oldItem: Schedule, newItem: Schedule): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Schedule, newItem: Schedule): Boolean {
            return oldItem == newItem
        }
    }
}
