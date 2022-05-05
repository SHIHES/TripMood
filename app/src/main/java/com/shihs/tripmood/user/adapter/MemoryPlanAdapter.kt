package com.shihs.tripmood.user.adapter

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.*
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shihs.tripmood.R
import com.shihs.tripmood.databinding.ItemPlanBinding
import com.shihs.tripmood.databinding.ItemUserpagePlanBinding
import com.shihs.tripmood.dataclass.Plan
import com.shihs.tripmood.home.childpage.ChildHomeViewModel
import com.shihs.tripmood.util.PlanStatusFilter
import java.text.SimpleDateFormat
import java.util.*

class MemoryPlanAdapter(private val onClickListener: OnClickListener) : ListAdapter<Plan, MemoryPlanAdapter.PlanVH>(
    DiffUtil()
) {

    class PlanVH (private var binding: ItemUserpagePlanBinding) : RecyclerView.ViewHolder(binding.root){


        fun bind(item: Plan){
            val formatTime = SimpleDateFormat("yyyy.MM.dd")

            binding.memoryTitle.text = item.title

            if(item.startDate == item.endDate){
                binding.memoryDateTv.text = "${formatTime.format(item.startDate)}"

            } else{
                binding.memoryDateTv.text = "${formatTime.format(item.startDate)} - ${formatTime.format(item.endDate)}"

            }

        }
    }

    class OnClickListener(val clickListener: (plan: Plan) -> Unit) {
        fun onClick(plan: Plan) = clickListener(plan)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanVH {
        return PlanVH(ItemUserpagePlanBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }


    override fun onBindViewHolder(holder: PlanVH, position: Int) {
        val plan = getItem(position)
        holder.bind(plan)


        holder.itemView.setOnClickListener {
            onClickListener.onClick(plan)

        }
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