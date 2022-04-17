package com.shihs.tripmood.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.shihs.tripmood.MobileNavigationDirections
import com.shihs.tripmood.databinding.FragmentHomeBinding
import com.shihs.tripmood.dataclass.Plan
import com.shihs.tripmood.dataclass.Schedule
import com.shihs.tripmood.util.Logger

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val recyclerView = binding.planRV
        val adapter = MyPlanAdapter( MyPlanAdapter.OnClickListener{
            viewModel.navigateToDetail(it)
            Log.d("SS","MyPlanAdapter.OnClickListener$it")
        })

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)


        // 假資料
        val mockList = mutableListOf<Plan>()
        val mockSchedule = mutableListOf<Schedule>()
        val schedule = Schedule(time = "12:00", title = "南投", note = "測試")

        for(i in 1..3){
            mockSchedule.add(schedule)
        }

        val data1 = Plan(title = "宜蘭", startDate = 12300000L, endDate = 45600000L, id = "123" , schedule = mockSchedule  )

        for (i in 1.. 3){
            mockList.add(data1)
        }
        adapter.submitList(mockList)

        viewModel.selectedPlan.observe(viewLifecycleOwner){
            findNavController().navigate(MobileNavigationDirections.actionGlobalMyPlanFragment(it))
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}