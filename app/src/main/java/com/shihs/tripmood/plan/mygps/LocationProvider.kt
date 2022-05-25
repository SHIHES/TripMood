package com.shihs.tripmood.plan.mygps

import android.annotation.SuppressLint
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import java.util.*
import kotlin.math.roundToInt

@SuppressLint("MissingPermission")
class LocationProvider(private val fragment: Fragment) {

    // The entry point to the Fused Location Provider. Construct a FusedLocationProviderClient.
    private val client by lazy { LocationServices.getFusedLocationProviderClient(fragment.requireActivity()) }

//    private val placesClient by lazy { Places.createClient(fragment.requireActivity()) }

    private val locations = mutableListOf<LatLng>()
    private var distance = 0

    val liveLocations = MutableLiveData<List<LatLng>>()
    val liveDistance = MutableLiveData<Int>()
    val liveLocation = MutableLiveData<LatLng>()

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            val currentLocation = result.lastLocation
            val latLng = LatLng(currentLocation.latitude, currentLocation.longitude)

            val lastLocation = locations.lastOrNull()

            if (lastLocation != null) {
                distance += SphericalUtil.computeDistanceBetween(lastLocation, latLng).roundToInt()
                liveDistance.value = distance
            }

            locations.add(latLng)
            liveLocations.value = locations
        }
    }

    fun getUserLocation() {
        try {
            client.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val latLng = LatLng(location.latitude, location.longitude)
                    locations.add(latLng)
                    liveLocation.value = latLng
                } else {
                    Log.d("SS", "Current location is null.")
                }
            }
        } catch (e: SecurityException) {
            Log.e("SS", e.message, e)
        }
    }

    fun trackUser() {
        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 5000
        client.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    fun stopTracking() {
        client.removeLocationUpdates(locationCallback)
        locations.clear()
        distance = 0
    }
}
