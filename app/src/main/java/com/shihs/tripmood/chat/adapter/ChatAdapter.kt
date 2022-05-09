package com.shihs.tripmood.chat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.appworks.school.publisher.ext.toDisplayDateFormat
import app.appworks.school.publisher.ext.toDisplayTimeFormat
import com.shihs.tripmood.databinding.ItemChatMeBinding
import com.shihs.tripmood.databinding.ItemChatOtherBinding
import com.shihs.tripmood.dataclass.Chat
import com.shihs.tripmood.util.UserManager

class ChatAdapter : ListAdapter<Chat, RecyclerView.ViewHolder>(DiffCallback){

    class MeChatViewHolder(private var binding: ItemChatMeBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(chat: Chat) {

            binding.textGchatDateMe.text = chat.createdTime?.toDisplayDateFormat()
            binding.textGchatMessageMe.text = chat.msg
            binding.textGchatTimestampMe.text = chat.createdTime?.toDisplayTimeFormat()

        }
    }

    class OtherChatViewHolder(private var binding: ItemChatOtherBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(chat: Chat) {

            binding.textGchatDateOther.text = chat.createdTime?.toDisplayDateFormat()
//            binding.imageGchatProfileOther = chat.speaker.image
            binding.textGchatMessageOther.text = chat.msg
            binding.textGchatTimestampOther.text = chat.createdTime?.toDisplayTimeFormat()
            binding.textGchatUserOther.text = chat.speaker?.name


        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Chat>() {
        override fun areItemsTheSame(oldItem: Chat, newItem: Chat): Boolean {
            return oldItem === newItem
        }
        override fun areContentsTheSame(oldItem: Chat, newItem: Chat): Boolean {
            return oldItem.id == newItem.id
        }

        private const val ITEM_CHAT_ME = 0x00
        private const val ITEM_CHAT_OTHER = 0x01
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {

            ITEM_CHAT_ME -> MeChatViewHolder(
                ItemChatMeBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
            ITEM_CHAT_OTHER -> OtherChatViewHolder(
                ItemChatOtherBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )

            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder) {
            is MeChatViewHolder -> {
                holder.bind((getItem(position)))
            }
            is OtherChatViewHolder -> {
                holder.bind((getItem(position)))
            }
        }
    }

    override fun getItemViewType(position: Int): Int {

        val speakerID = getItem(position)

        if( speakerID.speaker?.uid == UserManager.userUID){

            return ITEM_CHAT_ME

        } else{

            return ITEM_CHAT_OTHER

        }
    }

}