package com.shihs.tripmood.plan.mygps

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPhotoRequest
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.shihs.tripmood.MainActivity
import com.shihs.tripmood.R
import com.shihs.tripmood.databinding.FragmentPlanMygpsBinding
import com.shihs.tripmood.ext.getVmFactory
import com.shihs.tripmood.ext.toBase64String
import com.shihs.tripmood.plan.adapter.LocationAdapter

class MyGPSFragment : Fragment(), OnMapReadyCallback {

    private var map: GoogleMap? = null
    private var cameraPosition: CameraPosition? = null

    // The entry point to the Places API.
    private lateinit var placesClient: PlacesClient
    private lateinit var binding: FragmentPlanMygpsBinding

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private val viewModel by viewModels<MyGPSViewModel> {
        getVmFactory(
            MyGPSFragmentArgs.fromBundle(requireArguments()).myPlan,
            MyGPSFragmentArgs.fromBundle(requireArguments()).selectedSchedule,
            MyGPSFragmentArgs.fromBundle(requireArguments()).selectedPosition
        )
    }

    private lateinit var presenter: MapPresenter

    private var lastKnownLocation: Location? = null

    private var recommendPlace = mutableListOf<com.shihs.tripmood.dataclass.Location>()

    private val arg: MyGPSFragmentArgs by navArgs()

    private val defaultLocation = LatLng(25.105497, 121.597366)

    private lateinit var locationAdapter: LocationAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlanMygpsBinding.inflate(inflater, container, false)

        val info = (activity as MainActivity).applicationContext.packageManager
            .getApplicationInfo(
                (activity as MainActivity).packageName,
                PackageManager.GET_META_DATA
            )

        val key = info.metaData[resources.getString(R.string.map_api_key_name)].toString()

        Places.initialize(requireActivity(), key)

        placesClient = Places.createClient(requireActivity())

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        setBtn()

        (requireActivity() as MainActivity).hideToolBar()

        val recyclerInfo = binding.recyclerInfo

        locationAdapter = LocationAdapter(
            LocationAdapter.OnClickListener {
                it.image = null
                viewModel.getLocationFromCard(it)
            }
        )

        recyclerInfo.adapter = locationAdapter
        recyclerInfo.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        viewModel.selectedLocation.observe(
            viewLifecycleOwner,
            Observer {
                it?.let {
                    showInterestDialog(it)
                }
            }
        )

        viewModel.nearbyLocation.observe(viewLifecycleOwner) {
            it?.let {
                Log.d("SS", "nearbyLocation $it")
                locationAdapter.submitList(it)
                locationAdapter.notifyDataSetChanged()
                addMarker(it)
            }
        }

        presenter = MapPresenter(this)
        presenter.onViewCreated()

        return binding.root
    }

    private fun showInterestDialog(location: com.shihs.tripmood.dataclass.Location) {

        MaterialAlertDialogBuilder(requireActivity())
            .setTitle(resources.getString(R.string.add_spot_title))
            .setMessage(resources.getString(R.string.add_spot_support_msg))
            .setNegativeButton(resources.getString(R.string.decline)) { dialog, which ->
                Toast.makeText(context, "忍痛放棄QQ", Toast.LENGTH_LONG).show()
            }
            .setPositiveButton(resources.getString(R.string.accept)) { dialog, which ->
                Toast.makeText(context, "加入成功!", Toast.LENGTH_LONG).show()
                viewModel.packageGPSSchedule(location = location)
            }
            .show()
    }
    private fun startTracking() {

        map?.clear()

        presenter.startTracking()
    }

    private fun stopTracking() {
        presenter.stopTracking()
    }

    fun setBtn() {
        binding.controlLayout.setOnClickListener {
            startTracking()
            showCurrentPlace()
        }

        binding.gpsBackButton.setOnClickListener {
            stopTracking()
            findNavController().navigateUp()
        }
    }

    @SuppressLint("PotentialBehaviorOverride")
    override fun onMapReady(map: GoogleMap) {
        this.map = map

        presenter.ui.observe(this) { ui ->
            updateUI(ui)
        }
        presenter.onMapLoaded()
        map.uiSettings.isZoomControlsEnabled = true

        map.uiSettings.isZoomControlsEnabled = true
        map.uiSettings.isMyLocationButtonEnabled = true
        map.uiSettings.setAllGesturesEnabled(true)
        map.uiSettings.isCompassEnabled = true

        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(defaultLocation, 13F)

        map.moveCamera(cameraUpdate)

        if (isLocationPermissionGranted()) {
        } else {
            requestLocationPermission()
        }
    }

    @SuppressLint("MissingPermission")
    private fun updateUI(ui: Ui) {
        if (ui.currentLocation != null && ui.currentLocation != map?.cameraPosition?.target) {
            map?.isMyLocationEnabled = true
        }
    }

    @SuppressLint("MissingPermission")
    private fun showCurrentPlace() {

        recommendPlace.clear()
        viewModel.clearNearbyLocation()

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

        val placeResult = placesClient.findCurrentPlace(request)
        placeResult.addOnCompleteListener { task ->
            if (task.isSuccessful && task.result != null) {
                val likelyPlaces = task.result
                Log.d("SS", "showCurrentPlace isSuccessful")
                val count =
                    if (likelyPlaces != null &&
                        likelyPlaces.placeLikelihoods.size < M_MAX_ENTRIES
                    ) {
                        likelyPlaces.placeLikelihoods.size
                    } else {
                        M_MAX_ENTRIES
                    }
                var i = 0

                for (placeLikelihood in likelyPlaces?.placeLikelihoods ?: emptyList()) {
                    var result = com.shihs.tripmood.dataclass.Location()
                    var place = placeLikelihood.place

                    Log.d("SS", "placeLikelihood isSuccessful ${placeLikelihood.place}")

                    result.name = placeLikelihood.place.name
                    result.address = placeLikelihood.place.address
                    result.latitude = placeLikelihood.place.latLng?.latitude
                    result.longitude = placeLikelihood.place.latLng?.longitude
                    result.type = placeLikelihood.place.types as List<String>?
                    result.icon = placeLikelihood.place.iconUrl
                    result.rating = placeLikelihood.place.rating

                    Log.d("SS", "result Successful $result")

                    showCurrentPlacePhoto(place = place, result = result)

                    recommendPlace.add(result)

                    Log.d("SS", "result Successful recommendPlace${recommendPlace.size}")

                    i++

                    if (i > count - 1) {
                        break
                    }
                }
                viewModel.getNearbyLocation(recommendPlace)
            } else {
                Log.e(TAG, "Exception: %s", task.exception)
            }
        }
    }

    private fun showCurrentPlacePhoto(place: Place, result: com.shihs.tripmood.dataclass.Location) {
        val photoMetadata = place.photoMetadatas?.get(0)

        if (photoMetadata == null) {
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
        }.addOnFailureListener { e ->
            Log.d("SS", "fetchPhoto addOnFailureListener$e")
        }
    }

    private fun addMarker(locations: List<com.shihs.tripmood.dataclass.Location>) {

        for (location in locations) {
            val latlng = LatLng(location.latitude!!, location.longitude!!)

            map?.addMarker(
                MarkerOptions()
                    .position(latlng)
                    .title(location.name)
            )
        }
    }

    companion object {
        private val TAG = MyGPSFragment::class.java.simpleName
        private const val DEFAULT_ZOOM = 13F
        private const val ACCESS_FINE_LOCATION = 100
        const val M_MAX_ENTRIES = 6
    }

    private fun isLocationPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            ACCESS_FINE_LOCATION
        )
        // AppConstant.LOCATION_REQUEST_CODE 為自己隨意定義的 int（例如：999）
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == ACCESS_FINE_LOCATION) {
            if (grantResults.size > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                // 被拒絕惹
                Toast.makeText(requireContext(), "QQQQQQQ", Toast.LENGTH_LONG).show()
                if (!ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                    // 被拒絕惹 + 按了never ask again
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).hideToolBar()
    }

    override fun onDestroy() {
        super.onDestroy()
        (requireActivity() as MainActivity).showToolBar()
    }
}
