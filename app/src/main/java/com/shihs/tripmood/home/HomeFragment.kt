package com.shihs.tripmood.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.shihs.tripmood.R
import com.shihs.tripmood.databinding.FragmentHomeBinding
import com.shihs.tripmood.dataclass.Plan

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
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val recyclerView = binding.planRV
        val adapter = MyPlanAdapter()

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val mockList = mutableListOf<Plan>()
        val data1 = Plan(title = "宜蘭", startDate = "123", endDate = "456")
        val data2 = Plan(title = "花蓮", startDate = "123", endDate = "456")
        val data3 = Plan(title = "墾丁", startDate = "123", endDate = "456")
        mockList.add(data1)
        mockList.add(data2)
        mockList.add(data3)

        adapter.submitList(mockList)

        homeViewModel.text.observe(viewLifecycleOwner) {
        }

        setBtn()




        return binding.root
    }

    private fun setBtn() {
        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_global_planModeDialog)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}