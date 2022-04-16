package com.shihs.tripmood.plan.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shihs.tripmood.databinding.ItemDayBinding
import java.text.SimpleDateFormat
import java.util.*

class DateAdapter : ListAdapter<Long, DateAdapter.DateVH>(DiffUtil()) {

    class DateVH(private var binding: ItemDayBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Long) {

            val fm1 = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())

            binding.daysTv.text = "第${adapterPosition.plus(1)}天"
            binding.dateTv.text = fm1.format(item).toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateVH {
        return DateVH(ItemDayBinding.inflate(LayoutInflater.from(parent.context), parent,false))
    }

    override fun onBindViewHolder(holder: DateVH, position: Int) {
        val date = getItem(position)
        holder.bind(date)
    }

    class DiffUtil : androidx.recyclerview.widget.DiffUtil.ItemCallback<Long>() {
        override fun areItemsTheSame(oldItem: Long, newItem: Long): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Long, newItem: Long): Boolean {
            return oldItem == newItem
        }

    }
}