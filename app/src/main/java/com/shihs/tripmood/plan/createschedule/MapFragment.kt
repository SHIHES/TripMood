package com.shihs.tripmood.plan.createschedule

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.shihs.tripmood.MainActivity
import com.shihs.tripmood.R
import com.shihs.tripmood.databinding.FragmentScheduleMapBinding
import com.shihs.tripmood.dataclass.Location

class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var placesClient: PlacesClient
    lateinit var binding: FragmentScheduleMapBinding
    private var map: GoogleMap? = null
    private var selectedLocation = Location()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentScheduleMapBinding.inflate(inflater, container, false)

        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync(this)



        setupAutoCompleteFragment()

        setupBtn()

        return binding.root
    }

    fun setupBtn(){
        binding.cancelBtn.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.addPlaceBtn.setOnClickListener {
            if (selectedLocation == null){
                Toast.makeText(context,"請選擇添加地點",Toast.LENGTH_SHORT).show()
            } else{
                setFragmentResult("keyForRequest", bundleOf("bundleKey" to selectedLocation))

                findNavController().navigateUp()
            }

        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        var markerOptions = MarkerOptions()
        var latLng = LatLng(23.69, 120.96)
        Log.d("QAQ", "latLng$latLng")

        markerOptions.title("HI")
        markerOptions.position(latLng)


        var cameraUdpate = CameraUpdateFactory.newLatLngZoom(latLng, 8F)
        map?.moveCamera(cameraUdpate)

        map?.uiSettings?.setAllGesturesEnabled(true)
        map?.uiSettings?.setCompassEnabled(true)
        map?.uiSettings?.setZoomControlsEnabled(true)
    }

    fun setupAutoCompleteFragment(){

        val info = (activity as MainActivity).applicationContext.packageManager
            .getApplicationInfo(
                (activity as MainActivity).packageName,
                PackageManager.GET_META_DATA
            )
        val key = info.metaData[resources.getString(R.string.map_api_key_name)].toString()


        if (!Places.isInitialized()) {
            Places.initialize(requireActivity(), key);
        }

        placesClient = Places.createClient(requireActivity())

        val autocompleteFragment = childFragmentManager
            .findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment

        autocompleteFragment.setPlaceFields(listOf(
            Place.Field.ID,
            Place.Field.NAME,
            Place.Field.LAT_LNG,
            Place.Field.ADDRESS,
            Place.Field.PHOTO_METADATAS,
            Place.Field.ICON_URL,
            Place.Field.TYPES))

        autocompleteFragment.setOnPlaceSelectedListener(object: PlaceSelectionListener {
            override fun onError(p0: Status) {
                Log.d("QAQ", "PlaceSelectionListener error$p0")
            }

            override fun onPlaceSelected(place: Place) {

                if (place.latLng != null){
                    Log.d("QAQ", "onPlaceSelected selected${place.types}")
                    selectedLocation.latitude = place.latLng?.latitude
                    selectedLocation.longitude = place.latLng?.longitude
                    selectedLocation.name = place.name


                    var markerOptions = MarkerOptions()
                    markerOptions.title(selectedLocation.name)
                    var latLng = LatLng(selectedLocation?.latitude!!, selectedLocation?.longitude!!)
                    markerOptions.position(latLng)

                    map?.addMarker(markerOptions)

                    var cameraUdpate = CameraUpdateFactory.newLatLngZoom(latLng, 15F)
                    map?.animateCamera(cameraUdpate)
                } else {
                    Log.d("QAQ", "onPlaceSelected error$place")

                }
            }

        })

    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }
}