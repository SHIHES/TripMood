package com.shihs.tripmood.plan

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.shihs.tripmood.databinding.FragmentPlanBinding
import com.shihs.tripmood.dataclass.Schedule
import com.shihs.tripmood.plan.adapter.DateAdapter
import com.shihs.tripmood.plan.adapter.ScheduleAdapter
import java.lang.Exception

class MyPlanFragment : Fragment() {

    lateinit var binding: FragmentPlanBinding

    val arg: MyPlanFragmentArgs by navArgs()

    val daysList = arrayListOf<Long?>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentPlanBinding.inflate(inflater, container, false)

        planDayCalculator()

        setUpRv()

        return binding.root
    }

    fun setUpRv(){

        val recyclerView = binding.daysRv
        val dayAdapter = DateAdapter()

        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = dayAdapter

        dayAdapter.submitList(daysList)


        val scheduleRv = binding.scheduleRv
        val scheduleAdapter = ScheduleAdapter()
        scheduleRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        scheduleRv.adapter = scheduleAdapter

        val mockList = mutableListOf<Schedule>()
        val mock1 = Schedule("02:00","測試","測測")
        val mock2 = Schedule("02:00","測試","測測")
        val mock3 = Schedule("02:00","測試","測測")
        val mock4 = Schedule("02:00","測試","測測")
        mockList.add(mock1)
        mockList.add(mock2)
        mockList.add(mock3)
        mockList.add(mock4)
        scheduleAdapter.submitList(mockList)
    }

    fun planDayCalculator() {
        val myPlan = arg.myPlan
        Log.d("SS", "myPlan $myPlan")
        var start = myPlan?.startDate
        val end = myPlan?.endDate

        try {
            if (start != null && end != null) {
                while (start <= end) {
                    daysList.add(start)
                    Log.d("SS2", "${daysList}")
                    Log.d("SS1", "$start")
                    start += 86400000

                }
            }
            Log.d("SS2", "${daysList}")
        } catch (e: Exception){
            Log.d("SS", "planDayCalculator Exception $e")
        }

    }

}