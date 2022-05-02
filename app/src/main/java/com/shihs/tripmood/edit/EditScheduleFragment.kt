package com.shihs.tripmood.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.shihs.tripmood.databinding.FragmentScheduleEditBinding


class EditScheduleFragment: Fragment() {

    lateinit var binding:FragmentScheduleEditBinding

    val arg: EditScheduleFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentScheduleEditBinding.inflate(inflater, container, false)



        setupUI()



        return binding.root
    }


    private fun setupUI(){
        val time = arg.selectedSchedule?.time
        val catalog = arg.selectedSchedule?.catalog
        val title = arg.selectedSchedule?.title
        val address = arg.selectedSchedule?.location?.address
        val cost = arg.selectedSchedule?.cost
        val note = arg.selectedSchedule?.note
        val notificationStatus = arg.selectedSchedule?.notification
        val dayInt = arg.selectedPosition
        val location = arg.selectedSchedule?.location


        binding.catalogEditText.setText(catalog)
        binding.contentEditText.setText(note)
        binding.titleEditText.setText(title)
        binding.costEditText.setText(cost)
        binding.addressEditText.setText(address)
        binding.scheduleDayTv.setText("Day$dayInt")



    }
}