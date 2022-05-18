package com.shihs.tripmood.search.adapter

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.*
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
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

            Glide.with(itemView.context).load(item.image).placeholder(R.drawable.placeholder).into(binding.planCoverPic)

            val formatTime = SimpleDateFormat("yyyy.MM.dd")

            binding.planTitle.text = item.title

            binding.moreBtn.visibility = View.INVISIBLE

            binding.statusTextView.visibility = View.INVISIBLE


            if (item.startDate == item.endDate) {
                binding.tripDate.text = "${formatTime.format(item.startDate)}"

            } else {
                binding.tripDate.text =
                    "${formatTime.format(item.startDate)} - ${formatTime.format(item.endDate)}"

            }

            binding.favoriteBtn.setOnCheckedChangeListener { compoundButton, b ->

                if (compoundButton.isChecked){
                    item.let { viewModel.addFavoritePlan(it) }
                }
                else{
                    item.let { viewModel.cancelFavoritePlan(it) }
                }
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