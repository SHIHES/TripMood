package com.shihs.tripmood.home

import android.os.Bundle
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

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var viewModel: HomeViewModel
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)


        val recyclerView = binding.planRV
        val adapter = MyPlanAdapter(MyPlanAdapter.OnItemClickListener{
            viewModel.displayPlanDetails(it)
        })

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val mockList = mutableListOf<Plan>()
        val data1 = Plan(title = "宜蘭", startDate = 123L, endDate = 456L)


        adapter.submitList(mockList)


        viewModel.navigateToSelectedProperty.observe(viewLifecycleOwner){
            it?.let {
                findNavController().navigate(MobileNavigationDirections.actionGlobalMyPlanFragment(it))
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}