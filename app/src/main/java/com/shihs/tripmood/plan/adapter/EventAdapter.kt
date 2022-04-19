package com.shihs.tripmood.plan.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shihs.tripmood.databinding.ItemScheduleBinding
import com.shihs.tripmood.dataclass.Schedule
import java.text.SimpleDateFormat
import java.util.*



class EventAdapter : ListAdapter<Schedule, EventAdapter.ScheduleVH>(DiffUtil()) {

    class ScheduleVH(private var binding: ItemScheduleBinding ) : RecyclerView.ViewHolder(binding.root){

        val dashline = binding.dashLine
        val expandLayout = binding.expandedView
        val cardView = binding.cardView
        val fm1 = SimpleDateFormat("yyyy.MM.dd h:mm a", Locale.getDefault())

        fun bind(item: Schedule){
            binding.timeTv.text = fm1.format(item.time)
            binding.noteTv.text = item.title
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

        holder.cardView.setOnClickListener {
            if (schedule.expand == false){
                holder.expandLayout.visibility = View.GONE
                schedule.expand = true
                notifyDataSetChanged()
            } else {
                holder.expandLayout.visibility = View.VISIBLE
                schedule.expand = false
                notifyDataSetChanged()
            }

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