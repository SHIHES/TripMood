package com.shihs.tripmood.plan.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shihs.tripmood.databinding.ItemDayBinding
import com.shihs.tripmood.dataclass.Schedule
import com.shihs.tripmood.plan.MyPlanViewModel
import java.text.SimpleDateFormat
import java.util.*

class ScheduleAdapter(private val onClickListener: OnClickListener,private val viewModel: MyPlanViewModel) : ListAdapter<Schedule, ScheduleAdapter.DateVH>(DiffUtil()) {

    class OnClickListener(val clickListener: (schedule: Schedule) -> Unit) {
        fun onClick(schedule: Schedule) = clickListener(schedule)
    }

    class DateVH(private var binding: ItemDayBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Schedule, onClickListener: OnClickListener, viewModel: MyPlanViewModel) {

            val fm1 = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())


            binding.daysTv.text = "第${adapterPosition.plus(1)}天"
            binding.dateTv.text = fm1.format(item.time).toString()
            binding.root.setOnClickListener {
                Log.d("QAQ", "binding.root.setOnClickListener$item")
                onClickListener.onClick(item)
                viewModel.getSelectedAdapterPosition(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateVH {
        return DateVH(ItemDayBinding.inflate(LayoutInflater.from(parent.context), parent,false))
    }

    override fun onBindViewHolder(holder: DateVH, position: Int) {
        val date = getItem(position)
        holder.bind(date, onClickListener, viewModel)
    }

    class DiffUtil : androidx.recyclerview.widget.DiffUtil.ItemCallback<Schedule>() {
        override fun areItemsTheSame(oldItem: Schedule, newItem: Schedule): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Schedule, newItem: Schedule): Boolean {
            return oldItem == newItem
        }

    }

}