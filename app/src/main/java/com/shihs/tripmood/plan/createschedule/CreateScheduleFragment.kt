package com.shihs.tripmood.plan.createschedule

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import app.appworks.school.publisher.ext.getVmFactory
import com.shihs.tripmood.MainActivity
import com.shihs.tripmood.R
import com.shihs.tripmood.databinding.FragmentScheduleCreateBinding
import com.shihs.tripmood.dataclass.Schedule
import com.shihs.tripmood.dataclass.Location
import com.shihs.tripmood.util.ReminderManager
import java.text.SimpleDateFormat
import java.util.*

class CreateScheduleFragment : Fragment() {

    lateinit var binding: FragmentScheduleCreateBinding

    private val viewModel by activityViewModels <CreateScheduleViewModel> { getVmFactory(
        CreateScheduleFragmentArgs.fromBundle(requireArguments()).myPlan,
        CreateScheduleFragmentArgs.fromBundle(requireArguments()).selectedSchedule,
        CreateScheduleFragmentArgs.fromBundle(requireArguments()).selectedPosition
    ) }

    val arg: CreateScheduleFragmentArgs by navArgs()

    private var locationResult: Location? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentScheduleCreateBinding.inflate(inflater, container, false)


        val item = listOf<String>("美食","住宿", "交通", "逛街", "景點")
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.item_schedule_catalog_list, item)

        (binding.catalogEditText as? AutoCompleteTextView)?.setAdapter(arrayAdapter)




        val argPosition = arg.selectedPosition.plus(1)
        val argSchedule = arg.selectedSchedule
        var argLocation = arg.selectedSchedule.let { it?.location }



        binding.scheduleDayTv.text = "Day $argPosition"


        val fmt = SimpleDateFormat("yyyy.MM.dd")

        binding.scheduleDateTv.text = fmt.format(argSchedule?.time)

        setupBtn()

        setFragmentResultListener("keyForRequest"){ requestKey, bundle ->
            if (bundle.get("bundleKey") == null){
                Toast.makeText(requireContext(), "沒選擇任何景點", Toast.LENGTH_SHORT)

            } else{
                locationResult = bundle.get("bundleKey") as Location?
                Log.d("SS", "setFragmentResultListener result$locationResult")
            }
        }

        (requireActivity() as MainActivity).hideActionBar()

        return binding.root
    }

    private fun NotificationSwitch(time: Long){
        if(!binding.notificationSwitch.isChecked){
            return
            } else{
                ReminderManager.startReminder(requireContext(), time, 123)
            }

    }


    private fun setupBtn(){

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
            val cost = binding.costEditText.text.toString()
            val fmt = SimpleDateFormat("HH:mm")
            val catalog = binding.catalogEditText.toString()
            val notification = binding.notificationSwitch.isChecked
            val schedultTime = fmt.parse(binding.scheduleTimeTv.text.toString())?.time
            val postTime = arg.selectedSchedule?.time?.let { it -> schedultTime?.plus(it) }

            if (postTime != null) {
                NotificationSwitch(postTime)
            }

            Log.d("QAQ", "schedultTime$schedultTime")
            viewModel.postNewSchedule(Schedule(
                time = postTime,
                title = title,
                note = content,
                location = locationResult,
                cost = cost,
                notification = notification,
                catalog = catalog))


            findNavController().navigateUp()
        }

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.addLocationButton.setOnClickListener {
            findNavController().navigate(CreateScheduleFragmentDirections
                .actionCreateScheduleFragmentToMapFragment(arg.selectedSchedule))
        }

        binding.addPictureButton.setOnClickListener {

        }

    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).hideActionBar()
    }

    override fun onDestroy() {
        super.onDestroy()
        (requireActivity() as MainActivity).showActionBar()
    }



}