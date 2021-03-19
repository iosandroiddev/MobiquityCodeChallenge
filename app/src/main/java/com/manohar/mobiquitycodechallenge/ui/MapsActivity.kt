package com.manohar.mobiquitycodechallenge.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.TooltipCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.manohar.mobiquitycodechallenge.R
import com.manohar.mobiquitycodechallenge.model.BookmarkLocationModel
import com.manohar.mobiquitycodechallenge.utils.ConstantKeys
import com.manohar.mobiquitycodechallenge.utils.ConstantKeys.locationPermissionCode
import com.manohar.mobiquitycodechallenge.utils.hasLocationPermission
import com.manohar.mobiquitycodechallenge.utils.showErrorSnackMessage
import getCustomArrayListPreferenceAsynchronous
import kotlinx.android.synthetic.main.activity_maps.*
import kotlinx.android.synthetic.main.app_bar_home.*
import setCustomArrayListPreferenceAsynchronous
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, LocationListener {

    private var _bookMarkLocationModel: BookmarkLocationModel? = null
    private lateinit var mMap: GoogleMap
    private lateinit var _locationManager: LocationManager

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.bookmark_a_location)
        }
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        _locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        _locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
            R.id.action_bookmark -> {
                bookMarkThisLocation()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun bookMarkThisLocation() {
        _bookMarkLocationModel?.let { selectedLocation ->
            getCustomArrayListPreferenceAsynchronous(ConstantKeys.pinnedLocations)?.let { it ->
                val arrayOfLocations = ArrayList(it)
                val uniqueLocations =
                    arrayOfLocations.filter { it.locationLatitude == selectedLocation.locationLatitude }
                if (uniqueLocations.isEmpty()) {
                    arrayOfLocations.add(selectedLocation)
                    setCustomArrayListPreferenceAsynchronous(
                        ConstantKeys.pinnedLocations, arrayOfLocations
                    )
                    Handler(Looper.getMainLooper()).postDelayed({
                        finish()
                    }, 1000)
                } else {
                    mapContent.showErrorSnackMessage(
                        "This Location has been already bookmarked.",
                        applicationContext
                    )
                }
            } ?: kotlin.run {
                val arrayLocations = ArrayList<BookmarkLocationModel>()
                arrayLocations.add(selectedLocation)
                setCustomArrayListPreferenceAsynchronous(
                    ConstantKeys.pinnedLocations,
                    arrayLocations
                )
                Handler(Looper.getMainLooper()).postDelayed({
                    finish()
                }, 1000)
            }
        } ?: kotlin.run {

        }
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        if (hasLocationPermission()) {
            mMap.isMyLocationEnabled = true
            mMap.uiSettings.isZoomControlsEnabled = true
            mMap.uiSettings.isMyLocationButtonEnabled = true
            enableMarkerDrag()
        } else {
            // Ask for Location Permission
            requestLocationPermission()
        }

    }

    private fun enableMarkerDrag() {
        mMap.setOnMarkerDragListener(object : OnMarkerDragListener {
            override fun onMarkerDragStart(marker: Marker) {}
            override fun onMarkerDrag(marker: Marker) {}
            override fun onMarkerDragEnd(marker: Marker) {
                val latLng = marker.position
                val pickedAddress = getAddressFromLatLong(latLng)
                marker.title = pickedAddress
                saveLocation(latLng)
            }
        })
    }

    private fun getAddressFromLatLong(latLng: LatLng): String {
        var pickedAddress = ""
        try {
            val geocoder = Geocoder(applicationContext, Locale.getDefault())
            val address = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)[0]
            pickedAddress =
                address.getAddressLine(0) ?: address.locality + " - " + address.postalCode
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return pickedAddress
    }

    private fun saveLocation(latLng: LatLng) {
        try {
            val pickedAddress = getAddressFromLatLong(latLng)
            _bookMarkLocationModel = BookmarkLocationModel(
                pickedAddress,
                pickedAddress,
                "" + latLng.latitude,
                "" + latLng.longitude
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun requestLocationPermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ), locationPermissionCode
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if ((grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED)
        ) {
            when (requestCode) {
                locationPermissionCode -> {

                }
            }
        }
    }

    override fun onLocationChanged(location: Location) {
        val latLng = LatLng(location.latitude, location.longitude)
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 10f)
        if (this::mMap.isInitialized) {
            val locationAddress = getAddressFromLatLong(latLng)
            mMap.addMarker(MarkerOptions().position(latLng).title(locationAddress).draggable(true))
            mMap.animateCamera(cameraUpdate)
            saveLocation(latLng)
        }
        _locationManager.removeUpdates(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_bookmark, menu)
        return super.onCreateOptionsMenu(menu)
    }
}