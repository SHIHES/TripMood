package com.shihs.tripmood.plan

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
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
import com.shihs.tripmood.util.DetailPageFilter
import com.shihs.tripmood.util.ItemTouchHelperCallback
import com.shihs.tripmood.util.MapViewType

class MyPlanFragment : Fragment() {

    lateinit var binding: FragmentPlanBinding

    private val viewModel by viewModels <MyPlanViewModel> { getVmFactory(MyPlanFragmentArgs.fromBundle(requireArguments()).myPlan) }

    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_open_anim) }
    private val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_close_anim) }
    private val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(requireContext(), R.anim.from_bottom_anim) }
    private val toBottom: Animation by lazy { AnimationUtils.loadAnimation(requireContext(), R.anim.to_bottom_anim) }

    private var fabClicked = false

    private var position = 0

    private val args: MyPlanFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentPlanBinding.inflate(inflater, container, false)

        val recyclerPlanDays = binding.daysRv

        val scheduleAdapter = ScheduleAdapter(
            ScheduleAdapter.OnClickListener {
//               viewModel.getSelectedSchedule(it)
            },
            viewModel
        )

        recyclerPlanDays.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerPlanDays.adapter = scheduleAdapter

        when (args.navigateFrom) {

            DetailPageFilter.FROM_MYPLAN_COWORK.navigateFrom -> {
                binding.chatBtn.visibility = View.VISIBLE
                binding.mapWholeSchedule.visibility = View.VISIBLE
                binding.friendsLocation.visibility = View.VISIBLE
            }

            DetailPageFilter.FROM_OTHERS.navigateFrom -> {
                binding.chatBtn.visibility = View.INVISIBLE
                binding.mapWholeSchedule.visibility = View.VISIBLE
                binding.friendsLocation.visibility = View.INVISIBLE
            }

            DetailPageFilter.FROM_MYPLAN_SINGLE.navigateFrom -> {
                binding.chatBtn.visibility = View.INVISIBLE
                binding.mapWholeSchedule.visibility = View.VISIBLE
                binding.friendsLocation.visibility = View.INVISIBLE
            }
        }

        val recyclerEvents = binding.scheduleRv
        val eventAdapter = EventAdapter(
            EventAdapter.OnClickListener {
                viewModel.navigationToDetail(it)
            },
            viewModel
        )

        recyclerEvents.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        recyclerEvents.adapter = eventAdapter

        val callback = ItemTouchHelperCallback(eventAdapter, requireContext())

        val itemTouchHelper = ItemTouchHelper(callback)

        itemTouchHelper.attachToRecyclerView(recyclerEvents)

        viewModel.liveSchedules.observe(viewLifecycleOwner) {
            it?.let {
                viewModel.getPositionAndDate(position).let {
                    viewModel.findTimeRangeSchedule()
                }
            }
        }

        viewModel.schedules.observe(viewLifecycleOwner) {
            it?.let {
                scheduleAdapter.submitList(it)
            }
        }

        viewModel.adapterPosition.observe(viewLifecycleOwner) {
            Log.d("SS", "adapterPosition.observe$it")
            position = it
            viewModel.getPositionAndDate(it).let {
                viewModel.findTimeRangeSchedule()
            }
            scheduleAdapter.notifyDataSetChanged()
        }

        viewModel.navigationToDetail.observe(viewLifecycleOwner) {
            it?.let {
                findNavController().navigate(MobileNavigationDirections.actionGlobalDetailFragment(it))
                viewModel.navigationToDetailEnd()
            }
        }

        viewModel.dayOfSchedule.observe(viewLifecycleOwner) {
            it?.let {

                Log.d("QAQ", "dayOfSchedule $it")
                if (it.isEmpty()) {
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

    fun setView() {

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

    fun setUpBtn() {
        binding.addActivityBtn.setOnClickListener {
            onAddButtonClicked()
        }

        binding.addMode1Btn.setOnClickListener {
            findNavController().navigate(
                MobileNavigationDirections.actionGlobalCreateScheduleFragment(
                    MyPlanFragmentArgs.fromBundle(requireArguments()).myPlan,
                    viewModel.positionControlSchedule.value,
                    position
                )
            )
        }

        binding.addMode2Btn.setOnClickListener {
            findNavController().navigate(
                MobileNavigationDirections.actionGlobalMyGPSFragment(
                    MyPlanFragmentArgs.fromBundle(requireArguments()).myPlan,
                    viewModel.positionControlSchedule.value,
                    position
                )
            )
        }

        binding.friendsLocation.setOnClickListener {
            findNavController().navigate(
                MobileNavigationDirections.actionGlobalShowAllLocationFragment(
                    MyPlanFragmentArgs.fromBundle(requireArguments()).myPlan, MapViewType.MAP_COWORKLOCATION.value
                )
            )
        }

        binding.mapWholeSchedule.setOnClickListener {
            findNavController().navigate(
                MobileNavigationDirections.actionGlobalShowAllLocationFragment(
                    MyPlanFragmentArgs.fromBundle(requireArguments()).myPlan, MapViewType.MAP_SHOWALLLOCATION.value
                )
            )
        }

        binding.chatBtn.setOnClickListener {
            findNavController().navigate(
                MobileNavigationDirections.actionGlobalChatFragment(
                    MyPlanFragmentArgs.fromBundle(requireArguments()).myPlan
                )
            )
        }
    }

    private fun onAddButtonClicked() {
        setVisibility(fabClicked)
        setAnimation(fabClicked)
        setClickable(fabClicked)
        fabClicked = !fabClicked
    }

    private fun setAnimation(clicked: Boolean) {
        if (!clicked) {
            binding.addMode1Btn.visibility = View.VISIBLE
            binding.addMode2Btn.visibility = View.VISIBLE
        } else {
            binding.addMode1Btn.visibility = View.INVISIBLE
            binding.addMode2Btn.visibility = View.INVISIBLE
        }
    }

    private fun setVisibility(clicked: Boolean) {
        if (!clicked) {
            binding.addMode1Btn.startAnimation(fromBottom)
            binding.addMode2Btn.startAnimation(fromBottom)
            binding.addActivityBtn.startAnimation(rotateOpen)
        } else {
            binding.addMode1Btn.startAnimation(toBottom)
            binding.addMode2Btn.startAnimation(toBottom)
            binding.addActivityBtn.startAnimation(rotateClose)
        }
    }

    private fun setClickable(clicked: Boolean) {
        if (!clicked) {
            binding.addMode1Btn.isClickable = true
            binding.addMode2Btn.isClickable = true
        } else {
            binding.addMode1Btn.isClickable = false
            binding.addMode2Btn.isClickable = false
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
