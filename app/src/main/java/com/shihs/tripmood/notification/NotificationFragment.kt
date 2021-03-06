package com.shihs.tripmood.notification

import android.graphics.Bitmap
import android.os.Bundle
import android.util.LruCache
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.shihs.tripmood.databinding.FragmentNotificationBinding
import com.shihs.tripmood.ext.getVmFactory
import com.shihs.tripmood.notification.adapter.InviteAdapter
import java.util.*

class NotificationFragment : Fragment() {

    lateinit var binding: FragmentNotificationBinding

    private val viewModel by viewModels<NotificationViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotificationBinding.inflate(inflater, container, false)

        val adapter = InviteAdapter(
            InviteAdapter.OnClickListener {
            },
            viewModel
        )

        val recyclerNotification = binding.notificationRV

        recyclerNotification.adapter = adapter
        recyclerNotification.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )

        viewModel.receiveInvites.observe(viewLifecycleOwner) {
            it?.let {
                viewModel.getReplyInvite()
            }
        }

        viewModel.replyInvites.observe(viewLifecycleOwner) {
            it?.let {
                viewModel.addInvites()
            }
        }

        viewModel.allUserInvites.observe(viewLifecycleOwner) {
            it?.let {
                adapter.submitList(it)
            }
        }
        

        return binding.root
    }
}
