package com.shihs.tripmood.home

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.*
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shihs.tripmood.R
import com.shihs.tripmood.databinding.DialogPlanFuctionMoreBinding
import com.shihs.tripmood.databinding.ItemPlanBinding
import com.shihs.tripmood.dataclass.Plan
import java.text.SimpleDateFormat

class MyPlanAdapter(private val onClickListener: OnClickListener) : ListAdapter<Plan, MyPlanAdapter.PlanVH>(DiffUtil()) {

    class PlanVH (private var binding: ItemPlanBinding) : RecyclerView.ViewHolder(binding.root){

        val moreBtn = binding.moreBtn

        fun bind(item: Plan){
            val formatTime = SimpleDateFormat("yyyy.MM.dd")
            binding.planTitle.text = item.title
            binding.tripDate.text = "${formatTime.format(item.startDate)} - ${formatTime.format(item.endDate)}"
            binding.moreBtn.setOnClickListener {


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
        val context = holder.itemView.context
        holder.bind(plan)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(plan) }

        holder.moreBtn.setOnClickListener {
            val dialog = Dialog(context)
            val binding = DialogPlanFuctionMoreBinding.inflate(LayoutInflater.from(context))
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.dialog_plan_fuction_more)

            binding.deleteLayout
            binding.privateLayout
            binding.openLayout
            binding.friendListLayout
            binding.editLayout
            binding.shareLayout

            dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window?.setGravity(Gravity.BOTTOM)
            dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation

            dialog.show()
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