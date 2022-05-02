package com.shihs.tripmood.search.adapter

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.*
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shihs.tripmood.R
import com.shihs.tripmood.databinding.ItemPlanBinding
import com.shihs.tripmood.dataclass.Plan
import com.shihs.tripmood.home.childpage.ChildHomeViewModel
import com.shihs.tripmood.search.SearchViewModel
import java.text.SimpleDateFormat

class SearchPlanAdapter(private val onClickListener: OnClickListener, val viewModel: SearchViewModel) : ListAdapter<Plan, SearchPlanAdapter.PlanVH>(
    (DiffUtil())
) {

    class PlanVH(private var binding: ItemPlanBinding) : RecyclerView.ViewHolder(binding.root) {



        fun bind(item: Plan, viewModel: SearchViewModel) {
            val formatTime = SimpleDateFormat("yyyy.MM.dd")
            binding.planTitle.text = item.title

            binding.moreBtn.visibility = View.INVISIBLE

            if (item.startDate == item.endDate) {
                binding.tripDate.text = "${formatTime.format(item.startDate)}"

            } else {
                binding.tripDate.text =
                    "${formatTime.format(item.startDate)} - ${formatTime.format(item.endDate)}"

            }

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
        holder.bind(plan, viewModel)

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