package com.shihs.tripmood.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.shihs.tripmood.MobileNavigationDirections
import com.shihs.tripmood.databinding.FragmentScheduleDetailBinding
import com.shihs.tripmood.ext.getVmFactory
import java.text.SimpleDateFormat

class DetailFragment: Fragment(), OnMapReadyCallback {

    lateinit var binding: FragmentScheduleDetailBinding

    private var map: GoogleMap? = null

    private val viewModel by viewModels <DetailViewModel> { getVmFactory(
        DetailFragmentArgs.fromBundle(requireArguments()).selectedSchedule
    ) }

    val arg: DetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentScheduleDetailBinding.inflate(inflater, container, false)



        binding.detailMap.onCreate(savedInstanceState)
        binding.detailMap.getMapAsync(this)

        val recyclerView = binding.imageRecycler

        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)

        setupUI()
        setBtn()

        return binding.root
    }

    private fun setBtn(){
        binding.editBtn.setOnClickListener {
            findNavController().navigate(DetailFragmentDirections.actionDetailFragmentToEditScheduleFragment(
                arg.selectedSchedule, arg.selectedPosition
            ))
        }
    }

    private  fun setupUI(){
        if (arg.selectedSchedule != null){
            val selectedSchedule = arg.selectedSchedule

            binding.catalogTextView.text = selectedSchedule?.catalog
            binding.costTextView.text = selectedSchedule?.cost
            binding.noteTextView.text = selectedSchedule?.note
            binding.titleTextView.text = selectedSchedule?.title

            val fmt = SimpleDateFormat("yyyy/MM/dd EE hh:mm a").format(selectedSchedule?.time)
            binding.timeTextView.text = fmt

            if (arg.selectedSchedule?.location != null ){

                binding.addressTextView.text = selectedSchedule?.location?.address

            }
        }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        var markerOptions = MarkerOptions()
        if(arg.selectedSchedule?.location != null &&
            arg.selectedSchedule?.location?.latitude != null &&
                arg.selectedSchedule?.location?.longitude != null){

            val lat = arg.selectedSchedule?.location?.latitude
            val lng = arg.selectedSchedule?.location?.longitude
            val name = arg.selectedSchedule?.location?.name
            val position = LatLng(lat!!,lng!!)

            map?.addMarker(markerOptions
                .position(position)
                .title(name))
            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(position, 15F)
            map?.moveCamera(cameraUpdate)

            map?.uiSettings?.setCompassEnabled(true)

        }


    }

    override fun onStart() {
        super.onStart()
        binding.detailMap.onStart()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.detailMap.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        binding.detailMap.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.detailMap.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.detailMap.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.detailMap.onLowMemory()
    }



}