package com.shihs.tripmood.plan

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.type.DateTime
import com.shihs.tripmood.databinding.FragmentPlanBinding
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

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


        val recyclerView = binding.daysRv
        val adapter = DateAdapter()


        planDayCalculator()

        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adapter



        var num = 6
        val list = mutableListOf<Int>()
        while (num < 10){
            list.add(num)
            num += 1
            Log.d("SS1", "$list")
            Log.d("SS1", "$num")
        }
        adapter.submitList(daysList)

        return binding.root
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