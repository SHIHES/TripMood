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
import com.shihs.tripmood.util.HomePlanFilter
import com.shihs.tripmood.home.adapter.PlanAdapter

class ChildFragment(private val homePlanType: HomePlanFilter) : Fragment() {

    lateinit var binding: FragmentPlanChildViewpagerBinding

    private val viewModel by viewModels<ChildHomeViewModel> { getVmFactory(homePlanType) }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlanChildViewpagerBinding.inflate(inflater, container, false)

        Log.d("SS","ChildFragment $homePlanType")


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

        viewModel.livePlans.observe(viewLifecycleOwner) {it?.let {
            viewModel.planSorter(homePlanType)
            viewModel.updatePlanStatus(it)
            adapter.notifyDataSetChanged()
        }
        }

        viewModel.viewpagerPlans.observe(viewLifecycleOwner){it?.let {
            adapter.submitList(it)
            adapter.notifyDataSetChanged()
        } }

        viewModel.inviteUser.observe(viewLifecycleOwner) {
            it?.let {
                Log.d("QAQ", "inviteUserID$it")
                viewModel.inviteFriend(it)
            }
        }




        return binding.root
    }
}