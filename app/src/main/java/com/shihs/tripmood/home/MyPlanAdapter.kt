package com.shihs.tripmood.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shihs.tripmood.databinding.ItemPlanBinding
import com.shihs.tripmood.dataclass.Plan
import java.text.SimpleDateFormat

class MyPlanAdapter(private val onClickListener: OnClickListener) : ListAdapter<Plan, MyPlanAdapter.PlanVH>(DiffUtil()) {

    class PlanVH (private var binding: ItemPlanBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item: Plan){
            val formatTime = SimpleDateFormat("yyyy.MM.dd")
            binding.planTitle.text = item.title
            binding.tripDate.text = "${formatTime.format(item.startDate)} - ${formatTime.format(item.endDate)}"

        }
    }

    class OnClickListener(val clickListener: (plan: Plan) -> Unit) {
        fun onClick(plan: Plan) = clickListener(plan)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanVH {
        return PlanVH(ItemPlanBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }


    override fun onBindViewHolder(holder: PlanVH, position: Int) {
        val plan = getItem(position)
        holder.bind(plan)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(plan) }
    }

    class DiffUtil : androidx.recyclerview.widget.DiffUtil.ItemCallback<Plan>(){
        override fun areItemsTheSame(oldItem: Plan, newItem: Plan): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Plan, newItem: Plan): Boolean {
            return oldItem == newItem
        }

    }
}