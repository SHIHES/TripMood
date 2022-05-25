package com.shihs.tripmood.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.shihs.tripmood.databinding.FragmentSearchBinding
import com.shihs.tripmood.ext.getVmFactory
import com.shihs.tripmood.home.HomeFragmentDirections
import com.shihs.tripmood.search.adapter.SearchPlanAdapter
import com.shihs.tripmood.util.DetailPageFilter

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

        val adapter = SearchPlanAdapter(
            SearchPlanAdapter.OnClickListener {
                viewModel.navigateToDetail(it)
            },
            viewModel
        )

        val recyclerPlan = binding.planRecyclerView
        recyclerPlan.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerPlan.adapter = adapter

        val searchView = binding.searchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.filterSearch(newText)
                return false
            }
        })
        viewModel.searchPlans.observe(viewLifecycleOwner) {
            it?.let {
                adapter.submitList(it)
                adapter.notifyDataSetChanged()
            }
        }

        viewModel.publicPlans.observe(
            viewLifecycleOwner,
            Observer {
                it?.let {
                    Log.d("QAQ", "plans $it")
                    adapter.submitList(it)
                    adapter.notifyDataSetChanged()
                }
            }
        )

        viewModel.selectedPlan.observe(
            viewLifecycleOwner,
            Observer {
                it?.let {
                    findNavController().navigate(HomeFragmentDirections.actionGlobalMyPlanFragment(DetailPageFilter.FROM_OTHERS.navigateFrom, it))
                    viewModel.onPlanNavigated()
                }
            }
        )

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
