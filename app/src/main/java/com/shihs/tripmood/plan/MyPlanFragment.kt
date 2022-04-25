package com.shihs.tripmood.plan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import app.appworks.school.publisher.ext.getVmFactory
import com.shihs.tripmood.MainActivity
import com.shihs.tripmood.MobileNavigationDirections
import com.shihs.tripmood.databinding.FragmentPlanBinding
import com.shihs.tripmood.dataclass.Schedule
import com.shihs.tripmood.plan.adapter.EventAdapter
import com.shihs.tripmood.plan.adapter.ScheduleAdapter

class MyPlanFragment : Fragment() {

    lateinit var binding: FragmentPlanBinding

    private val viewModel by viewModels <MyPlanViewModel> { getVmFactory(MyPlanFragmentArgs.fromBundle(requireArguments()).myPlan) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentPlanBinding.inflate(inflater, container, false)

        val recyclerPlanDays = binding.daysRv

        val scheduleAdapter = ScheduleAdapter(ScheduleAdapter.OnClickListener{
               viewModel.getSelectedSchedule(it)
        }, viewModel )

        recyclerPlanDays.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerPlanDays.adapter = scheduleAdapter


        val recyclerEvents = binding.scheduleRv
        val eventAdapter = EventAdapter()

        recyclerEvents.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        recyclerEvents.adapter = eventAdapter

        viewModel.schedules.observe(viewLifecycleOwner){
            scheduleAdapter.submitList(it)
        }

        viewModel.selectedSchedule.observe(viewLifecycleOwner){
            viewModel.findTimeRangeSchedule()

        }

        viewModel.dayOfSchedule.observe(viewLifecycleOwner){
            it?.let{
                eventAdapter.submitList(it)
                binding.hintTv.visibility = View.GONE
            }
        }


        setUpBtn()

        (requireActivity() as MainActivity).hideActionBar()

        return binding.root
    }


    fun setUpBtn(){
        binding.addActivityBtn.setOnClickListener {
            findNavController().navigate(
                MobileNavigationDirections.actionGlobalCreateScheduleFragment(
                    MyPlanFragmentArgs.fromBundle(requireArguments()).myPlan,
                    viewModel.clickSchedule,
                    viewModel.adapterPosition
                )
            )
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