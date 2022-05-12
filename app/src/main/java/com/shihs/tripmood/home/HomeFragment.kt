package com.shihs.tripmood.home

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.google.common.reflect.Reflection.getPackageName
import com.shihs.tripmood.databinding.FragmentHomeBinding
import com.shihs.tripmood.home.adapter.ViewPagerAdapter
import com.shihs.tripmood.util.HomePlanFilter


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    var switch = false

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
                HomePlanFilter.INDIVIDUAL -> {
                    tab.text = "獨自規劃"
                }
                else -> {
                    tab.text = "共同編輯"
                }
            }
        }.attach()

        if (switch == false) {
            showDialog()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun openAppSettingsIntent() {
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", requireContext().getPackageName(), null)
        )
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }



    private fun showDialog() {
        val builder = AlertDialog.Builder(requireContext())

            builder.setMessage("開啟GPS功能已獲得更好的體驗")
                .setTitle("小提醒")
                .setNegativeButton("取消"){ dialog, which ->
                    switch = true
                    Log.d("SS", "switch  $switch")
                }
                .setPositiveButton("前往設定"){dialog, which ->

                    openAppSettingsIntent()
                    switch = true
                }

        val dialog = builder.create()

        if (switch == false){
            dialog.show()
        } else{
            dialog.cancel()
        }
    }
}