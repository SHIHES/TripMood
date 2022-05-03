package com.shihs.tripmood.plan.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shihs.tripmood.databinding.ItemScheduleBinding
import com.shihs.tripmood.dataclass.Location
import com.shihs.tripmood.dataclass.Schedule
import java.text.SimpleDateFormat
import java.util.*



class EventAdapter(private val onClickListener: EventAdapter.OnClickListener) : ListAdapter<Schedule, EventAdapter.ScheduleVH>(DiffUtil()) {

    class OnClickListener(val clickListener: (schedule: Schedule) -> Unit) {
        fun onClick(schedule: Schedule) = clickListener(schedule)
    }

    class ScheduleVH(private var binding: ItemScheduleBinding ) : RecyclerView.ViewHolder(binding.root){

        val dashline = binding.dashLine
        val cardView = binding.cardView
        val fm1 = SimpleDateFormat("MM.dd h:mm a", Locale.getDefault())

        fun bind(item: Schedule){
            binding.timeTv.text = fm1.format(item.time)
            binding.ScheduleTitle.text = item.title
            binding.locationText.text = item.location?.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleVH {
        return ScheduleVH(ItemScheduleBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ScheduleVH, position: Int) {
        val schedule = getItem(position)

        if (position == currentList.size - 1){
            holder.dashline.visibility = View.INVISIBLE
        } else{
            holder.dashline.visibility = View.VISIBLE
        }
        holder.bind(schedule)

        holder.itemView.setOnClickListener {
            onClickListener.onClick(schedule = schedule)
        }
    }

    class DiffUtil : androidx.recyclerview.widget.DiffUtil.ItemCallback<Schedule>(){
        override fun areItemsTheSame(oldItem: Schedule, newItem: Schedule): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Schedule, newItem: Schedule): Boolean {
            return oldItem == newItem
        }

    }
}