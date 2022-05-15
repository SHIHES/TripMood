package com.shihs.tripmood.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.shihs.tripmood.chat.adapter.ChatAdapter
import com.shihs.tripmood.databinding.FragmentChatDetailBinding
import com.shihs.tripmood.dataclass.Chat
import com.shihs.tripmood.dataclass.User
import com.shihs.tripmood.ext.getVmFactory
import com.shihs.tripmood.util.UserManager
import java.util.*


class ChatFragment : Fragment() {

    lateinit var binding: FragmentChatDetailBinding

    private val viewModel by viewModels <ChatViewModel> { getVmFactory(ChatFragmentArgs.fromBundle(requireArguments()).myPlan) }

    private val arg: ChatFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentChatDetailBinding.inflate(inflater, container, false)

        val adapter = ChatAdapter()
        val recyclerViewChats = binding.recyclerChat
        recyclerViewChats.adapter = adapter
        recyclerViewChats.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)


        viewModel.chat.observe(viewLifecycleOwner){it?.let {
            adapter.submitList(it)
        }}


        setBtn()

        return binding.root
    }

    fun setBtn(){

        binding.buttonGchatSend.setOnClickListener {
            val time = Calendar.getInstance(Locale.TAIWAN).timeInMillis
            var msg = binding.editGchatMessage.text.toString()
            val user = User(name = UserManager.userName,
                email = "",
                image = UserManager.userPhotoUrl,
                uid = UserManager.userUID)

            val chat = Chat(
                createdTime = time,
                speaker = user,
                msg = msg ,
                planID = arg.myPlan?.id,

            )


            viewModel.postChats(chat)
        }
    }



}