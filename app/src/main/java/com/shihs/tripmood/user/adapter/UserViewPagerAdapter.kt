package com.shihs.tripmood.user.adapter

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.shihs.tripmood.user.child.UserChildFragment
import com.shihs.tripmood.util.UserPlanFilter

class UserViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(
    fragmentManager,
    lifecycle
) {

    override fun getItemCount() = UserPlanFilter.values().size

    override fun createFragment(position: Int): Fragment {
        Log.d("Steven", "createFragment$position")
        return UserChildFragment(UserPlanFilter.values()[position])
    }
}
