package abhishek.pathak.mapsdemo

import abhishek.pathak.mapsdemo.databinding.ActivityFetchCurrentLocationInAndroidBinding
import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationRequest
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar

class FetchCurrentLocationInAndroid : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityFetchCurrentLocationInAndroidBinding
    private var locationRequest: LocationRequest? = null
    private val fusedLocationProviderClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(applicationContext)
    }
    private var cancellationTokenSource = CancellationTokenSource()
    private var currentLocation = LatLng(0.0, 0.0)
    private var marker: Marker? = null
    private val snackBar by lazy {
        Snackbar.make(
            binding.container,
            getString(R.string.permission_request),
            Snackbar.LENGTH_INDEFINITE
        )
            .setAction(getString(R.string.okay)) {
                requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 100
                )
            }
    }

    private fun requestForPermissionAccess() {
        val permission =
            applicationContext.hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        if (permission) {
            requestForCurrentLocation()
        } else {
            requirePermissionWithRationale(
                Manifest.permission.ACCESS_FINE_LOCATION,
                100,
                snackBar
            )
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestForCurrentLocation() {
        if (applicationContext.hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
            val currentTask = fusedLocationProviderClient.getCurrentLocation(
                PRIORITY_HIGH_ACCURACY,
                cancellationTokenSource.token
            )

            currentTask.addOnCompleteListener { task: Task<Location> ->
                if (task.isSuccessful) {
                    task.result?.let {
                        currentLocation = LatLng(it.latitude, it.longitude)
                    }

                    if (this::mMap.isInitialized) {
                        marker?.position = currentLocation
                        mMap.addMarker(
                            MarkerOptions().position(currentLocation).title("Live Location")
                                .icon(
                                    vectorToBitmapDesc(
                                        this,
                                        R.drawable.baseline_my_location_24
                                    )
                                )
                        )

                        val cameraPos = CameraPosition(
                            currentLocation,
                            15.5f, 0f, 0f
                        )
                        val updatedPosCamera = CameraUpdateFactory.newCameraPosition(cameraPos)
                        mMap.animateCamera(updatedPosCamera)
                    }
                } else {
                    Snackbar.make(
                        binding.container,
                        "Failed to get location", Snackbar.LENGTH_LONG
                    )
                        .show()
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 100) {
            when {
                grantResults.isEmpty() -> Log.i("tag", "User denied permission")

                grantResults[0] == PackageManager.PERMISSION_GRANTED ->
                    Snackbar.make(binding.container, "Approved", Snackbar.LENGTH_LONG).show()
            }
        } else {
            Snackbar.make(binding.container, "Permission denied", Snackbar.LENGTH_LONG)
                .setAction("Settings") {
                    val intent = Intent()
                    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    intent.data = Uri.fromParts("package", "abhishek.pathak.mapsdemo", null)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
                .show()

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFetchCurrentLocationInAndroidBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        requestForPermissionAccess()
        registerForCallback()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        binding.btnNormal.setOnClickListener {
            mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        }
        binding.btnSettelite.setOnClickListener {
            mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
        }
        binding.btnHybrid.setOnClickListener {
            mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
        }
        binding.btnTerrain.setOnClickListener {
            mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
        }

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    private lateinit var locationCallback: LocationCallback
    private fun registerForCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    currentLocation = LatLng(location.latitude, location.longitude)
                    Toast.makeText(
                        this@FetchCurrentLocationInAndroid,
                        "Updated ${currentLocation.longitude}",
                        Toast.LENGTH_SHORT
                    ).show()
                    if (this@FetchCurrentLocationInAndroid::mMap.isInitialized) {
                        marker?.position = currentLocation
                        mMap.addMarker(
                            MarkerOptions().position(currentLocation)
                                .title("Marker in current location")
                                .icon(
                                    vectorToBitmapDesc(
                                        this@FetchCurrentLocationInAndroid,
                                        R.drawable.baseline_my_location_24
                                    )
                                )
                        )
                        val cameraPosition = CameraPosition(currentLocation, 15.5f, 0f, 0f)
                        val cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition)
                        mMap.animateCamera(cameraUpdate)
                    }
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun onResume() {
        super.onResume()
        locationRequest?.let {
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest as com.google.android.gms.location.LocationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }
    }

 /*   fun createLocationRequest() {
        locationRequest = LocationRequest.create()?.apply {
            interval = 600000 // 10 minutes in milliseconds
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }*/
}