package com.shihs.tripmood.home

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.shihs.tripmood.databinding.FragmentHomeBinding
import com.shihs.tripmood.home.adapter.ViewPagerAdapter
import com.shihs.tripmood.util.HomePlanFilter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {



        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val viewPagerAdapter = ViewPagerAdapter(childFragmentManager, lifecycle)
        val viewPager2 = binding.pager
        val tabLayout = binding.tableLayout

        viewPager2.adapter = viewPagerAdapter

        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            when (HomePlanFilter.values()[position]) {
                HomePlanFilter.INDIVIDUAL  -> {
                    tab.text = "獨自規劃"
                }
                else -> {
                    tab.text = "共同編輯"
                }
            }
        }.attach()


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}