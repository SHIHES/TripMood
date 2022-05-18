package com.shihs.tripmood.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.shihs.tripmood.databinding.FragmentFavoriteBinding
import com.shihs.tripmood.ext.getVmFactory
import com.shihs.tripmood.favorite.adapter.FavoritePlanAdapter
import com.shihs.tripmood.home.HomeFragmentDirections
import com.shihs.tripmood.search.adapter.SearchPlanAdapter
import com.shihs.tripmood.util.DetailPageFilter

class FavoriteFragment : Fragment() {

    lateinit var binding: FragmentFavoriteBinding

    private val viewModel by viewModels<FavoriteViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentFavoriteBinding.inflate(inflater, container, false)

        val adapter = FavoritePlanAdapter(FavoritePlanAdapter.OnClickListener{
            viewModel.navigateToDetail(it)
        }, viewModel)

        val recyclerview = binding.favoritePlanRv

        recyclerview.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerview.adapter = adapter


        viewModel.favoritePlans.observe(viewLifecycleOwner){it?.let {
            if(it.isNullOrEmpty()){
                binding.noplanAnimation.visibility = View.VISIBLE
                binding.noPlanHints.visibility = View.VISIBLE
            } else {
                adapter.submitList(it)
                binding.noplanAnimation.visibility = View.INVISIBLE
                binding.noPlanHints.visibility = View.INVISIBLE
            }

        }}

        viewModel.selectedPlan.observe(viewLifecycleOwner){
            it?.let {
                findNavController().navigate(HomeFragmentDirections.actionGlobalMyPlanFragment(DetailPageFilter.FROM_OTHERS.navigateFrom, it))
                viewModel.onPlanNavigated()
            }
        }



        return binding.root
    }
}