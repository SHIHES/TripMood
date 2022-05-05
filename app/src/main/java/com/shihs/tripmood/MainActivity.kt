package com.shihs.tripmood

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat.isLocationEnabled
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import app.appworks.school.publisher.ext.getVmFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.navigation.NavigationBarView
import com.shihs.tripmood.databinding.ActivityMainBinding
import com.shihs.tripmood.util.CurrentFragmentType

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

//    private var PERMISSION_ID = 1000
//    lateinit var fusedLocationProviderClient: FusedLocationProviderClient


    val viewModel by viewModels<MainViewModel> { getVmFactory() }



    companion object {
        lateinit var myLocation: String
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: NavigationBarView = binding.bottomNavigationView

        navView.menu.getItem(2).isEnabled = false

        navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_search,
                R.id.navigation_chat,
                R.id.navigation_user
            )
        )
//        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        viewModel.currentFragmentType.observe(
            this, Observer {

            }
        )




        setBtn()
        setupBottomNav()
        createNotificationsChannels()
        setupNavController()
    }

    private fun setupNavController() {
        findNavController(R.id.nav_host_fragment_activity_main).addOnDestinationChangedListener{navController: NavController, _: NavDestination, _:Bundle? ->
            viewModel.currentFragmentType.value = when (navController.currentDestination?.id) {
                R.id.navigation_home -> CurrentFragmentType.HOME
                R.id.navigation_search -> CurrentFragmentType.SEARCH
//                R.id.      -> CurrentFragmentType.NOTIFICATION
                R.id.mapFragment -> CurrentFragmentType.FIND_SPOT_MAP
                R.id.detailFragment -> CurrentFragmentType.MY_PLAN
                R.id.showAllLocationFragment -> CurrentFragmentType.MY_PLAN_MAP
                R.id.createScheduleFragment -> CurrentFragmentType.CREATE_SCHEDULE
                R.id.createPlanFragment -> CurrentFragmentType.CREATE_PLAN
                R.id.detailFragment -> CurrentFragmentType.DETAIL_SCHEDULE
                R.id.editScheduleFragment -> CurrentFragmentType.EDIT_SCHEDULE
                R.id.navigation_chat -> CurrentFragmentType.CHAT
                R.id.navigation_user -> CurrentFragmentType.USER
                else -> viewModel.currentFragmentType.value
            }
        }
    }

    fun setBtn() {
        binding.addFAB.setOnClickListener {
            navController.navigate(MobileNavigationDirections.actionGlobalPlanModeDialog())
        }
    }

    private fun setupBottomNav() {
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {

                    findNavController(R.id.nav_host_fragment_activity_main).navigate(
                        MobileNavigationDirections.actionGlobalNavigationHome()
                    )
                    return@setOnItemSelectedListener true
                }
                R.id.navigation_chat -> {

                    findNavController(R.id.nav_host_fragment_activity_main).navigate(
                        MobileNavigationDirections.actionGlobalNavigationHome()
                    )
                    return@setOnItemSelectedListener true
                }
                R.id.navigation_search -> {

                    findNavController(R.id.nav_host_fragment_activity_main).navigate(
                        MobileNavigationDirections.actionGlobalNavigationSearch()
                    )
                    return@setOnItemSelectedListener true
                }
                R.id.navigation_user-> {

                    findNavController(R.id.nav_host_fragment_activity_main).navigate(
                        MobileNavigationDirections.actionGlobalNavigationNotifications()
                    )
                    return@setOnItemSelectedListener false
                }
            }
            false
        }
    }

    private fun createNotificationsChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                getString(R.string.reminders_notification_channel_id),
                getString(R.string.reminders_notification_channel_name),
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                lightColor = Color.GREEN
                enableLights(true)
            }
            ContextCompat.getSystemService(this, NotificationManager::class.java)
                ?.createNotificationChannel(channel)
        }
    }

    fun hideActionBar() {
        binding.bottomAppBar.visibility = View.GONE
        binding.addFAB.visibility = View.GONE
        binding.bottomNavigationView.visibility = View.GONE
    }

    fun showActionBar() {
        binding.bottomAppBar.visibility = View.VISIBLE
        binding.addFAB.visibility = View.VISIBLE
        binding.bottomNavigationView.visibility = View.VISIBLE
    }


//    private fun checkPermission(): Boolean {
//        if (
//            ActivityCompat.checkSelfPermission(
//                this,
//                android.Manifest.permission.ACCESS_COARSE_LOCATION
//            ) == PackageManager.PERMISSION_GRANTED ||
//            ActivityCompat.checkSelfPermission(
//                this,
//                android.Manifest.permission.ACCESS_FINE_LOCATION
//            ) == PackageManager.PERMISSION_GRANTED
//        ) {
//            return true
//        }
//        return false
//    }
//
//
//    private fun requestPermission() {
//
//        ActivityCompat.requestPermissions(
//            this,
//            arrayOf(
//                android.Manifest.permission.ACCESS_COARSE_LOCATION,
//                android.Manifest.permission.ACCESS_FINE_LOCATION
//            ),
//            PERMISSION_ID
//        )
//    }

}