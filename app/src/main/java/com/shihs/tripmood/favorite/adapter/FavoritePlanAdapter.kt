package com.shihs.tripmood.favorite.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shihs.tripmood.R
import com.shihs.tripmood.databinding.ItemPlanBinding
import com.shihs.tripmood.dataclass.Plan
import com.shihs.tripmood.favorite.FavoriteViewModel
import java.text.SimpleDateFormat
import java.util.*

class FavoritePlanAdapter(
    private val onClickListener: OnClickListener,
    val viewModel: FavoriteViewModel
) : ListAdapter<Plan, FavoritePlanAdapter.PlanVH>(
    (DiffUtil())
) {

    class PlanVH(private var binding: ItemPlanBinding) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item: Plan) {
            Glide.with(itemView.context).load(item.image).placeholder(R.drawable.placeholder).into(
                binding.planCoverPic
            )

            val formatTime = SimpleDateFormat("yyyy.MM.dd", Locale.TAIWAN)

            binding.planTitle.text = item.title

            binding.favoriteBtn.visibility = View.INVISIBLE

            binding.moreBtn.visibility = View.INVISIBLE

            binding.statusTextView.visibility = View.INVISIBLE

            if (item.startDate == item.endDate) {
                binding.tripDate.text = formatTime.format(item.startDate)
            } else {
                binding.tripDate.text =
                    "${formatTime.format(item.startDate)} - ${formatTime.format(item.endDate)}"
            }

            binding.favoriteBtn.setOnCheckedChangeListener { _, _ ->
            
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
