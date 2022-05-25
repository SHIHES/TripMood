package com.shihs.tripmood.user.child

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.shihs.tripmood.databinding.FragmetUserPlanChildViewpagerBinding
import com.shihs.tripmood.ext.getVmFactory
import com.shihs.tripmood.home.HomeFragmentDirections
import com.shihs.tripmood.user.adapter.MemoryPlanAdapter
import com.shihs.tripmood.util.DetailPageFilter
import com.shihs.tripmood.util.UserPlanFilter

class UserChildFragment(private val userPlanFilter: UserPlanFilter) : Fragment() {

    lateinit var binding: FragmetUserPlanChildViewpagerBinding

    private val viewModel by viewModels<UserChildViewModel> { getVmFactory(userPlanFilter) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmetUserPlanChildViewpagerBinding.inflate(inflater, container, false)

        val adapter = MemoryPlanAdapter(
            MemoryPlanAdapter.OnClickListener {
                viewModel.navigateToDetail(it)
            }
        )

        val recyclerViewPlan = binding.userPlanRv
        recyclerViewPlan.adapter = adapter
        recyclerViewPlan.layoutManager = GridLayoutManager(
            context,
            2,
            GridLayoutManager.VERTICAL,
            false
        )

        viewModel.selectedPlan.observe(viewLifecycleOwner) {
            it?.let {
                findNavController().navigate(
                    HomeFragmentDirections.actionGlobalMyPlanFragment(
                        DetailPageFilter.FROM_MYPLAN_COWORK.navigateFrom,
                        it
                    )
                )
                viewModel.onPlanNavigated()
            }
        }

        viewModel.livePlans.observe(viewLifecycleOwner) {
            viewModel.planSorter(userPlanFilter)
        }

        viewModel.viewpagerPlans.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            adapter.notifyDataSetChanged()
        }

        return binding.root
    }
}
