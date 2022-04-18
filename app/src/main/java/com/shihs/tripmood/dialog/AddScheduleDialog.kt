package com.shihs.tripmood.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.shihs.tripmood.databinding.DialogAddScheduleBinding
import com.shihs.tripmood.dataclass.Event
import com.shihs.tripmood.plan.MyPlanViewModel

class AddScheduleDialog : DialogFragment() {

    lateinit var binding: DialogAddScheduleBinding
    lateinit var viewModel: MyPlanViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DialogAddScheduleBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(requireActivity()).get(MyPlanViewModel::class.java)

        setupBtn()

        return binding.root
    }

    fun setupBtn(){
        binding.okBtn.setOnClickListener {
            createActivity()
        }
    }


    fun createActivity(){
        val title = binding.titleET.text.toString()
        val content = binding.contentET.text.toString()
        val time = binding.timeET.text.toString()

        val activity = Event(title = title, time = time, note = content)

        viewModel.getActivity(activity)

    }
}