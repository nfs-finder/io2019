package io2019.nfsfinder.ui.maps

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import io2019.nfsfinder.R
import io2019.nfsfinder.data.maps.RacerRepositorySingleton
import java.io.IOException
import java.util.*
import kotlin.concurrent.fixedRateTimer

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var currentLocation: LatLng

    private val LOG_TAG = "MapsActivity"
    private val DEFAULT_MAP_ZOOM = 15f
    private val MY_LOC_STR = "My location"
    private val refreshTime: Long = 3000

    private val FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
    private val COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION
    private val LOCATION_PERM_REQUEST_CODE = 1234
    private var mLocationPermsGranted = false
    private var zoom = true
    private var display = true
    private val markerList: LinkedList<Marker> = LinkedList()

    private lateinit var mMap: GoogleMap
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var mSearchText: EditText
    private lateinit var mGps: ImageView
    private lateinit var geoLocMarker: Marker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        mSearchText = findViewById(R.id.searchInput)
        mGps = findViewById(R.id.ic_gps)


        getLocationPermission()
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    init {
        val updateLocTask = fixedRateTimer(period = refreshTime) {
            Log.d("updateLocTask@MA", "updating localization")
            this@MapsActivity.deviceLocation()
            this@MapsActivity.runOnUiThread {
                run {
                    displayRacers()
                }
            }
        }

        Log.d(LOG_TAG, "Initialized cyclic tasks")
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        Toast.makeText(this, "Map is ready", Toast.LENGTH_SHORT).show()
        mMap = googleMap

        if (mLocationPermsGranted) {
            deviceLocation()

            if (ActivityCompat.checkSelfPermission(this, FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return
            }

            mMap.isMyLocationEnabled = true
            mMap.uiSettings.isMyLocationButtonEnabled = false

            mMap.setOnCameraMoveListener {
                zoom = false
                display = false
            }
            searchInit()
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

    private fun searchInit() {
        mSearchText.setOnEditorActionListener { _, actionId, _ ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_SEND -> {
                    geoLocate()
                    true
                }
                else -> false
            }
        }

        mGps.setOnClickListener {
            zoom = true
            display = true
            deviceLocation()
        }
    }

    private fun geoLocate() {
        val input = mSearchText.text.toString()
        val geocoder = Geocoder(this)
        var addresses: List<Address> = listOf()

        try {
            addresses = geocoder.getFromLocationName(input, 1)
        } catch (e: IOException) {
            Log.e(LOG_TAG, "geoLocate(): IOException: " + e.message)
        }

        if (addresses.isNotEmpty()) {
            val address = addresses[0]

            Log.d(LOG_TAG, "geoLocate(): location found: $address")

            moveCamera(LatLng(address.latitude, address.longitude), DEFAULT_MAP_ZOOM, address.getAddressLine(0))
        }
    }

    private fun deviceLocation() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        try {
            if (mLocationPermsGranted) {
                val location = mFusedLocationProviderClient.lastLocation
                location.addOnCompleteListener {task ->
                    if (task.isSuccessful) {
                        val deviceLocation: Location? = task.result

                        if (deviceLocation != null) {
                            updateDeviceLocation(deviceLocation)
                        }

                        if (display) {
                            if (zoom) {
                                moveCamera(
                                    this.currentLocation,
                                    DEFAULT_MAP_ZOOM,
                                    MY_LOC_STR
                                )
                                zoom = false
                            } else {
                                moveCamera(
                                    this.currentLocation,
                                    mMap.cameraPosition.zoom,
                                    MY_LOC_STR
                                )
                            }
                        }
                    } else {
                        Toast.makeText(this, "Unable to get current location", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e(LOG_TAG, "showDeviceLocation: SecurityException: " + e.message)
        }
    }

    private fun updateDeviceLocation(location: Location) {
        this.currentLocation = LatLng(location.latitude, location.longitude)
        RacerRepositorySingleton.getInstance().racerRepository.currentLocation = this.currentLocation
    }

    private fun moveCamera(latLng: LatLng, zoom: Float, title: String) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))

        if (title != MY_LOC_STR) {
            this.zoom = false
            val options = MarkerOptions().position(latLng).title(title)
            geoLocMarker = mMap.addMarker(options)
        }
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

    private fun displayRacers() {
        val racers = RacerRepositorySingleton.getInstance().racerRepository.racerMap
        clearMarkers()
        var racerMarker: Marker
        var markerTitle: String
        for ((_, racer) in racers) {
            markerTitle = racer.username + ", " + racer.car
            racerMarker = mMap.addMarker(MarkerOptions().position(racer.location).title(markerTitle))
            markerList.addLast(racerMarker)
        }
    }

    private fun clearMarkers() {
        for (marker in markerList) {
            marker.remove()
        }

        markerList.clear()
    }
}
