package com.shihs.tripmood.plan.mygps

import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.shihs.tripmood.PermissionsManager
import com.shihs.tripmood.R

// 觀察Latlng livedata 畫出路線
class MapPresenter(private val fragment: Fragment) {

    val ui = MutableLiveData(Ui.EMPTY)

    private val locationProvider = LocationProvider(fragment)

    private val permissionsManager = PermissionsManager(fragment, locationProvider)

    fun onViewCreated() {

        locationProvider.liveLocations.observe(fragment.viewLifecycleOwner) { locations ->
            val current = ui.value
            ui.value = current?.copy(userPath = locations)
        }

        locationProvider.liveLocation.observe(fragment.viewLifecycleOwner) { currentLocation ->
            val current = ui.value
            ui.value = current?.copy(currentLocation = currentLocation)
        }

        locationProvider.liveDistance.observe(fragment.viewLifecycleOwner) { distance ->
            val current = ui.value
            val formattedDistance = fragment.getString(R.string.distance_value, distance)
            ui.value = current?.copy(formattedDistance = formattedDistance)
        }
    }

    fun onMapLoaded() {
        permissionsManager.requestUserLocation()
    }

    fun startTracking() {
        locationProvider.trackUser()

        val currentUi = ui.value
        ui.value = currentUi?.copy(
            formattedPace = Ui.EMPTY.formattedPace,
            formattedDistance = Ui.EMPTY.formattedDistance
        )
    }

    fun stopTracking() {
        locationProvider.stopTracking()
    }
}

data class Ui(
    val formattedPace: String,
    val formattedDistance: String,
    val currentLocation: LatLng?,
    val userPath: List<LatLng>
) {

    companion object {

        val EMPTY = Ui(
            formattedPace = "",
            formattedDistance = "",
            currentLocation = null,
            userPath = emptyList()
        )
    }
}
