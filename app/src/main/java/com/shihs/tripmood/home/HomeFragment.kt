package com.shihs.tripmood.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.shihs.tripmood.MobileNavigationDirections
import com.shihs.tripmood.databinding.FragmentHomeBinding
import com.shihs.tripmood.dataclass.Plan
import com.shihs.tripmood.ext.getVmFactory
import com.shihs.tripmood.util.Logger

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<HomeViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


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