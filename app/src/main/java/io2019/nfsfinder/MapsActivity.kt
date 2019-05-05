package io2019.nfsfinder

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.Toast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private val LOG_TAG = "MapsActivity"
    private val DEFAULT_MAP_ZOOM = 10f

    private val FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
    private val COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION
    private val LOCATION_PERM_REQUEST_CODE = 1234

    private var mLocationPermsGranted = false
    private lateinit var mMap: GoogleMap
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        getLocationPermission()
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        Toast.makeText(this, "Map is ready", Toast.LENGTH_SHORT).show()
        mMap = googleMap

        if (mLocationPermsGranted) {
            showDeviceLocation()

            if (ActivityCompat.checkSelfPermission(this, FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return
            }

            mMap.isMyLocationEnabled = true
            mMap.uiSettings.isMyLocationButtonEnabled = false
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        mLocationPermsGranted = false

        if (requestCode == LOCATION_PERM_REQUEST_CODE) {
            if (grantResults.isNotEmpty()) {
                for (item in grantResults) {
                    if (item != PackageManager.PERMISSION_GRANTED) {
                        mLocationPermsGranted = false
                        return
                    }
                }
                mLocationPermsGranted = true
            }
        }
    }

    private fun showDeviceLocation() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        try {
            if (mLocationPermsGranted) {
                val location = mFusedLocationProviderClient.lastLocation
                location.addOnCompleteListener {task ->
                    if (task.isSuccessful) {
                        val currentLocation: Location = task.result

                        moveCamera(LatLng(currentLocation.latitude, currentLocation.longitude), DEFAULT_MAP_ZOOM)
                    } else {
                        Toast.makeText(this, "Unable to get current location", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e(LOG_TAG, "showDeviceLocation: SecurityException: " + e.message)
        }
    }

    private fun moveCamera(latLng: LatLng, zoom: Float) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))
    }

    private fun getLocationPermission() {
        val permissions = arrayOf(FINE_LOCATION, COARSE_LOCATION)

        if (ContextCompat.checkSelfPermission(this.applicationContext, FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.applicationContext, COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermsGranted = true
            } else {
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERM_REQUEST_CODE)
            }
        } else {
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERM_REQUEST_CODE)
        }
    }
}
