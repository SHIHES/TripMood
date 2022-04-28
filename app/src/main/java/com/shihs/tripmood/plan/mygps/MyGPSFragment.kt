package com.shihs.tripmood.plan.mygps

import android.Manifest
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.PhotoMetadata
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPhotoRequest
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.shihs.tripmood.MainActivity
import com.shihs.tripmood.R
import com.shihs.tripmood.databinding.FragmentPlanMygpsBinding
import com.shihs.tripmood.ext.toBase64String
import com.shihs.tripmood.plan.adapter.LocationAdapter


class MyGPSFragment : Fragment(), OnMapReadyCallback {

    private var map: GoogleMap? = null
    private var cameraPosition: CameraPosition? = null

    // The entry point to the Places API.
    private lateinit var placesClient: PlacesClient
    private lateinit var binding: FragmentPlanMygpsBinding
    private lateinit var  presenter : MapPresenter

    private var locationPermissionGranted = false

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private var lastKnownLocation: Location? = null

    private var recommendPlace = mutableListOf<com.shihs.tripmood.dataclass.source.Location>()

    private lateinit var handler: Handler

    private lateinit var locationAdapter: LocationAdapter

    private var updateLocationTask = object : Runnable {
        override fun run() {
            showCurrentPlace()
            handler.postDelayed(this, 3000)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlanMygpsBinding.inflate(inflater, container, false)


        // [START_EXCLUDE silent]
        // Retrieve location and camera position from saved instance state.
        // [START maps_current_place_on_create_save_instance_state]
        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION)
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION)
        }

        val info = (activity as MainActivity).applicationContext.packageManager
            .getApplicationInfo(
                (activity as MainActivity).packageName,
                PackageManager.GET_META_DATA
            )
        val key = info.metaData[resources.getString(R.string.map_api_key_name)].toString()

        Places.initialize(requireActivity(), key)
        placesClient = Places.createClient(requireActivity())

        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        setBtn()

        presenter = MapPresenter(this)
        presenter.onViewCreated()

        (requireActivity() as MainActivity).hideActionBar()

        handler = Handler(Looper.getMainLooper())

        val recyclerInfo = binding.recyclerInfo

        locationAdapter = LocationAdapter(LocationAdapter.OnClickListener{

        })

        recyclerInfo.adapter = locationAdapter
        recyclerInfo.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        return binding.root
    }



    private fun startTracking() {

        map?.clear()

        presenter.startTracking()
    }

    private fun stopTracking() {
        presenter.stopTracking()
    }

    /**
     * Saves the state of the map when the activity is paused.
     */

    // [START maps_current_place_on_save_instance_state]
    override fun onSaveInstanceState(outState: Bundle) {
        map?.let { map ->
            outState.putParcelable(KEY_CAMERA_POSITION, map.cameraPosition)
            outState.putParcelable(KEY_LOCATION, lastKnownLocation)
        }
        super.onSaveInstanceState(outState)
    }


    /**等等要在這邊放取得place所有資訊的action**/
    fun setBtn() {
        binding.controlLayout.setOnClickListener {

            if(binding.startRecordBtn.text == getString(R.string.start_label)){
                recommendPlace.clear()
                locationAdapter.notifyDataSetChanged()
                handler.post(updateLocationTask)
                startTracking()

                binding.startRecordBtn.text = getString(R.string.stop_label)
                binding.controlLayout.setBackgroundColor(resources.getColor(R.color.tripMood_red))
                binding.controlIcon.setImageResource(R.drawable.ic_baseline_stop_24)
            } else {
                locationAdapter.submitList(recommendPlace)
                locationAdapter.notifyDataSetChanged()
                addMarker(recommendPlace)
                handler.removeCallbacks(updateLocationTask)
                stopTracking()

                binding.startRecordBtn.text = getString(R.string.start_label)
                binding.controlLayout.setBackgroundColor(resources.getColor(R.color.tripMood_green))
                binding.controlIcon.setImageResource(R.drawable.ic_baseline_play_arrow_24)
            }
        }
    }


    /**
     * Manipulates the map when it's available.
     * This callback is triggered when the map is ready to be used.
     */

    @SuppressLint("PotentialBehaviorOverride")
    override fun onMapReady(map: GoogleMap) {
        this.map = map

        presenter.ui.observe(this){ ui ->
            updateUI(ui)
        }
        presenter.onMapLoaded()
        map.uiSettings.isZoomControlsEnabled = true

        this.map?.setInfoWindowAdapter(object : GoogleMap.InfoWindowAdapter {
            // Return null here, so that getInfoContents() is called next.
            override fun getInfoWindow(arg0: Marker): View? {
                return null
            }

            override fun getInfoContents(marker: Marker): View {
                // Inflate the layouts for the info window, title and snippet.
                val infoWindow = layoutInflater.inflate(
                    R.layout.custom_info_contents,
                    requireActivity().findViewById<FrameLayout>(R.id.map), false
                )
                val title = infoWindow.findViewById<TextView>(R.id.title)
                title.text = marker.title
                val snippet = infoWindow.findViewById<TextView>(R.id.snippet)
                snippet.text = marker.snippet
                return infoWindow
            }
        })

        getLocationPermission()

    }

    @SuppressLint("MissingPermission")
    private fun updateUI(ui: Ui) {
        if(ui.currentLocation != null && ui.currentLocation != map?.cameraPosition?.target){
            map?.isMyLocationEnabled = true
            map?.animateCamera(CameraUpdateFactory.newLatLngZoom(ui.currentLocation, 18f))
        }
        drawRoute(ui.userPath)
    }

    private fun drawRoute(locations: List<LatLng>) {
        val polylineOptions = PolylineOptions()

        map?.clear()

        val points = polylineOptions.points
        points.addAll(locations)

        map?.addPolyline(polylineOptions)
    }

    /**
     * Prompts the user for permission to use the device location.
     */
    // [START maps_current_place_location_permission]
    private fun getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        }
    }
    /**
     * Handles the result of the request for location permissions.
     */
    // [START maps_current_place_on_request_permissions_result]
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        locationPermissionGranted = false
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    locationPermissionGranted = true
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }
    /**
     * Prompts the user to select the current place from a list of likely places, and shows the
     * current place on the map - provided the user has granted location permission.
     */
    // [START maps_current_place_show_current_place]
    @SuppressLint("MissingPermission")
    private fun showCurrentPlace() {
        if (map == null) {
            return
        }

        if (locationPermissionGranted) {
            // Use fields to define the data types to return.
            val placeFields = listOf(
                Place.Field.NAME,
                Place.Field.ADDRESS,
                Place.Field.LAT_LNG,
                Place.Field.TYPES,
                Place.Field.PHOTO_METADATAS,
                Place.Field.ICON_URL,
                Place.Field.RATING
            )

            // Use the builder to create a FindCurrentPlaceRequest.
            val request = FindCurrentPlaceRequest.newInstance(placeFields)

            // Get the likely places - that is, the businesses and other points of interest that
            // are the best match for the device's current location.
            val placeResult = placesClient.findCurrentPlace(request)
            placeResult.addOnCompleteListener { task ->
                if (task.isSuccessful && task.result != null) {
                    val likelyPlaces = task.result

                    // Set the count, handling cases where less than 5 entries are returned.
                    val count =
                        if (likelyPlaces != null &&
                            likelyPlaces.placeLikelihoods.size < M_MAX_ENTRIES) {
                            likelyPlaces.placeLikelihoods.size
                        } else {
                            M_MAX_ENTRIES
                        }
                    var i = 0

                    for (placeLikelihood in likelyPlaces?.placeLikelihoods ?: emptyList()) {
                        var result = com.shihs.tripmood.dataclass.source.Location()
                        var place = placeLikelihood.place

                        result.name = placeLikelihood.place.name
                        result.address = placeLikelihood.place.address
                        result.latitude = placeLikelihood.place.latLng?.latitude
                        result.longitude = placeLikelihood.place.latLng?.longitude
                        result.type = placeLikelihood.place.types as List<String>?
                        result.icon = placeLikelihood.place.iconUrl
                        result.rating = placeLikelihood.place.rating


                        showCurrentPlacePhoto(place = place, result = result)

                        Log.d("SS", "placeLikelihood ${result}")

                        recommendPlace.add(result)

                        i++

                        if (i > count - 1) {
                            break
                        }
                    }

//                    openPlacesDialog()
                } else {
                    Log.e(TAG, "Exception: %s", task.exception)
                }
            }
        } else {
            getLocationPermission()
        }
    }

    private fun showCurrentPlacePhoto(place: Place, result: com.shihs.tripmood.dataclass.source.Location){
        val photoMetadata = place.photoMetadatas?.get(0)

        if(photoMetadata == null){
            return
        }

        val photoRequest = FetchPhotoRequest
            .builder(photoMetadata)
            .setMaxWidth(resources.getDimensionPixelSize(R.dimen.bitmap_height))
            .setMaxHeight(resources.getDimensionPixelSize(R.dimen.bitmap_width))
            .build()

        placesClient.fetchPhoto(photoRequest).addOnSuccessListener { response ->
            val bitmap = response.bitmap
            result.image = bitmap.toBase64String()
            Log.d("SS", "fetchPhoto bitmap${result.image}")



        } .addOnFailureListener { e ->
            Log.d("SS", "fetchPhoto addOnFailureListener${e}")
        }
    }

    private fun addMarker(locations: List<com.shihs.tripmood.dataclass.source.Location>){

        for(location in locations){
            val latlng = LatLng(location.latitude!!, location.longitude!!)
            map?.addMarker(MarkerOptions()
                .position(latlng)
                .title(location.name)
            )

        }


    }




    companion object {
        private val TAG = MyGPSFragment::class.java.simpleName
        private const val DEFAULT_ZOOM = 20F
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1

        // Keys for storing activity state.
        // [START maps_current_place_state_keys]
        private const val KEY_CAMERA_POSITION = "camera_position"
        private const val KEY_LOCATION = "location"
        // [END maps_current_place_state_keys]

        // Used for selecting the current place.
        const val M_MAX_ENTRIES = 5
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).hideActionBar()
    }

    override fun onDestroy() {
        super.onDestroy()
        (requireActivity() as MainActivity).showActionBar()
    }
}








/**
 * Displays a form allowing the user to select a place from a list of likely places.
 */
// [START maps_current_place_open_places_dialog]
//    private fun openPlacesDialog() {
//        // Ask the user to choose the place where they are now.
//        val listener =
//            DialogInterface.OnClickListener { dialog, which -> // The "which" argument contains the position of the selected item.
//                var latLng  = recommendPlace[which].latitude?.let {
//                    recommendPlace[which].longitude?.let {
//                            it1 ->
//                    LatLng(it, it1) }
//                }
//                val markerLatLng = latLng
//                var markerSnippet = recommendPlace[which].address
////                if (likelyPlaceAttributions[which] != null) {
////                    markerSnippet = """
////                    $markerSnippet
////                    ${likelyPlaceAttributions[which]}
////                    """.trimIndent()
////                }
//
//                if (markerLatLng == null) {
//                    return@OnClickListener
//                }
//
//                // Add a marker for the selected place, with an info window
//                // showing information about that place.
//                map?.addMarker(
//                    MarkerOptions()
//                        .title(recommendPlace[which].name)
//                        .position(markerLatLng)
//                        .snippet(markerSnippet)
//                )
//
//                // Position the map's camera at the location of the marker.
//                map?.moveCamera(
//                    CameraUpdateFactory.newLatLngZoom(
//                        markerLatLng,
//                        DEFAULT_ZOOM.toFloat()
//                    )
//                )
//            }
//        var recommendNamelist = mutableListOf<String>()
//        for (i in recommendPlace){
//            i.name?.let { recommendNamelist.add(it) }
//        }
//
//        // Display the dialog.
//        AlertDialog.Builder(requireActivity())
//            .setTitle(R.string.pick_place)
//            .setItems(recommendNamelist.toTypedArray(), listener)
//            .show()
//
//    }
// [END maps_current_place_open_places_dialog]