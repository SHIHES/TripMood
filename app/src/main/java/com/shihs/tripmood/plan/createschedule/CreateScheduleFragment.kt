package com.shihs.tripmood.plan.createschedule

<<<<<<< HEAD
import android.os.Bundle
=======
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
>>>>>>> develop
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
<<<<<<< HEAD
import app.appworks.school.publisher.ext.getVmFactory
import com.shihs.tripmood.databinding.FragmentScheduleCreateBinding
import com.shihs.tripmood.dataclass.Schedule
import com.shihs.tripmood.plan.MyPlanFragmentArgs
import com.shihs.tripmood.plan.MyPlanViewModel
=======
import androidx.navigation.fragment.navArgs
import app.appworks.school.publisher.ext.getVmFactory
import com.shihs.tripmood.databinding.FragmentScheduleCreateBinding
import com.shihs.tripmood.dataclass.Schedule
import com.shihs.tripmood.plan.MyPlanViewModel
import java.text.SimpleDateFormat
import java.util.*
>>>>>>> develop

class CreateScheduleFragment : Fragment() {

    lateinit var binding: FragmentScheduleCreateBinding

<<<<<<< HEAD
    private val viewModel by viewModels <CreateScheduleViewModel> { getVmFactory(CreateScheduleFragmentArgs.fromBundle(requireArguments()).myPlan) }
=======
    private val viewModel by viewModels <CreateScheduleViewModel> { getVmFactory(
        CreateScheduleFragmentArgs.fromBundle(requireArguments()).myPlan,
        CreateScheduleFragmentArgs.fromBundle(requireArguments()).selectedSchedule,
        CreateScheduleFragmentArgs.fromBundle(requireArguments()).selectedPosition
    ) }

    val arg: CreateScheduleFragmentArgs by navArgs()
>>>>>>> develop


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentScheduleCreateBinding.inflate(inflater, container, false)

<<<<<<< HEAD

        setupBtn()




=======
        val position = arg.selectedPosition

        binding.scheduleDayTv.text = position.toString()


        val schedule = arg.selectedSchedule

        setupBtn()

>>>>>>> develop
        return binding.root
    }

    private fun setupBtn(){

<<<<<<< HEAD
        binding.addScheduleBtn.setOnClickListener {
            val time = binding.timeEditText.text
            val title = binding.titleEditText.text.toString()
            val content = binding.contentEditText.text.toString()
=======
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)




        binding.scheduleTimeTv.setOnClickListener {
            TimePickerDialog(context, {_, hour, minute  ->
                binding.scheduleTimeTv.text = "$hour:$minute"
            }, hour, minute, true).show()



        }

        binding.addScheduleBtn.setOnClickListener {
            val title = binding.titleEditText.text.toString()
            val content = binding.contentEditText.text.toString()
            val fmt = SimpleDateFormat("HH:mm")
            val schedultTime = fmt.parse(binding.scheduleTimeTv.text.toString())?.time

            Log.d("QAQ", "schedultTime$schedultTime")
>>>>>>> develop
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