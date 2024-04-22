package abhishek.pathak.mapsdemo

import abhishek.pathak.mapsdemo.databinding.ActivityMapsBinding
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolygonOptions
import com.google.android.gms.maps.model.PolylineOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
        val qatar1 = LatLng(25.375019, 51.507702)
        val qatar2 = LatLng(25.376019, 51.507802)
        val qatar3 = LatLng(25.377019, 51.506702)
        val qatar4 = LatLng(25.379019, 51.508702)

        mMap.addMarker(
            MarkerOptions().position(qatar1).title("Marker in Sydney")
                .icon(vectorToBitmapDesc(this,R.drawable.baseline_home_24))
        )
        mMap.addMarker(
            MarkerOptions().position(qatar1).title("Marker in Sydney1")
                .icon(vectorToBitmapDesc(this,R.drawable.baseline_follow_the_signs_24))
        )
        mMap.addMarker(MarkerOptions().position(qatar2).title("Marker in Sydney2"))

        mMap.addCircle(CircleOptions().center(qatar1).radius(100.0).fillColor(Color.RED))
        mMap.addCircle(CircleOptions().center(qatar2).radius(500.0).strokeColor(Color.GREEN))
        mMap.addPolyline(
            PolylineOptions().add(qatar4).add(qatar2).add(qatar3).add(qatar1)
                .color(Color.BLUE).width(10.0f)
        )

        mMap.addPolygon(
            PolygonOptions().add(qatar1).add(qatar2).add(qatar3).add(qatar4).add(qatar1)
                .strokeWidth(1.0f).strokeColor(Color.WHITE).fillColor(Color.DKGRAY)
        )
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(qatar1, 15.2f))
    }
}