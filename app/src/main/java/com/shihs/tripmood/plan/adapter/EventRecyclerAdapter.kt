package com.shihs.tripmood.plan.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shihs.tripmood.databinding.ItemScheduleBinding
import com.shihs.tripmood.dataclass.Schedule
import java.text.SimpleDateFormat
import java.util.*



/** For testing difference between listAdapter and Recyclerview adapter **/
class EventRecyclerAdapter : RecyclerView.Adapter<EventRecyclerAdapter.ScheduleVH>() {

    private var schedules: List<Schedule>? = null

    class ScheduleVH(private var binding: ItemScheduleBinding ) : RecyclerView.ViewHolder(binding.root){

        val dashline = binding.dashLine
        val expandLayout = binding.expandedView
        val cardView = binding.cardView
        val fm1 = SimpleDateFormat("yyyy.MM.dd h:mm a", Locale.getDefault())

        fun bind(item: Schedule){
            item.let {

                binding.timeTv.text = fm1.format(item.time)
                binding.noteTv.text = item.title
                binding.cardView.setOnClickListener {
                    if (item.expand == false){
                        expandLayout.visibility = View.GONE
                        item.expand = true
                    } else {
                        expandLayout.visibility = View.VISIBLE
                        item.expand = false
                    }

                }
            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleVH {
        return ScheduleVH(ItemScheduleBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ScheduleVH, position: Int) {

        schedules?.let {
            holder.bind(it[getRealPosition(position)])
        }

        if (position == schedules?.size?.minus(1) ?: 0){
            holder.dashline.visibility = View.INVISIBLE
        } else{
            holder.dashline.visibility = View.VISIBLE
        }


    }

    override fun getItemCount(): Int {
        return schedules?.let { Int.MAX_VALUE } ?: 0
    }

    private fun getRealPosition(position: Int): Int = schedules?.let {
        position % it.size
    } ?: 0

    fun submitSchedules(schedules: List<Schedule>) {
        this.schedules = schedules
        notifyDataSetChanged()
    }
}