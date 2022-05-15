package com.shihs.tripmood.plan

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.RoundedCorner
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.UiSettings
import com.google.android.gms.maps.model.*
import com.shihs.tripmood.R
import com.shihs.tripmood.databinding.FragmentPlanMapBinding
import com.shihs.tripmood.databinding.ItemPlanCoworkImageBinding
import com.shihs.tripmood.dataclass.Schedule
import com.shihs.tripmood.dataclass.UserLocation
import com.shihs.tripmood.ext.getVmFactory
import com.shihs.tripmood.plan.adapter.MapScheduleAdapter
import com.shihs.tripmood.util.CatalogFilter
import com.shihs.tripmood.util.InviteFilter
import com.shihs.tripmood.util.Logger
import com.shihs.tripmood.util.MapViewType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ShowAllLocationFragment() : Fragment(), OnMapReadyCallback {


    private var map: GoogleMap? = null


    lateinit var binding: FragmentPlanMapBinding

    private val viewModel by viewModels <ShowAllLocationViewModel> { getVmFactory(ShowAllLocationFragmentArgs.fromBundle(requireArguments()).myPlan) }

    private val markerRef = mutableListOf<Marker>()

    private val arg : ShowAllLocationFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentPlanMapBinding.inflate(inflater, container, false)

        binding.theMapView.onCreate(savedInstanceState)
        binding.theMapView.getMapAsync(this)


        if(arg.mapViewType == MapViewType.MAP_SHOWALLLOCATION.value) {

            val adapter = MapScheduleAdapter(MapScheduleAdapter.OnClickListener {
                viewModel.getCLickData(it)
            }, viewModel)

            val recyclerView = binding.recyclerImage

            recyclerView.adapter = adapter
            recyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

            val linearSnapHelper = LinearSnapHelper().apply {
                attachToRecyclerView(recyclerView)
            }


            recyclerView.setOnScrollChangeListener { _, _, _, _, _ ->
                viewModel.onGalleryScrollChange(
                    recyclerView.layoutManager,
                    linearSnapHelper
                )
            }

            viewModel.snapPosition.observe(
                viewLifecycleOwner,
                Observer {
                    moveCamera(viewModel.filterLocationData.value?.get(it) ?: return@Observer)
                    markerRef[it].showInfoWindow()

                }
            )


            viewModel.filterLocationData.observe(viewLifecycleOwner) {
                it?.let {
                    createMarker(it)
                    adapter.submitList(it)
                    adapter.notifyDataSetChanged()
                }
            }

            viewModel.clickSchedule.observe(viewLifecycleOwner) {
                it?.let {
                    moveCamera(it)
                }
            }

            viewModel.reObserve.observe(viewLifecycleOwner) {
                it?.let {
                    viewModel.liveSchedules.observe(viewLifecycleOwner) {
                        it?.let { data ->
                            viewModel.filterWithLocationData(it)
                            Logger.d("reObserve ${data}")
                        }
                    }
                }
            }
        } else {

            viewModel.liveUsersLocation.observe(viewLifecycleOwner){ it?.let {
                viewModel.sortUserLocation()
                Log.d("SS","liveUsersLocation $it")
            } }

            viewModel.realCoworkUsersLocation.observe(viewLifecycleOwner){it?.let {
                Log.d("SS","realCoworkUsersLocation $it")
                map?.clear()
                createCoworkerUserLocationMarker(it)
            }}

        }

        return binding.root
    }

    private fun createCoworkerUserLocationMarker(usersLocation: List<UserLocation>) { lifecycleScope.launch(Dispatchers.IO){
        for (usersLocation in usersLocation){
            val image = Glide.with(requireContext()).asBitmap().load(usersLocation.userPhotoUrl)
                .transform(CenterCrop(), RoundedCorners(50))
                .submit(100, 100).get()

            withContext(Dispatchers.Main){
                val userLatlng = LatLng(usersLocation.lat!!,usersLocation.lng!!)
                map?.addMarker(MarkerOptions().apply {
                    title(usersLocation.userName)
                    position(userLatlng)
                    icon(BitmapDescriptorFactory.fromBitmap(image))
                })

//                moveUserCamera(userLatlng)
            }
        }
    }

    }

    private fun moveUserCamera(userLatlng: LatLng) {
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(userLatlng, 13F)
        map?.animateCamera(cameraUpdate)
    }




    private fun createMarker(schedules: List<Schedule>){

        val day1 = PolylineOptions()
        val day2 = PolylineOptions()
        val day3 = PolylineOptions()
        val day4 = PolylineOptions()
        val day5 = PolylineOptions()
        val day6 = PolylineOptions()
        val day7 = PolylineOptions()
        val other = PolylineOptions()

        for (schedule in schedules){

            val marker: Marker? = map?.addMarker(MarkerOptions().apply {

                title("${schedule.location?.name}")
                position(LatLng(schedule.location?.latitude!!, schedule.location?.longitude!!))

                when(schedule.catalog){

                    CatalogFilter.FOOD.value -> {
                        icon(BitmapDescriptorFactory.fromResource(R.drawable.dish))
                    }
                    CatalogFilter.HOTEL.value ->{
                        icon(BitmapDescriptorFactory.fromResource(R.drawable.hotel))
                    }
                    CatalogFilter.VEHICLE.value ->{
                        icon(BitmapDescriptorFactory.fromResource(R.drawable.car))
                    }
                    CatalogFilter.SHOPPING.value ->{
                        icon(BitmapDescriptorFactory.fromResource(R.drawable.online_shopping))
                    }
                    CatalogFilter.ATTRACTION.value -> {
                        icon(BitmapDescriptorFactory.fromResource(R.drawable.map))
                    }

                    else -> {
                        icon(BitmapDescriptorFactory.fromResource(R.drawable.ask))
                    }
                }
            }
            )
                when(schedule.theDay){

                    1 -> {
                        map?.addPolyline(
                            day1
                            .add(LatLng(schedule.location?.latitude!!, schedule.location?.longitude!!))
                            .color(resources.getColor(R.color.day1)))

                    }

                    2 -> {
                            map?.addPolyline(day2
                                .add(LatLng(schedule.location?.latitude!!, schedule.location?.longitude!!))
                                .color(resources.getColor(R.color.day2)))

                        }
                    3 -> {
                            map?.addPolyline(day3
                                .add(LatLng(schedule.location?.latitude!!, schedule.location?.longitude!!))
                                .color(resources.getColor(R.color.day3)))

                        }
                    4 -> {
                            map?.addPolyline(day4
                                .add(LatLng(schedule.location?.latitude!!, schedule.location?.longitude!!))
                                .color(resources.getColor(R.color.day4)))

                        }
                    5 -> {
                            map?.addPolyline(day5
                                .add(LatLng(schedule.location?.latitude!!, schedule.location?.longitude!!))
                                .color(resources.getColor(R.color.day5)))

                        }
                    6 -> {
                            map?.addPolyline(day6
                                .add(LatLng(schedule.location?.latitude!!, schedule.location?.longitude!!))
                                .color(resources.getColor(R.color.day6)))

                        }
                    7 -> {
                            map?.addPolyline(day7
                                .add(LatLng(schedule.location?.latitude!!, schedule.location?.longitude!!))
                                .color(resources.getColor(R.color.day7)))
                        }

                    else -> {
                        map?.addPolyline(other
                            .add(LatLng(schedule.location?.latitude!!, schedule.location?.longitude!!))
                            .color(resources.getColor(R.color.white)))
                    }
                }

            if (marker != null) {
                markerRef.add(marker)
            }


        }



    }

    private fun moveCamera(schedule: Schedule) {
        val lat = schedule.location?.latitude
        val lng = schedule.location?.longitude
        val position = LatLng(lat!!, lng!!)
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(position, 13F)
        map?.animateCamera(cameraUpdate)
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        this.map = googleMap
        val taiwanPosition = LatLng( 25.105497,121.597366)
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(taiwanPosition,10F)
        map?.moveCamera(cameraUpdate)

        map?.uiSettings?.apply {
            setAllGesturesEnabled(true)
            setZoomControlsEnabled(true)
        }
        map?.isMyLocationEnabled
        map?.setMyLocationEnabled(true)

    }


    override fun onStart() {
        super.onStart()
        binding.theMapView.onStart()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.theMapView.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        binding.theMapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.theMapView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.theMapView.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.theMapView.onLowMemory()
    }



}