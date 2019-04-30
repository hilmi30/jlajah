package com.perumdajepara.jlajah.map


import android.Manifest
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.perumdajepara.jlajah.R
import kotlinx.android.synthetic.main.fragment_map.*
import com.google.android.gms.maps.model.Circle
import com.google.maps.android.SphericalUtil
import com.nabinbhandari.android.permissions.PermissionHandler
import com.nabinbhandari.android.permissions.Permissions
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.ctx

class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.maps) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val center = LatLng(-6.59064703, 110.66732168)
        mMap.addMarker(MarkerOptions().position(center).title("Jepara"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(center))
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15F))
        mMap.addCircle(CircleOptions()
            .center(center)
            .radius(400.0)
            .strokeColor(Color.rgb(0, 136, 255))
            .fillColor(Color.argb(20, 0, 136, 255))
        )

        val places = ArrayList<Places>()
        places.add(Places("-6.59256546", "110.66612005", "Tempat A"))
        places.add(Places("-6.58212061", "110.6819129", "Tempat B"))

        places.forEach {
            val position = LatLng(it.latitude.toDouble(), it.longitude.toDouble())
            val marker = mMap.addMarker(MarkerOptions()
                .position(position)
                .title(it.name)
            )

            marker.isVisible = SphericalUtil.computeDistanceBetween(center, marker.position) < 400
        }
    }

    private fun izinLokasi() {
        Permissions.check(ctx, Manifest.permission.ACCESS_FINE_LOCATION, null, object: PermissionHandler() {
            override fun onGranted() {

            }

            override fun onDenied(context: Context?, deniedPermissions: java.util.ArrayList<String>?) {
                super.onDenied(context, deniedPermissions)
                alert {
                    title = getString(R.string.akses_ditolak)
                    message = "Diperlukan izin akses lokasi anda"
                    negativeButton(R.string.tutup) {
                        it.dismiss()
                    }
                    positiveButton("Izinkan") {
                        izinLokasi()
                    }
                }.show()
            }
        })
    }

    data class Places (
        var latitude: String,
        var longitude: String,
        var name: String
    )
}
