package com.shihs.tripmood.plan

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.shihs.tripmood.MobileNavigationDirections
import com.shihs.tripmood.databinding.FragmentPlanBinding
import com.shihs.tripmood.dataclass.Schedule
import com.shihs.tripmood.plan.adapter.ScheduleAdapter
import com.shihs.tripmood.plan.adapter.EventAdapter
import java.lang.Exception

class MyPlanFragment : Fragment() {

    lateinit var binding: FragmentPlanBinding
    lateinit var viewModel: MyPlanViewModel

    val arg: MyPlanFragmentArgs by navArgs()

    val daysList = mutableListOf<Schedule?>()

    var selectedPosition = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        daysList[selectedPosition]?.events.add()
        binding = FragmentPlanBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(requireActivity()).get(MyPlanViewModel::class.java)

        planDayCalculator()

        setUpRv()
        setUpBtn()

        return binding.root
    }

    fun setUpRv(){
        val eventAdapter = EventAdapter()
        val recyclerView = binding.daysRv

        val scheduleAdapter = ScheduleAdapter(ScheduleAdapter.OnClickListener{
            eventAdapter.submitList(it.events)
        }, viewModel )

        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = scheduleAdapter

        scheduleAdapter.submitList(daysList)


        val scheduleRv = binding.scheduleRv

        scheduleRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        scheduleRv.adapter = eventAdapter

    }



    fun planDayCalculator() {
        val myPlan = arg.myPlan
        Log.d("SS", "myPlan $myPlan")
        var start = myPlan?.startDate
        val end = myPlan?.endDate

        try {
            if (start != null && end != null) {
                while (start <= end) {

                    daysList.add(Schedule(date = start))

                    start += 86400000

                }
            }
            Log.d("SS2", "${daysList}")
        } catch (e: Exception){
            Log.d("SS", "planDayCalculator Exception $e")
        }

    }

    fun setUpBtn(){
        binding.addActivityBtn.setOnClickListener {
            findNavController().navigate(MobileNavigationDirections.actionGlobalAddScheduleDialog())
        }
    }

}