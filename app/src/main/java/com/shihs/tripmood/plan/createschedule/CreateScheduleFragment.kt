package com.shihs.tripmood.plan.createschedule

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
import com.shihs.tripmood.MainActivity
import com.shihs.tripmood.R
import com.shihs.tripmood.databinding.FragmentScheduleCreateBinding
import com.shihs.tripmood.dataclass.Location
import com.shihs.tripmood.dataclass.Schedule
import com.shihs.tripmood.ext.getVmFactory
import java.text.SimpleDateFormat
import java.util.*

class CreateScheduleFragment : Fragment() {

    lateinit var binding: FragmentScheduleCreateBinding

    private val viewModel by activityViewModels<CreateScheduleViewModel> {
        getVmFactory(
            CreateScheduleFragmentArgs.fromBundle(requireArguments()).myPlan,
            CreateScheduleFragmentArgs.fromBundle(requireArguments()).selectedSchedule,
            CreateScheduleFragmentArgs.fromBundle(requireArguments()).selectedPosition
        )
    }

    private val arg: CreateScheduleFragmentArgs by navArgs()

    private var catalog = ""

    private var locationResult: Location? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentScheduleCreateBinding.inflate(inflater, container, false)

        val item = resources.getStringArray(R.array.schedule_catalog)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.item_schedule_catalog_list, item)

        (binding.catalogEditText as? AutoCompleteTextView)?.setAdapter(arrayAdapter)

        binding.catalogEditText.setOnItemClickListener { adapterView, view, position, rowID ->
            catalog = adapterView.getItemAtPosition(position).toString()
        }

        val argPosition = arg.selectedPosition.plus(1)
        val argSchedule = arg.selectedSchedule
        var argLocation = arg.selectedSchedule.let { it?.location }

        binding.scheduleDayTv.text = resources.getString(R.string.schedule_theDay, argPosition)

        val fmt = SimpleDateFormat("yyyy.MM.dd", Locale.TAIWAN)

        binding.scheduleDateTv.text = fmt.format(argSchedule?.time)

        setupBtn()

        (requireActivity() as MainActivity).hideToolBar()
        (requireActivity() as MainActivity).hideBottomNavBar()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setFragmentResultListener("keyForRequest") { requestKey, bundle ->
            if (bundle.get("bundleKey") == null) {
                Toast.makeText(requireContext(), "沒選擇任何景點", Toast.LENGTH_SHORT).show()
            } else {
                locationResult = bundle.get("bundleKey") as Location?

                binding.addressEditText.setText(locationResult?.address)
                Log.d("SS", "setFragmentResultListener result$locationResult")
            }
        }
    }

    private fun setupBtn() {
        val calendar = Calendar.getInstance(Locale.TAIWAN)
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        binding.scheduleTimeTv.setOnClickListener {
            TimePickerDialog(context, { _, hour, minute ->
                binding.scheduleTimeTv.text = "$hour:$minute"
            }, hour, minute, true).show()
            }

            binding.addScheduleBtn.setOnClickListener {
                val title = binding.titleEditText.text.toString()
                val content = binding.contentEditText.text.toString()
                val cost = binding.costEditText.text.toString()
                val fmt = SimpleDateFormat("HH:mm", Locale.TAIWAN)
                val notification = binding.notificationSwitch.isChecked
                val scheduleTime = fmt.parse(binding.scheduleTimeTv.text.toString())?.time
                val postTime = arg.selectedSchedule?.time?.let { scheduleTime?.plus(it) }
                val planId = arg.myPlan?.id

                val postSchedule = Schedule(
                    planID = planId,
                    time = postTime,
                    title = title,
                    note = content,
                    location = locationResult,
                    cost = cost,
                    notification = notification,
                    catalog = catalog,
                    theDay = arg.selectedPosition.plus(1)
                )

                viewModel.postNewSchedule(postSchedule)

                findNavController().navigateUp()
            }

            binding.backButton.setOnClickListener {
                findNavController().navigateUp()
            }

            binding.addressEditText.setOnClickListener {
                findNavController().navigate(
                    CreateScheduleFragmentDirections
                        .actionCreateScheduleFragmentToMapFragment(arg.selectedSchedule)
                )
            }

            binding.addPictureButton.setOnClickListener {
            }
        }

        override fun onResume() {
            super.onResume()
            (requireActivity() as MainActivity).hideToolBar()
            (requireActivity() as MainActivity).hideBottomNavBar()
        }

        override fun onDestroy() {
            super.onDestroy()
            (requireActivity() as MainActivity).showToolBar()
            (requireActivity() as MainActivity).showBottomNavBar()
        }
    }
    