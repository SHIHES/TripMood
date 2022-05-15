package com.shihs.tripmood.plan

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.shihs.tripmood.MainActivity
import com.shihs.tripmood.MobileNavigationDirections
import com.shihs.tripmood.R
import com.shihs.tripmood.databinding.FragmentPlanBinding
import com.shihs.tripmood.ext.getVmFactory
import com.shihs.tripmood.ext.toDisplayDateFormat
import com.shihs.tripmood.plan.adapter.EventAdapter
import com.shihs.tripmood.plan.adapter.ScheduleAdapter
import com.shihs.tripmood.util.ItemTouchHelperCallback
import com.shihs.tripmood.util.MapViewType


class MyPlanFragment : Fragment() {

    lateinit var binding: FragmentPlanBinding

    private val viewModel by viewModels <MyPlanViewModel> { getVmFactory(MyPlanFragmentArgs.fromBundle(requireArguments()).myPlan) }

    private var position = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentPlanBinding.inflate(inflater, container, false)

        val recyclerPlanDays = binding.daysRv

        val scheduleAdapter = ScheduleAdapter(ScheduleAdapter.OnClickListener{
//               viewModel.getSelectedSchedule(it)
        }, viewModel )

        recyclerPlanDays.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerPlanDays.adapter = scheduleAdapter




        val recyclerEvents = binding.scheduleRv
        val eventAdapter = EventAdapter(EventAdapter.OnClickListener{
            viewModel.navigationToDetail(it)
        }, viewModel)

        recyclerEvents.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        recyclerEvents.adapter = eventAdapter

        val callback = ItemTouchHelperCallback(eventAdapter, requireContext())

        val itemTouchHelper = ItemTouchHelper(callback)

        itemTouchHelper.attachToRecyclerView(recyclerEvents)

        viewModel.liveSchedules.observe(viewLifecycleOwner){it?.let {
            viewModel.getPositionAndDate(position).let {
                viewModel.findTimeRangeSchedule()
            }

        }}


        viewModel.schedules.observe(viewLifecycleOwner){it?.let{
            scheduleAdapter.submitList(it)
        }}


        viewModel.adapterPosition.observe(viewLifecycleOwner){
            Log.d("SS", "adapterPosition.observe${it}")
            position = it
            viewModel.getPositionAndDate(it).let {
                viewModel.findTimeRangeSchedule()
            }
            scheduleAdapter.notifyDataSetChanged()
        }

        viewModel.navigationToDetail.observe(viewLifecycleOwner){it?.let{
            findNavController().navigate(MobileNavigationDirections.actionGlobalDetailFragment(it))
            viewModel.navigationToDetailEnd()
        }}

        viewModel.dayOfSchedule.observe(viewLifecycleOwner){
            it?.let {

                Log.d("QAQ", "dayOfSchedule $it")
                if (it.isEmpty() ){
                    Log.d("QAQ", "dayOfSchedule empty $it")
                    binding.hintAnimation.visibility = View.VISIBLE
                    binding.hintTv.visibility = View.VISIBLE
                    eventAdapter.submitList(it)
                } else {
                    Log.d("QAQ", "dayOfSchedule else$it")
                    eventAdapter.submitList(it)
                    binding.hintAnimation.visibility = View.GONE
                    binding.hintTv.visibility = View.GONE
                }
            }
        }

        setUpBtn()
        setView()

        (requireActivity() as MainActivity).hideToolBar()

        return binding.root
    }

    fun setView(){

        val title = MyPlanFragmentArgs.fromBundle(requireArguments()).myPlan?.title
        val endDate = MyPlanFragmentArgs.fromBundle(requireArguments()).myPlan?.endDate?.toDisplayDateFormat()
        val startDate = MyPlanFragmentArgs.fromBundle(requireArguments()).myPlan?.startDate?.toDisplayDateFormat()
        val image = MyPlanFragmentArgs.fromBundle(requireArguments()).myPlan?.image
        binding.planCollapsingToolbar.title = title
        binding.myPlanDate.text = "$startDate - $endDate"

        Glide.with(requireContext()).load(image).placeholder(R.drawable.placeholder)
            .centerCrop()
            .into(binding.coworkLocationImage)
    }



    fun setUpBtn(){
        binding.addActivityBtn.setOnClickListener {
            findNavController().navigate(
                MobileNavigationDirections.actionGlobalCreateScheduleFragment(
                    MyPlanFragmentArgs.fromBundle(requireArguments()).myPlan,
                    viewModel.positionControlSchedule.value,
                    position
                )
            )
        }

        binding.friendsLocation.setOnClickListener {
            findNavController().navigate(MobileNavigationDirections.actionGlobalShowAllLocationFragment(
                MyPlanFragmentArgs.fromBundle(requireArguments()).myPlan, MapViewType.MAP_COWORKLOCATION.value
            ))
        }

        binding.mapWholeSchedule.setOnClickListener {
            findNavController().navigate(MobileNavigationDirections.actionGlobalShowAllLocationFragment(
                MyPlanFragmentArgs.fromBundle(requireArguments()).myPlan, MapViewType.MAP_SHOWALLLOCATION.value
            ))
        }

        binding.chatBtn.setOnClickListener {
            findNavController().navigate(MobileNavigationDirections.actionGlobalChatFragment(
                MyPlanFragmentArgs.fromBundle(requireArguments()).myPlan
            ))
        }
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).hideToolBar()
    }

    override fun onDestroy() {
        super.onDestroy()
        (requireActivity() as MainActivity).showToolBar()
    }

}