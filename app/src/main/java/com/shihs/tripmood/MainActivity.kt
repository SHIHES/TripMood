package com.shihs.tripmood

import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationBarView
import com.shihs.tripmood.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

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

        setBtn()
        setupBottomNav()
        createNotificationsChannels()
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
}