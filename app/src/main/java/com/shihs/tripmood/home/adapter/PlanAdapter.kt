package com.shihs.tripmood.home.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shihs.tripmood.R
import com.shihs.tripmood.databinding.ItemPlanBinding
import com.shihs.tripmood.databinding.ItemPlanCoworkImageBinding
import com.shihs.tripmood.dataclass.Plan
import com.shihs.tripmood.home.childpage.ChildHomeViewModel
import java.text.SimpleDateFormat
import java.util.*

class PlanAdapter(private val onClickListener: OnClickListener, val viewModel: ChildHomeViewModel) :
    ListAdapter<Plan, PlanAdapter.PlanVH>(
        DiffUtil()
    ) {

    class PlanVH(private var binding: ItemPlanBinding) : RecyclerView.ViewHolder(binding.root) {

        val moreBtn = binding.moreBtn
        val statusTv = binding.statusTextView

        @SuppressLint("SetTextI18n")
        fun bind(item: Plan, viewModel: ChildHomeViewModel) {
            val formatTime = SimpleDateFormat("yyyy.MM.dd", Locale.TAIWAN)

            binding.favoriteBtn.visibility = View.GONE

            binding.planTitle.text = item.title

            Glide.with(itemView.context).load(item.image)
                .placeholder(R.drawable.placeholder)
                .centerCrop()
                .into(binding.planCoverPic)

            if (item.startDate == item.endDate) {
                binding.tripDate.text = formatTime.format(item.startDate)
            } else {
                binding.tripDate.text =
                    "${formatTime.format(item.startDate)} - ${formatTime.format(item.endDate)}"
            }

            if (!item.coworkingList.isNullOrEmpty()) {
                viewModel.realUserDataList.forEach {
                    if (item.coworkingList!!.contains(it.uid)) {
                        val itemPlanCoworkingImageBinding =
                            ItemPlanCoworkImageBinding.inflate(
                                LayoutInflater.from(itemView.context)
                            )

                        Log.d("SS", "count function run times")
                        Glide.with(itemView.context).load(it.image).override(100, 100)
                            .into(itemPlanCoworkingImageBinding.coworkImage)
                        binding.coworkerImageLayoutChild.addView(itemPlanCoworkingImageBinding.root)
                    }
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
        val context = holder.itemView.context
        val dialog = Dialog(context)
        val calendar = Calendar.getInstance(Locale.getDefault()).timeInMillis
        val editTextLayout = LayoutInflater.from(context).inflate(R.layout.view_edittext, null)
        val oneDayMillisecond = 86400000

        if (editTextLayout.parent != null) {
            editTextLayout.parent
        }

        if (calendar < plan.startDate!!) {
            holder.statusTv.text = "尚未開始"
            holder.statusTv.setBackgroundColor(context.getColor(R.color.tripMood_dark_blue))
        }

        if (plan.startDate!! >= calendar && plan.endDate!!.plus(oneDayMillisecond) <= calendar) {
            holder.statusTv.text = "進行中"
            holder.statusTv.setBackgroundColor(context.getColor(R.color.tripMood_green))
        }

        if (plan.endDate!!.plus(oneDayMillisecond) < calendar) {
            holder.statusTv.text = "已結束"
            holder.statusTv.setBackgroundColor(context.getColor(R.color.tripMood_dark_blue))
        }

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_plan_fuction_more)

        holder.itemView.setOnClickListener {
            onClickListener.onClick(plan)
        }

        holder.moreBtn.setOnClickListener {
            val dialogContext = dialog.context

            dialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
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
            dialog.findViewById<View>(R.id.inviteFriendLayout).setOnClickListener {
                AlertDialog.Builder(dialogContext).apply {
                    setTitle(dialogContext.getString(R.string.inviteTitle))
                    setView(editTextLayout)
                    setCancelable(false)
                    setPositiveButton(R.string.accept) { _, _ ->
                        val text =
                            editTextLayout.findViewById<EditText>(R.id.alertDialogEditText).text.toString()
                        if (text.isNotEmpty()) {
                            viewModel.changeEmailToUserID(text)
                            viewModel.getDialogSelectedPlan(plan)
                        } else {
                            Toast.makeText(context, "沒輸入訊息", Toast.LENGTH_LONG).show()
                        }
                    }
                    setNegativeButton(R.string.cancel) { _, _ ->
                    }
                    setOnCancelListener {
                        (editTextLayout.parent as ViewGroup).removeView(editTextLayout)
                    }
                }.show()
            }

            dialog.show()
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
