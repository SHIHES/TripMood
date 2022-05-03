package com.shihs.tripmood.plan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.shihs.tripmood.databinding.FragmentPlanMapBinding

class ShowAllLocationFragment : Fragment(), OnMapReadyCallback {

    lateinit var binding: FragmentPlanMapBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentPlanMapBinding.inflate(inflater, container, false)

        binding.theMapView.onCreate(savedInstanceState)
        binding.theMapView.getMapAsync(this)











        return binding.root
    }

    override fun onMapReady(p0: GoogleMap) {
        TODO("Not yet implemented")
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