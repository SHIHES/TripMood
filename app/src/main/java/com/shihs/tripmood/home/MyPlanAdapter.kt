package com.shihs.tripmood.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shihs.tripmood.databinding.ItemPlanBinding
import com.shihs.tripmood.dataclass.Plan

class MyPlanAdapter(private val onClickListener: OnClickListener) : ListAdapter<Plan, MyPlanAdapter.PlanVH>(DiffUtil()) {

    class PlanVH (private var binding: ItemPlanBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item: Plan, onClickListener: OnClickListener){
            binding.planTitle.text = item.title
            binding.tripDate.text = "${item.startDate} - ${item.endDate}"
            binding.root.setOnClickListener {
                Log.d("SS", "binding.root.setOnClickListener$item")
                onClickListener.onClick(item) }
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
        holder.bind(plan, onClickListener)
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