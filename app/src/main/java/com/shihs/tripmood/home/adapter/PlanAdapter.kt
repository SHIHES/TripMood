package com.shihs.tripmood.home.adapter

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.*
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shihs.tripmood.R
import com.shihs.tripmood.databinding.ItemPlanBinding
import com.shihs.tripmood.dataclass.Plan
import com.shihs.tripmood.home.childpage.ChildHomeViewModel
import java.text.SimpleDateFormat
import java.util.*

class PlanAdapter(private val onClickListener: OnClickListener, val viewModel: ChildHomeViewModel) : ListAdapter<Plan, PlanAdapter.PlanVH>(
    DiffUtil()
) {

    class PlanVH (private var binding: ItemPlanBinding) : RecyclerView.ViewHolder(binding.root){

        val moreBtn = binding.moreBtn
        val statusTv = binding.statusTextView


        fun bind(item: Plan, viewModel: ChildHomeViewModel){
            val formatTime = SimpleDateFormat("yyyy.MM.dd")

            binding.planTitle.text = item.title

            if(item.startDate == item.endDate){
                binding.tripDate.text = "${formatTime.format(item.startDate)}"

            } else{
                binding.tripDate.text = "${formatTime.format(item.startDate)} - ${formatTime.format(item.endDate)}"

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
        val context = holder.itemView.context
        val dialog = Dialog(context)
        val calendar = Calendar.getInstance(Locale.getDefault()).timeInMillis


        if (calendar < plan.startDate!!){
            holder.statusTv.text = "尚未開始"
           holder.statusTv.setBackgroundColor(context.getColor(R.color.tripMood_dark_blue))
        }

        if (plan.startDate!! >= calendar && plan.endDate!!.plus(86400000) <= calendar ){
            holder.statusTv.text = "進行中"
            holder.statusTv.setBackgroundColor(context.getColor(R.color.tripMood_green))
        }

        if (plan.endDate!!.plus(86400000) < calendar){
            holder.statusTv.text = "已結束"
            holder.statusTv.setBackgroundColor(context.getColor(R.color.tripMood_dark_blue))
        }


        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_plan_fuction_more)

        holder.itemView.setOnClickListener {
            onClickListener.onClick(plan)

        }

        holder.moreBtn.setOnClickListener {

            dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window?.setGravity(Gravity.BOTTOM)
            dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation


            dialog.findViewById<View>(R.id.deleteLayout).setOnClickListener {
                viewModel.deletePlan(plan = plan)
                notifyItemRemoved(position)
            }
            dialog.findViewById<View>(R.id.privateLayout).setOnClickListener {
                viewModel.changeToPersonal(plan = plan)
            }
            dialog.findViewById<View>(R.id.publicLayout).setOnClickListener {
                viewModel.changeToPublic(plan = plan)
            }
            dialog.findViewById<View>(R.id.friendListLayout).setOnClickListener {

            }
            dialog.findViewById<View>(R.id.editLayout).setOnClickListener {

            }
            dialog.findViewById<View>(R.id.shareLayout).setOnClickListener {

            }

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