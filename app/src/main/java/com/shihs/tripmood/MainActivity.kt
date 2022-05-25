package com.shihs.tripmood

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationBarView
import com.shihs.tripmood.databinding.ActivityMainBinding
import com.shihs.tripmood.ext.getVmFactory
import com.shihs.tripmood.service.UserLocatedService
import com.shihs.tripmood.util.CurrentFragmentType
import com.shihs.tripmood.util.UserManager.isLoggedIn

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private var locationPermissionGranted = false
    private lateinit var mContext: Context
    private var userLocatedServiceBound = false
    private var userLocatedService: UserLocatedService? = null
    private lateinit var tripMoodBroadcastReceiver: TripMoodBroadcastReceiver
    val viewModel by viewModels <MainViewModel> { getVmFactory() }

    private val userLocatedServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {

            val binder = service as UserLocatedService.LocalBinder
            userLocatedService = binder.service
            userLocatedServiceBound = true
            viewModel.getUserLocatedServiceStatus(userLocatedServiceBound)
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            userLocatedService = null
            userLocatedServiceBound = false
            viewModel.getUserLocatedServiceStatus(userLocatedServiceBound)
        }
    }
    companion object {
        var LOCATION_REQUEST_CODE = 999
    }

    private inner class TripMoodBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            Log.d("SS", "TripMoodBroadcastReceiver intent $p1")
            val location = p1?.getParcelableExtra<Location>(
                UserLocatedService.EXTRA_LOCATION
            )

            Log.d("SS", "TripMoodBroadcastReceiver $location")
            if (location != null) {
                viewModel.setUserLocation(location)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        bindService()
    }

    private fun bindService() {
        val serviceIntent = Intent(this, UserLocatedService::class.java)
        if (isLoggedIn) bindService(serviceIntent, userLocatedServiceConnection, BIND_AUTO_CREATE)
    }

    override fun onResume() {
        super.onResume()
        registerLocationReceiver()
        viewModel.resetBroadcastStatus()
    }

    private fun registerLocationReceiver() {
        Log.d(
            "SS",
            "registerLocationReceiver $isLoggedIn && ${viewModel.isBroadcastRegistered.value}"
        )
        if (isLoggedIn && viewModel.isBroadcastRegistered.value != true) {
            LocalBroadcastManager.getInstance(this).registerReceiver(
                tripMoodBroadcastReceiver,
                IntentFilter(
                    UserLocatedService.ACTION_TRIPMOOD_LOCATION_BROADCAST
                )
            )
            viewModel.setBroadcastRegistered()
        }
    }

    override fun onPause() {
        if (isLoggedIn) {
            LocalBroadcastManager.getInstance(this)
                .unregisterReceiver(tripMoodBroadcastReceiver)
            viewModel.resetBroadcastStatus()
        }
        super.onPause()
    }

    override fun onStop() {
        if (userLocatedServiceBound && isLoggedIn) {
            unbindService(userLocatedServiceConnection)
            userLocatedServiceBound = false
        }
        super.onStop()
    }

    private fun getLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionGranted = true
            bindService()
            registerLocationReceiver()
        } else {
            requestLocationPermission()
        }
    }

    private fun requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            AlertDialog.Builder(this)
                .setMessage(getString(R.string.home_hint_dialog_title))
                .setPositiveButton(getString(R.string.accept)) { _, _ ->
                    ActivityCompat.requestPermissions(
                        this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        LOCATION_REQUEST_CODE
                    )
                }
                .setNegativeButton(getString(R.string.cancel)) { _, _ -> requestLocationPermission() }
                .show()
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            LOCATION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty()) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        locationPermissionGranted = true
                        bindService()
                        registerLocationReceiver()
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            LOCATION_REQUEST_CODE -> {
                getLocationPermission()
                bindService()
                registerLocationReceiver()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mContext = this

        tripMoodBroadcastReceiver = TripMoodBroadcastReceiver()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: NavigationBarView = binding.bottomNavigationView

        navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_search,
                R.id.navigation_collect,
                R.id.navigation_user
            )
        )
//        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        viewModel.currentFragmentType.observe(
            this,
            Observer {
                when (it) {
                    CurrentFragmentType.PLAN_MODE -> {
                    }
                }
            }
        )

        viewModel.isUserLocatedServiceReady.observe(this) {
            it?.let {
                if (it) {
                    Log.d("SS", "isUserLocatedServiceReady $it")
                    userLocatedService?.subscribeToLocationUpdates()
                    viewModel.resetUserLocateServiceStatus()
                }
            }
        }

        viewModel.userLocation.observe(this) {
            it?.let {
                Log.d("SS", "userLocation $it")
                viewModel.updateUserLocation(it)
                viewModel.onUpdateUserLocation()
            }
        }

        setBtn()
        setupBottomNav()
        createNotificationsChannels()
        setupNavController()

        if (isLoggedIn) getLocationPermission()
    }

    private fun setBtn() {
        binding.notificationBtn.setOnClickListener {
            navController.navigate(MobileNavigationDirections.actionGlobalNotificationFragment())
        }
    }

    private fun setupNavController() {
        findNavController(R.id.nav_host_fragment_activity_main).addOnDestinationChangedListener { navController: NavController, _: NavDestination, _: Bundle? ->
            viewModel.currentFragmentType.value = when (navController.currentDestination?.id) {
                R.id.navigation_home -> CurrentFragmentType.HOME
                R.id.navigation_search -> CurrentFragmentType.SEARCH
                R.id.planModeDialog -> CurrentFragmentType.PLAN_MODE
                R.id.mapFragment -> CurrentFragmentType.FIND_SPOT_MAP
                R.id.detailFragment -> CurrentFragmentType.MY_PLAN
                R.id.showAllLocationFragment -> CurrentFragmentType.MY_PLAN_MAP
                R.id.createScheduleFragment -> CurrentFragmentType.CREATE_SCHEDULE
                R.id.createPlanFragment -> CurrentFragmentType.CREATE_PLAN
                R.id.detailFragment -> CurrentFragmentType.DETAIL_SCHEDULE
                R.id.editScheduleFragment -> CurrentFragmentType.EDIT_SCHEDULE
                R.id.navigation_collect -> CurrentFragmentType.CHAT
                R.id.navigation_user -> CurrentFragmentType.USER
                else -> viewModel.currentFragmentType.value
            }
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
                R.id.navigation_collect -> {

                    findNavController(R.id.nav_host_fragment_activity_main).navigate(
                        MobileNavigationDirections.actionGlobalNavigationCollect()
                    )
                    return@setOnItemSelectedListener true
                }
                R.id.navigation_search -> {

                    findNavController(R.id.nav_host_fragment_activity_main).navigate(
                        MobileNavigationDirections.actionGlobalNavigationSearch()
                    )
                    return@setOnItemSelectedListener true
                }
                R.id.navigation_user -> {

                    findNavController(R.id.nav_host_fragment_activity_main).navigate(
                        MobileNavigationDirections.actionGlobalNavigationUser()
                    )
                    return@setOnItemSelectedListener false
                }
                R.id.navigation_addPlan -> {

                    findNavController(R.id.nav_host_fragment_activity_main).navigate(
                        MobileNavigationDirections.actionGlobalCreatePlanFragment()
                    )
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

    fun hideToolBar() {
        binding.toolbar.visibility = View.GONE
    }

    fun showToolBar() {
        binding.toolbar.visibility = View.VISIBLE
    }

    fun hideBottomNavBar() {
        binding.bottomNavigationView.visibility = View.GONE
    }

    fun showBottomNavBar() {
        binding.bottomNavigationView.visibility = View.VISIBLE
    }
}
