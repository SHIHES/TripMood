package com.shihs.tripmood.home.childpage

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.shihs.tripmood.databinding.FragmentPlanChildViewpagerBinding
import com.shihs.tripmood.ext.getVmFactory
import com.shihs.tripmood.home.HomeFragmentDirections
import com.shihs.tripmood.home.adapter.PlanAdapter
import com.shihs.tripmood.util.DetailPageFilter
import com.shihs.tripmood.util.HomePlanFilter

class ChildFragment(private val homePlanType: HomePlanFilter) : Fragment() {

    lateinit var binding: FragmentPlanChildViewpagerBinding

    private val viewModel by viewModels<ChildHomeViewModel> { getVmFactory(homePlanType) }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlanChildViewpagerBinding.inflate(inflater, container, false)

        val recyclerPlan = binding.planRV
        val adapter = PlanAdapter(
            PlanAdapter.OnClickListener {
                viewModel.navigateToDetail(it)
            },
            viewModel
        )

        recyclerPlan.adapter = adapter
        recyclerPlan.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )

        viewModel.selectedPlan.observe(viewLifecycleOwner) {
            it?.let {
                if (HomePlanFilter.INDIVIDUAL.value == homePlanType.value) {
                    findNavController().navigate(
                        HomeFragmentDirections.actionGlobalMyPlanFragment(
                            DetailPageFilter.FROM_MYPLAN_SINGLE.navigateFrom,
                            it
                        )
                    )
                    viewModel.onPlanNavigated()
                }
                if (HomePlanFilter.COWORK.value == homePlanType.value) {
                    findNavController().navigate(
                        HomeFragmentDirections.actionGlobalMyPlanFragment(
                            DetailPageFilter.FROM_MYPLAN_COWORK.navigateFrom,
                            it
                        )
                    )
                    viewModel.onPlanNavigated()
                }
            }
        }

        viewModel.livePlans.observe(viewLifecycleOwner) {
            it?.let {
                viewModel.planSorter(homePlanType)
                viewModel.updatePlanStatus(it)
                adapter.notifyDataSetChanged()
            }
        }

        viewModel.liveCoworkingPlans.observe(viewLifecycleOwner) {
            it?.let {
                viewModel.planSorter(homePlanType)
                viewModel.updatePlanStatus(it)
                viewModel.getAllCoworkerInfo(it)
                adapter.notifyDataSetChanged()
            }
        }

        viewModel.viewpagerPlans.observe(viewLifecycleOwner) {
            it?.let {
                if (it.isNotEmpty()) {
                    binding.earthAnimation.visibility = View.GONE
                    binding.noPlanHint.visibility = View.GONE
                } else {
                    binding.earthAnimation.visibility = View.VISIBLE
                    binding.noPlanHint.visibility = View.VISIBLE
                }
                adapter.submitList(it)
                adapter.notifyDataSetChanged()
            }
        }

        viewModel.inviteUser.observe(viewLifecycleOwner) {
            if (it == null){
                Toast.makeText(requireContext(),"Cannot find this user", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.inviteFriend(it)
            }
        }

        viewModel.coworkingUser.observe(viewLifecycleOwner) {
            it?.let {
                viewModel.saveCoworkingUserInfo(it)
                adapter.notifyDataSetChanged()
            }
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.realUserDataList.clear()
    }

    override fun onStop() {
        super.onStop()
        viewModel.realUserDataList.clear()
    }
}
