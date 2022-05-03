package com.shihs.tripmood.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import app.appworks.school.publisher.ext.getVmFactory
import com.shihs.tripmood.databinding.FragmentSearchBinding
import com.shihs.tripmood.home.HomeFragmentDirections
import com.shihs.tripmood.home.adapter.PlanAdapter
import com.shihs.tripmood.search.adapter.SearchPlanAdapter

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null

    private val binding get() = _binding!!

    private val viewModel by viewModels <SearchViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        val adapter = SearchPlanAdapter(SearchPlanAdapter.OnClickListener {
            viewModel.navigateToDetail(it)
        }, viewModel)

        val recyclerPlan = binding.planRecyclerView
        recyclerPlan.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerPlan.adapter = adapter



        viewModel.plans.observe(viewLifecycleOwner, Observer {
            it?.let {
                Log.d("QAQ", "plans $it")
                adapter.submitList(it)
            }
        })

        viewModel.selectedPlan.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(HomeFragmentDirections.actionGlobalMyPlanFragment(it))
                viewModel.onPlanNavigated()
            }
        })




        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}