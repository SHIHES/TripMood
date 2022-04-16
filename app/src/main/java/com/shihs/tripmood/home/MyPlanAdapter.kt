package com.shihs.tripmood.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shihs.tripmood.MobileNavigationDirections
import com.shihs.tripmood.databinding.ItemPlanBinding
import com.shihs.tripmood.dataclass.Plan
import com.shihs.tripmood.util.Logger

class MyPlanAdapter(private val listener: OnItemClickListener) : ListAdapter<Plan, MyPlanAdapter.PlanVH>(DiffUtil()) {

    class PlanVH (private var binding: ItemPlanBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item: Plan){
            binding.planTitle.text = item.title
            binding.tripDate.text = "${item.startDate} - ${item.endDate}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanVH {
        return PlanVH(ItemPlanBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: PlanVH, position: Int) {
        val plan = getItem(position)
        holder.bind(plan)
        holder.itemView.setOnClickListener {
            listener.onItemClick(plan = plan)
            Logger.d("holder.itemView.setOnClickListener")
        }
        Logger.d("onBindViewHolder")
    }

    class DiffUtil : androidx.recyclerview.widget.DiffUtil.ItemCallback<Plan>(){
        override fun areItemsTheSame(oldItem: Plan, newItem: Plan): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Plan, newItem: Plan): Boolean {
            return oldItem == newItem
        }
    }

    class OnItemClickListener(val clickListener: (plan: Plan) -> Unit){
        fun onItemClick(plan: Plan) = clickListener(plan)
    }
}