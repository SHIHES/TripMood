package com.shihs.tripmood.plan.adapter

import android.app.Dialog
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Base64
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shihs.tripmood.R
import com.shihs.tripmood.databinding.DialogPlanFuctionMoreBinding
import com.shihs.tripmood.databinding.DialogPlanRequestAddBinding
import com.shihs.tripmood.databinding.DialogPlanRequestTitleBinding
import com.shihs.tripmood.databinding.ItemPlaceInfoBinding
import com.shihs.tripmood.dataclass.Location
import com.shihs.tripmood.plan.mygps.MyGPSViewModel

class LocationAdapter(private val onClickListener: OnClickListener) : ListAdapter<Location, LocationAdapter.LocationVH>(DiffUtil()) {


    class OnClickListener(val clickListener: (location: Location) -> Unit) {
        fun onClick(location: Location) = clickListener(location)
    }

    class LocationVH(private var binding: ItemPlaceInfoBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Location) {

            binding.spotAddress.text = item.address
            binding.spotTitile.text = item.name
            Glide.with(binding.spotIcon.context).load(item.icon).error(R.drawable.tripmood_logo)
                .override(56,56)
                .into(binding.spotIcon)

            if (item.rating == null){
                binding.ratingBar.visibility = View.GONE
            } else{
                binding.ratingBar.rating = item.rating!!.toFloat()
                binding.textRatingNum.text = item.rating.toString()
            }


            if(item.image == null){
                binding.spotPhoto.setImageResource(R.drawable.tripmood_logo)
            } else {
                val imageBytes = Base64.decode(item.image, Base64.DEFAULT)
                val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                binding.spotPhoto.setImageBitmap(decodedImage)
            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationVH {

        val locationVH = LocationVH(ItemPlaceInfoBinding.inflate(LayoutInflater.from(parent.context), parent,false))

        return locationVH
    }

    override fun onBindViewHolder(holder: LocationVH, position: Int) {
        val location = getItem(position)
        holder.bind(location)


        holder.itemView.setOnClickListener(object : DoubleClickListener() {
            override fun onDoubleClick(v: View?) {
                onClickListener.onClick(location = location)
            }
        })



    }


    class DiffUtil : androidx.recyclerview.widget.DiffUtil.ItemCallback<Location>() {
        override fun areItemsTheSame(oldItem: Location, newItem: Location): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Location, newItem: Location): Boolean {
            return oldItem == newItem
        }

    }

    abstract class DoubleClickListener : View.OnClickListener {
        var lastClickTime: Long = 0
        override fun onClick(v: View?) {
            val clickTime = System.currentTimeMillis()
            if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA) {
                onDoubleClick(v)
            }
            lastClickTime = clickTime
        }

        abstract fun onDoubleClick(v: View?)

        companion object {
            private const val DOUBLE_CLICK_TIME_DELTA: Long = 300 //milliseconds
        }
    }


}