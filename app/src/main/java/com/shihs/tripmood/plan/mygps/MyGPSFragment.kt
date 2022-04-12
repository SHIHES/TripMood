package com.shihs.tripmood.plan.mygps

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.shihs.tripmood.R
import com.shihs.tripmood.databinding.FragmentPlanMygpsBinding


class MyGPSFragment : Fragment(), OnMapReadyCallback {

    private val PERMISSION_ID: Int = 1000
    lateinit var mMap: GoogleMap
    lateinit var binding: FragmentPlanMygpsBinding
    lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    lateinit var locationCallback: LocationCallback

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentPlanMygpsBinding.inflate(inflater, container, false)

        //set up google map
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.onCreate(savedInstanceState)
        mapFragment.getMapAsync(this)

        //set location provider
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        checkPermission()

        setUpLocationRequest()



        return binding.root
    }

    fun setUpLocationRequest() {
        locationRequest = LocationRequest.create().apply {
            // 每隔多久詢問一次位置millisec
            interval = 1000 * 30
            // 效能最高時，隔多久問一次位置
            fastestInterval = 1000 * 5
            // 使用GPS的電池模式
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            //回傳GPS位置資料等待時間
            maxWaitTime = 1000
        }
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                if (p0.locations.isNotEmpty()) {
                    val location = p0.lastLocation

                    Log.d(
                        "SSS",
                        "location.longitude${location?.latitude} & location.longitude${location?.longitude}"
                    )
                }
            }
        }
    }

    fun checkPermission(): Boolean {

        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            updateGPS()
            return true

        } else {
            requestPermission()
        }
        return false
    }


    @SuppressLint("MissingPermission")
    fun updateGPS() {

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            val geocoder = Geocoder(requireContext())
            val currentLocation = geocoder.getFromLocation(
                location.latitude, location.longitude, 1
            )
            binding.latiTv.text = location?.latitude.toString()
            binding.longTv.text = location?.longitude.toString()
            binding.addressTv.text = location?.accuracy.toString()

            Log.d(
                "SSS",
                "location.longitude${location?.latitude} & location.longitude${location?.longitude}"
            )
            Log.d("SSS", "currentLocation${currentLocation?.first()?.subLocality}")
        }
    }

    fun requestPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(), arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            PERMISSION_ID
        )

    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
}