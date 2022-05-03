package com.shihs.tripmood.home.adapter

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.shihs.tripmood.dataclass.Plan
import com.shihs.tripmood.home.PlanFilter
import com.shihs.tripmood.home.childpage.ChildFragment

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount() = PlanFilter.values().size

    override fun createFragment(position: Int): Fragment {
        Log.d("Steven", "createFragment${position}")
        return ChildFragment(PlanFilter.values()[position])
    }
}