package com.shihs.tripmood.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.shihs.tripmood.MobileNavigationDirections
import com.shihs.tripmood.R
import com.shihs.tripmood.databinding.FragmentUserBinding
import com.shihs.tripmood.home.adapter.ViewPagerAdapter
import com.shihs.tripmood.user.adapter.UserViewPagerAdapter
import com.shihs.tripmood.util.HomePlanFilter
import com.shihs.tripmood.util.UserManager
import com.shihs.tripmood.util.UserPlanFilter
import org.checkerframework.checker.nullness.compatqual.MonotonicNonNullDecl

class UserFragment : Fragment() {

    private var _binding: FragmentUserBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentUserBinding.inflate(inflater, container, false)

        val root: View = binding.root

        binding.userName.text = UserManager.userName
        Glide.with(requireContext()).load(UserManager.userPhotoUrl).into(binding.userImage)


        binding.logoutBtn.setOnClickListener {
            UserManager.clear()
            findNavController().navigate(MobileNavigationDirections.actionGlobalLoginFragment())
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val viewPagerAdapter = UserViewPagerAdapter(childFragmentManager, lifecycle)
        val viewPager2 = binding.userViewPager
        val tabLayout = binding.userTabLayout

        viewPager2.adapter = viewPagerAdapter

        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            when (UserPlanFilter.values()[position]) {
                UserPlanFilter.MEMORY  -> {
                    tab.text = "回憶"
                }
                else -> {
                    tab.text = "收藏"
                }
            }
        }.attach()


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}