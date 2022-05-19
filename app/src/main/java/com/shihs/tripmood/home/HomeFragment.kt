package com.shihs.tripmood.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.location.*
import com.google.android.material.tabs.TabLayoutMediator
import com.shihs.tripmood.R
import com.shihs.tripmood.databinding.FragmentHomeBinding
import com.shihs.tripmood.dataclass.UserLocation
import com.shihs.tripmood.ext.getVmFactory
import com.shihs.tripmood.home.adapter.ViewPagerAdapter

import com.shihs.tripmood.util.HomePlanFilter
import com.shihs.tripmood.util.UserManager


class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    private val viewModel by viewModels <HomeViewModel> { getVmFactory() }
    lateinit var client: FusedLocationProviderClient

    companion object {

        var LOCATION_REQUEST_CODE = 999

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        client = LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        /** When Location permission granted, start uploading user location **/
//        if (isLocationPermissionGranted()) {
//            keepTrackingUser()
//        } else {
//            requestLocationPermission()
//        }


        binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val viewPagerAdapter = ViewPagerAdapter(childFragmentManager, lifecycle)
        val viewPager2 = binding.pager
        val tabLayout = binding.tableLayout

        viewPager2.adapter = viewPagerAdapter

        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            when (HomePlanFilter.values()[position]) {

                HomePlanFilter.INDIVIDUAL -> {
                    tab.text = getString(R.string.home_individual)
                }

                else -> {
                    tab.text = getString(R.string.home_cowork)
                }
            }
        }.attach()
    }

    private fun openAppSettingsIntent() {
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", requireActivity().packageName, null)
        )
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    private fun showDialog() {
        val builder = AlertDialog.Builder(requireActivity())

        builder.setMessage(getString(R.string.home_hint_dialog_content))
            .setTitle(getString(R.string.home_hint_dialog_title))
            .setNegativeButton(getString(R.string.home_hint_dialog_refused)) { dialog, which ->
            }
            .setPositiveButton(getString(R.string.home_hint_dialog_setting)) { dialog, which ->

                openAppSettingsIntent()
            }

        val dialog = builder.create()

        dialog.show()
    }

    private fun isLocationPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_REQUEST_CODE
        )
    }

//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//
//        if (requestCode == LOCATION_REQUEST_CODE) {
//            if (grantResults.isNotEmpty() && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
//
//                /** User refused to turn on GPS permission, show dialog  **/
//                showDialog()
//
//                if (!ActivityCompat.shouldShowRequestPermissionRationale(
//                        requireActivity(),
//                        Manifest.permission.ACCESS_FINE_LOCATION
//                    )
//                ) {
//                    /** User refused to turn on GPS permission again, show dialog  **/
//                    showDialog()
//                }
//
//            }
//
//        }
//    }

    @SuppressLint("MissingPermission")
    fun keepTrackingUser() {

        val locationRequest = LocationRequest.create().apply {
            this.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            this.interval = requireActivity().resources.getInteger(R.integer.gps_request_interval).toLong()
        }


        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                val currentLocation = result.lastLocation

                val userLocation = UserLocation(
                    userUID = UserManager.userUID,
                    userName = UserManager.userName,
                    userPhotoUrl = UserManager.userPhotoUrl,
                    lat = currentLocation.latitude,
                    lng = currentLocation.longitude
                )


                Log.d("SS", "currentLocation $currentLocation")

//                viewModel.upLoadUserLocation(userLocation)

            }
        }

//        client.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())

    }

}