package com.shihs.tripmood.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import app.appworks.school.publisher.ext.getVmFactory
import com.shihs.tripmood.MobileNavigationDirections
import com.shihs.tripmood.databinding.FragmentHomeBinding

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
        val adapter = MyPlanAdapter( MyPlanAdapter.OnClickListener{
            viewModel.navigateToDetail(it)
        })

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)


        viewModel.selectedPlan.observe(viewLifecycleOwner){
            findNavController().navigate(MobileNavigationDirections.actionGlobalMyPlanFragment(it))
        }

        viewModel.livePlans.observe(viewLifecycleOwner){
            adapter.submitList(it)
        }


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}