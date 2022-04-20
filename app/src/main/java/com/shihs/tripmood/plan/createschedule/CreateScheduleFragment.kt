package com.shihs.tripmood.plan.createschedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import app.appworks.school.publisher.ext.getVmFactory
import com.shihs.tripmood.databinding.FragmentScheduleCreateBinding
import com.shihs.tripmood.dataclass.Schedule
import com.shihs.tripmood.plan.MyPlanFragmentArgs
import com.shihs.tripmood.plan.MyPlanViewModel

class CreateScheduleFragment : Fragment() {

    lateinit var binding: FragmentScheduleCreateBinding

    private val viewModel by viewModels <CreateScheduleViewModel> { getVmFactory(CreateScheduleFragmentArgs.fromBundle(requireArguments()).myPlan) }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentScheduleCreateBinding.inflate(inflater, container, false)


        setupBtn()




        return binding.root
    }

    private fun setupBtn(){

        binding.addScheduleBtn.setOnClickListener {
            val time = binding.timeEditText.text
            val title = binding.titleEditText.text.toString()
            val content = binding.contentEditText.text.toString()
            viewModel.postNewSchedule(Schedule(time = 1650575600000, title = title, note = content))
        }

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.addLocationButton.setOnClickListener {

        }

        binding.addPictureButton.setOnClickListener {

        }

    }

}