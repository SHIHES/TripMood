package com.shihs.tripmood.home.childpage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import app.appworks.school.publisher.ext.getVmFactory
import com.shihs.tripmood.databinding.FragmentPlanChildViewpagerBinding
import com.shihs.tripmood.home.HomeFragmentDirections
import com.shihs.tripmood.home.PlanFilter
import com.shihs.tripmood.home.adapter.PlanAdapter

class ChildFragment(private val planType: PlanFilter) : Fragment() {

    lateinit var binding: FragmentPlanChildViewpagerBinding

    private val viewModel by viewModels<ChildHomeViewModel> { getVmFactory(planType) }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlanChildViewpagerBinding.inflate(inflater, container, false)

        Log.d("SS","ChildFragment $planType")


        val recyclerPlan = binding.planRV

        val adapter = PlanAdapter(PlanAdapter.OnClickListener {
            viewModel.navigateToDetail(it)
        },viewModel)

        recyclerPlan.adapter = adapter
        recyclerPlan.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)


        viewModel.selectedPlan.observe(viewLifecycleOwner) {
            it?.let {
                findNavController().navigate(HomeFragmentDirections.actionGlobalMyPlanFragment(it))
                viewModel.onPlanNavigated()
            }
        }

        viewModel.livePlans.observe(viewLifecycleOwner) {
            viewModel.planSorter(planType)
        }

        viewModel.viewpagerPlans.observe(viewLifecycleOwner){
            adapter.submitList(it)
        }



        return binding.root
    }
}