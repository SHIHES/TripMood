package com.shihs.tripmood.service

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.location.Location
import android.os.Binder
import android.os.IBinder
import android.os.Looper
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.location.*
import java.util.concurrent.TimeUnit

class UserLocatedService : Service() {

    private var isConfigurationChange = false

    private var isServiceRunningInForeground = false

    private val localBinder = LocalBinder()

    private lateinit var notificationManager: NotificationManager

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private lateinit var locationRequest: LocationRequest

    private lateinit var locationCallback: LocationCallback

    private var currentLocation: Location? = null

    inner class LocalBinder : Binder() {
        internal val service: UserLocatedService
            get() = this@UserLocatedService
    }

    override fun onCreate() {
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        locationRequest = LocationRequest.create().apply {

            interval = TimeUnit.SECONDS.toMillis(10)

            fastestInterval = TimeUnit.SECONDS.toMillis(5)

            maxWaitTime = TimeUnit.MINUTES.toMillis(2)

            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)

                currentLocation = locationResult.lastLocation

                val intent = Intent(ACTION_TRIPMOOD_LOCATION_BROADCAST)
                intent.putExtra(EXTRA_LOCATION, currentLocation)
                LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
            }
        }
    }

    override fun onBind(p0: Intent?): IBinder? {
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
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest, locationCallback, Looper.getMainLooper()
            )
        } catch (unlikely: SecurityException) {

        }
    }

    companion object {

        private const val PACKAGE_NAME = "com.shihs.tripmood"

        internal const val ACTION_TRIPMOOD_LOCATION_BROADCAST = "$PACKAGE_NAME.action.TRIPMOOD_LOCATION_BROADCAST"

        internal const val EXTRA_LOCATION = "$PACKAGE_NAME.extra.LOCATION"

    }


}