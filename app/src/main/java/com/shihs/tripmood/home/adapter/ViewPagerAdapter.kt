package com.shihs.tripmood.home.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.shihs.tripmood.home.childpage.ChildFragment
import com.shihs.tripmood.util.HomePlanFilter

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(
    fragmentManager,
    lifecycle
) {

    override fun getItemCount() = HomePlanFilter.values().size

    override fun createFragment(position: Int): Fragment {
        return ChildFragment(HomePlanFilter.values()[position])
    }
}
