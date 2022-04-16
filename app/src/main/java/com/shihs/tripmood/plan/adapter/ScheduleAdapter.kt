package com.shihs.tripmood.plan.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shihs.tripmood.databinding.ItemScheduleBinding
import com.shihs.tripmood.dataclass.Schedule

class ScheduleAdapter : ListAdapter<Schedule, ScheduleAdapter.ScheduleVH>(DiffUtil()) {

    class ScheduleVH(private var binding: ItemScheduleBinding ) : RecyclerView.ViewHolder(binding.root){

        val dashline = binding.dashLine
        val expandLayout = binding.expandedView
        val cardView = binding.cardView

        fun bind(item: Schedule){
            binding.timeTv.text = item.time
            binding.noteTv.text = item.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleVH {
        return ScheduleVH(ItemScheduleBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ScheduleVH, position: Int) {
        val schedule = getItem(position)

        if (position == currentList.lastIndex){
            holder.dashline.visibility = View.INVISIBLE
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