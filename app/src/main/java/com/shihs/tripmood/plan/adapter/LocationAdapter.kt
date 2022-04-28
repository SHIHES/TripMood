package com.shihs.tripmood.plan.adapter

import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shihs.tripmood.R
import com.shihs.tripmood.databinding.ItemPlaceInfoBinding
import com.shihs.tripmood.dataclass.source.Location
import kotlinx.coroutines.withContext

class LocationAdapter(private val onClickListener: OnClickListener, ) : ListAdapter<Location, LocationAdapter.LocationVH>(DiffUtil()) {

    class OnClickListener(val clickListener: (location: Location) -> Unit) {
        fun onClick(location: Location) = clickListener(location)
    }

    class LocationVH(private var binding: ItemPlaceInfoBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Location, onClickListener: OnClickListener) {

            binding.spotAddress.text = item.address
            binding.spotTitile.text = item.name
            Glide.with(binding.spotIcon.context).load(item.icon).error(R.drawable.the_professor_digital_painting_money_heist_ke)
                .override(56,56)
                .into(binding.spotIcon)

            if (item.rating == null){
                binding.ratingBar.visibility = View.GONE
            } else{
                binding.ratingBar.rating = item.rating!!.toFloat()
                binding.textRatingNum.text = item.rating.toString()
            }


            if(item.image == null){
                binding.spotPhoto.setImageResource(R.drawable.the_professor_digital_painting_money_heist_ke)
            } else {
                val imageBytes = Base64.decode(item.image, Base64.DEFAULT)
                val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                binding.spotPhoto.setImageBitmap(decodedImage)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationVH {
        return LocationVH(ItemPlaceInfoBinding.inflate(LayoutInflater.from(parent.context), parent,false))
    }

    override fun onBindViewHolder(holder: LocationVH, position: Int) {
        val date = getItem(position)
        holder.bind(date, onClickListener)
    }

    class DiffUtil : androidx.recyclerview.widget.DiffUtil.ItemCallback<Location>() {
        override fun areItemsTheSame(oldItem: Location, newItem: Location): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Location, newItem: Location): Boolean {
            return oldItem == newItem
        }

    }

}