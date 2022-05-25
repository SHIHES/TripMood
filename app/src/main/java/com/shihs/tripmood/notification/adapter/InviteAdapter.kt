package com.shihs.tripmood.notification.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shihs.tripmood.R
import com.shihs.tripmood.databinding.ItemInviteNotificationBinding
import com.shihs.tripmood.dataclass.Invite
import com.shihs.tripmood.notification.NotificationViewModel
import com.shihs.tripmood.util.Util.getString

class InviteAdapter(
    private val onClickListener: OnClickListener,
    private val viewModel: NotificationViewModel
) :
    ListAdapter<Invite, InviteAdapter.InviteVH>(DiffUtil()) {

    class OnClickListener(val clickListener: (invite: Invite) -> Unit) {
        fun onClick(invite: Invite) = clickListener(invite)
    }

    class InviteVH(private var binding: ItemInviteNotificationBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {

        fun bind(invite: Invite, viewModel: NotificationViewModel) {
            val inviteName = invite.senderName
            val inviteTitle = invite.invitePlanTitle

            binding.InviteMsg.text = "$inviteName \n邀請你一同參加 \n${inviteTitle}旅程"

            binding.notificationAcceptBtn.setOnClickListener {
                viewModel.acceptInviteChangeStatus(inviteID = invite.id.toString())
                viewModel.acceptInviteAddUserToPlan(invite = invite)
                binding.buttonControlLayout.visibility = View.INVISIBLE
                binding.responseNotification.visibility = View.VISIBLE
                binding.responseNotification.text = getString(R.string.acceptInvite)
            }
            binding.refusedNotificationBtn.setOnClickListener {
                viewModel.refusedInvite(inviteID = invite.id.toString())
                binding.buttonControlLayout.visibility = View.INVISIBLE
                binding.responseNotification.visibility = View.VISIBLE
                binding.responseNotification.text = getString(R.string.refusedInvite)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InviteVH {
        return InviteVH(
            ItemInviteNotificationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: InviteVH, position: Int) {
        val notification = getItem(position)

        holder.bind(notification, viewModel)

        holder.itemView.setOnClickListener { onClickListener.onClick(notification) }
    }

    class DiffUtil : androidx.recyclerview.widget.DiffUtil.ItemCallback<Invite>() {
        override fun areItemsTheSame(oldItem: Invite, newItem: Invite): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Invite, newItem: Invite): Boolean {
            return oldItem == newItem
        }
    }
}
