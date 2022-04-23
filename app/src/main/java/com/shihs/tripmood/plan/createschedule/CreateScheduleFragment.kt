package com.shihs.tripmood.plan.createschedule

import android.app.TimePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import app.appworks.school.publisher.ext.getVmFactory
import com.shihs.tripmood.MobileNavigationDirections
import com.shihs.tripmood.R
import com.shihs.tripmood.databinding.FragmentScheduleCreateBinding
import com.shihs.tripmood.dataclass.Schedule
import com.shihs.tripmood.dataclass.source.Location
import com.shihs.tripmood.plan.MyPlanViewModel
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


        val argPosition = arg.selectedPosition.plus(1)
        val argSchedule = arg.selectedSchedule
        var argLocation = arg.selectedSchedule.let { it?.location }

        Log.d("SS", "argLocation$argLocation")

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


//            binding.addScheduleBtn.setColorFilter(ContextCompat.getColor(requireContext(),R.color.purple_500))
//            binding.addPictureButton.setColorFilter(ContextCompat.getColor(requireContext(),R.color.purple_500))
//
//            binding.addLocationButton.setColorFilter(ContextCompat.getColor(requireContext(),R.color.purple_500))
//            binding.addLocationButton.setColorFilter(ContextCompat.getColor(requireContext(), R.color.tripMood_blue), android.graphics.PorterDuff.Mode.SRC_IN);
        }

        return binding.root
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
            val fmt = SimpleDateFormat("HH:mm")
            val schedultTime = fmt.parse(binding.scheduleTimeTv.text.toString())?.time
            val postTime = arg.selectedSchedule?.time?.let { it -> schedultTime?.plus(it) }

            Log.d("QAQ", "schedultTime$schedultTime")
            viewModel.postNewSchedule(Schedule(time = postTime, title = title, note = content, location = locationResult ))
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

}