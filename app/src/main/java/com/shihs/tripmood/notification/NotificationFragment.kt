package com.shihs.tripmood.notification

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import app.appworks.school.publisher.ext.getVmFactory
import com.shihs.tripmood.databinding.FragmentNotificationBinding
import com.shihs.tripmood.notification.adapter.InviteAdapter

class NotificationFragment : Fragment() {


    lateinit var binding: FragmentNotificationBinding

    private val viewModel by viewModels<NotificationViewModel> { getVmFactory() }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentNotificationBinding.inflate(inflater, container, false)

        val adapter = InviteAdapter(InviteAdapter.OnClickListener{

        },viewModel)

        val recyclerNotification = binding.notificationRV

        recyclerNotification.adapter = adapter
        recyclerNotification.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        viewModel.receiveInvites.observe(viewLifecycleOwner){it?.let {
            viewModel.getReplyInvite()
            Log.d("QAQQQ", "receiveInvites$it")
        }}

        viewModel.replyInvites.observe(viewLifecycleOwner){it?.let{
            viewModel.addInvites()
            Log.d("QAQQQ", "replyInvites$it")
        }}

        viewModel.allUserInvites.observe(viewLifecycleOwner){it?.let{
            Log.d("QAQQQ", "allUserInvites$it")
            adapter.submitList(it)
        }}





        return binding.root
    }
}