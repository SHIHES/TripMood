package com.shihs.tripmood.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaMetadata
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.location.*
import com.google.android.material.tabs.TabLayoutMediator
import com.google.maps.android.SphericalUtil
import com.shihs.tripmood.R
import com.shihs.tripmood.databinding.FragmentHomeBinding
import com.shihs.tripmood.dataclass.UserLocation
import com.shihs.tripmood.ext.getVmFactory
import com.shihs.tripmood.home.adapter.ViewPagerAdapter

import com.shihs.tripmood.util.HomePlanFilter
import com.shihs.tripmood.util.UserManager


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels <HomeViewModel> { getVmFactory() }
    lateinit var client: FusedLocationProviderClient

    companion object{

        var LOCATION_REQUEST_CODE = 999

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        client = LocationServices.getFusedLocationProviderClient(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        if (isLocationPermissionGranted()){
//            getUserLocation()
            keepTrackUser()
        } else{
            requestLocationPermission()
        }


        _binding = FragmentHomeBinding.inflate(inflater, container, false)

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
                    tab.text = "獨自規劃"
                }
                else -> {
                    tab.text = "共同編輯"
                }
            }
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun openAppSettingsIntent() {
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", requireContext().getPackageName(), null)
        )
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    private fun hintDailog(){

        val builder = AlertDialog.Builder(requireContext())

        builder.setMessage("開啟GPS功能以獲得更好的體驗")
                .setTitle("小提醒")
            .setPositiveButton("OK"){dialog, which ->
            }

        val dialog = builder.create()

        dialog.show()

    }



    private fun showDialog() {
        val builder = AlertDialog.Builder(requireContext())

            builder.setMessage("開啟GPS功能以獲得更好的體驗")
                .setTitle("小提醒")
                .setNegativeButton("下次再說"){ dialog, which ->

                }
                .setPositiveButton("前往設定"){dialog, which ->

                    openAppSettingsIntent()
                }

        val dialog = builder.create()

            dialog.show()
    }

    private fun isLocationPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(requireActivity(),
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_REQUEST_CODE
        )
        // AppConstant.LOCATION_REQUEST_CODE 為自己隨意定義的 int（例如：999）
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_REQUEST_CODE){
            if (grantResults.size > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED){

                hintDailog()
                //被拒絕惹
                Toast.makeText(requireContext(), "QQQQQQQ", Toast.LENGTH_LONG).show()
                if(!ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)){
                    //被拒絕惹 + 按了never ask again
                    showDialog()
                }

            }

        }
    }

    @SuppressLint("MissingPermission")
    fun keepTrackUser() {

        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = requireContext().resources.getInteger(R.integer.gps_request_interval).toLong()

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                val currentLocation = result.lastLocation
                if (currentLocation != null) {

                    val userLocation = UserLocation(
                        userUID = UserManager.userUID,
                        userName = UserManager.userName,
                        userPhotoUrl = UserManager.userPhotoUrl,
                        lat = currentLocation.latitude,
                        lng = currentLocation.longitude
                    )

                    Log.d("SS", "currentLocation$currentLocation")

                    viewModel.upLoadUserLocation(userLocation)
                } else {
                    Log.d("SS", "Current location is null.")
                }
            }
        }

        client.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())

    }

}