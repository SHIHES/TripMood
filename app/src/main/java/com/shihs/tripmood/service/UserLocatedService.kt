package com.shihs.tripmood.service

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.content.res.Configuration
import android.location.Location
import android.os.Binder
import android.os.Bundle
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.location.*
import com.shihs.tripmood.plan.mygps.MyGPSFragment
import java.util.concurrent.TimeUnit

class UserLocatedService : Service() {

    private var isConfigurationChange = false

    private var isServiceRunningInForeground = false

    private val localBinder = LocalBinder()

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private lateinit var locationRequest: LocationRequest

    private lateinit var locationCallback: LocationCallback

    private var currentLocation: Location? = null

    inner class LocalBinder : Binder() {
        internal val service: UserLocatedService
            get() = this@UserLocatedService
    }

    override fun onCreate() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        locationRequest = LocationRequest.create().apply {
            interval = TimeUnit.SECONDS.toMillis(10)

            fastestInterval = TimeUnit.SECONDS.toMillis(6)

            maxWaitTime = TimeUnit.MINUTES.toMillis(1)

            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)

                Log.d("SS", "onLocationResult $locationResult")

                currentLocation = locationResult.lastLocation

                val intent = Intent(ACTION_TRIPMOOD_LOCATION_BROADCAST)

                intent.putExtra(EXTRA_LOCATION, currentLocation)

                LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
                
                val currentLocationBundle = Bundle()
                currentLocationBundle.putParcelable("bundle", currentLocation)
                val gpsFragment = MyGPSFragment()
                gpsFragment.arguments = currentLocationBundle
            }
        }
    }

    override fun onBind(p0: Intent?): IBinder {
        stopForeground(true)
        isServiceRunningInForeground = false
        isConfigurationChange = false
        return localBinder
    }

    override fun onRebind(intent: Intent) {
        stopForeground(true)
        isServiceRunningInForeground = false
        isConfigurationChange = false
        super.onRebind(intent)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        isConfigurationChange = true
    }

    @SuppressLint("MissingPermission")
    fun subscribeToLocationUpdates() {
        startService(Intent(applicationContext, UserLocatedService::class.java))

        try {
            Log.d("SS", "subscribeToLocationUpdates succuss")
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        } catch (e: Exception) {
            Log.d("SS", "subscribeToLocationUpdates $e")
        }
    }
    


    companion object {

        const val PACKAGE_NAME = "com.shihs.tripmood"

        const val ACTION_TRIPMOOD_LOCATION_BROADCAST = "$PACKAGE_NAME.action.TRIPMOOD_LOCATION_BROADCAST"

        const val EXTRA_LOCATION = "$PACKAGE_NAME.extra.LOCATION"
        
    }
    
    
}


