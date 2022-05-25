package com.shihs.tripmood.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.location.*
import com.google.android.material.tabs.TabLayoutMediator
import com.shihs.tripmood.R
import com.shihs.tripmood.databinding.FragmentHomeBinding
import com.shihs.tripmood.home.adapter.ViewPagerAdapter
import com.shihs.tripmood.util.HomePlanFilter

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding

    lateinit var client: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        client = LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val viewPagerAdapter = ViewPagerAdapter(childFragmentManager, lifecycle)
        val viewPager2 = binding.pager
        val tabLayout = binding.tableLayout

        viewPager2.adapter = viewPagerAdapter

        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            when (HomePlanFilter.values()[position]) {

                HomePlanFilter.INDIVIDUAL -> {
                    tab.text = getString(R.string.home_individual)
                }

                else -> {
                    tab.text = getString(R.string.home_cowork)
                }
            }
        }.attach()
    }
}
