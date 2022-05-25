package com.shihs.tripmood

import android.Manifest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.shihs.tripmood.plan.mygps.LocationProvider

class PermissionsManager(
    fragment: Fragment,
    private val locationProvider: LocationProvider
) {

    private val locationPermissionProvider = fragment.registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            locationProvider.getUserLocation()
        }
    }

    fun requestUserLocation() {
        locationPermissionProvider.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }
}
