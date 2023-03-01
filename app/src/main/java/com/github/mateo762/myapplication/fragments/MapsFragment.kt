package com.github.mateo762.myapplication.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.github.mateo762.myapplication.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    companion object {
        val TAG = MapsFragment.javaClass.simpleName
    }

    private lateinit var mMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment: SupportMapFragment? =
            fragmentManager?.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        Log.d(TAG, "onMapReady")

        mMap = googleMap

        val satellite = LatLng(46.520544, 6.567825)
        val marketOptions = MarkerOptions()
            .position(satellite)
            .title("Satellite")

        mMap.clear()
        mMap.addMarker(marketOptions)
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(satellite, 10f))
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        Toast.makeText(
            context,
            "(${marker.position.latitude}, ${marker.position.longitude})",
            Toast.LENGTH_LONG
        ).show()

        return false
    }
}