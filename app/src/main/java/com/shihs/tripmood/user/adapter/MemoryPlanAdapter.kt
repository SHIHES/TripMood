package com.shihs.tripmood.user.adapter

import android.annotation.SuppressLint
import android.view.*
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shihs.tripmood.databinding.ItemUserpagePlanBinding
import com.shihs.tripmood.dataclass.Plan
import java.text.SimpleDateFormat

class MemoryPlanAdapter(private val onClickListener: OnClickListener) : ListAdapter<Plan, MemoryPlanAdapter.PlanVH>(
    DiffUtil()
) {

    class PlanVH(private var binding: ItemUserpagePlanBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {

        @SuppressLint("SetTextI18n", "SimpleDateFormat")
        fun bind(item: Plan) {
            val formatTime = SimpleDateFormat("yyyy.MM.dd")

            binding.memoryTitle.text = item.title

            if (item.startDate == item.endDate) {
                binding.memoryDateTv.text = formatTime.format(item.startDate)
            } else {
                binding.memoryDateTv.text = "${formatTime.format(item.startDate)} - ${formatTime.format(
                    item.endDate
                )}"
            }
        }
    }

    class OnClickListener(val clickListener: (plan: Plan) -> Unit) {
        fun onClick(plan: Plan) = clickListener(plan)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanVH {
        return PlanVH(
            ItemUserpagePlanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: PlanVH, position: Int) {
        val plan = getItem(position)
        holder.bind(plan)

        holder.itemView.setOnClickListener {
            onClickListener.onClick(plan)
        }
    }

    class DiffUtil : androidx.recyclerview.widget.DiffUtil.ItemCallback<Plan>() {
        override fun areItemsTheSame(oldItem: Plan, newItem: Plan): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Plan, newItem: Plan): Boolean {
            return oldItem == newItem
        }
    }
}
