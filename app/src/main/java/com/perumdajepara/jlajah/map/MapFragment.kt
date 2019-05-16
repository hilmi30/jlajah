package com.perumdajepara.jlajah.map


import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.IntentSender
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.LocationManager
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.perumdajepara.jlajah.R
import kotlinx.android.synthetic.main.fragment_map.*
import com.google.maps.android.SphericalUtil
import com.nabinbhandari.android.permissions.PermissionHandler
import com.nabinbhandari.android.permissions.Permissions
import com.perumdajepara.jlajah.model.data.Category
import com.perumdajepara.jlajah.model.data.Lokasi
import com.perumdajepara.jlajah.util.ConstantVariable
import com.perumdajepara.jlajah.util.ItemDecoration
import com.perumdajepara.jlajah.util.displayLocationSettingRequest
import com.perumdajepara.jlajah.util.getMyLang
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.toast
import java.security.Permission
import java.util.ArrayList

class MapFragment : Fragment(), OnMapReadyCallback, MapView {

    private lateinit var mMap: GoogleMap
    private val mapPresenter = MapPresenter()
    private lateinit var alertLoading: DialogInterface
    private lateinit var userPref: SharedPreferences

    private lateinit var kategoriMarkerAdapter: KategoriMarkerAdapter
    private var kategoriMarkerData: MutableList<Category> = mutableListOf()
    private var lokasiMarkerData: MutableList<Lokasi> = mutableListOf()
    private lateinit var alertInterface: DialogInterface

    private var radius: Float = 500F
    private lateinit var edtRadius: EditText
    private var myPos = LatLng(-6.5906502, 110.6673202)

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationManager: LocationManager

    private val permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private var fragHidden = true
    private var gpsEnabled = false

    private var idCategory = 0

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

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(ctx)
        locationManager = ctx.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        userPref = ctx.getSharedPreferences(ConstantVariable.userPref, Context.MODE_PRIVATE)
        radius = userPref.getFloat(ConstantVariable.radius, 500F)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        onAttachView()
    }

    @SuppressLint("SetTextI18n")
    override fun onAttachView() {
        mapPresenter.onAttach(this)

        setItemMap(myPos.latitude, myPos.longitude)

        tv_radius.text = "Radius: ${radius.toInt()} m"

        mapPresenter.getAllCategory(
            codeLang = getMyLang(ctx),
            context = ctx
        )

        kategoriMarkerAdapter = KategoriMarkerAdapter(kategoriMarkerData) {
            alertInterface.dismiss()
            tv_kategori.text = it.nameCategory
            idCategory = it.id.toInt()

            mapPresenter.getLokasiByCategoryAndRadius(
                context = ctx,
                codeLang = getMyLang(ctx),
                idCategory = it.id.toInt()
            )
        }

        tv_kategori.onClick {
            alertKategori()
        }

        tv_radius.onClick {
            alertRadius()
        }

        fbtn_my_location.onClick {
            setCurrentLocation()
        }
    }

    private fun setCurrentLocation() {
        if (ContextCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            mMap.isMyLocationEnabled = true
            gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

            if (gpsEnabled && fragHidden) {
                fusedLocationClient.lastLocation.addOnSuccessListener {
                    if (it != null) {
                        setItemMap(it.latitude, it.longitude)
                        mapPresenter.getLokasiByCategoryAndRadius(
                            context = ctx,
                            codeLang = getMyLang(ctx),
                            idCategory = idCategory
                        )
                    }
                }
            } else {
                alert {
                    isCancelable = false
                    title = getString(R.string.lokasi_tidak_aktif)
                    message = getString(R.string.perangkat_belum_terdeteksi_lokasi_aktif)
                    negativeButton(R.string.tutup) {
                        it.dismiss()
                    }
                    positiveButton(getString(R.string.aktifkan)) {
                        Permissions.check(context, permissions, null, null, object : PermissionHandler() {
                            override fun onGranted() {
                                displayLocationSettingRequest(context as Context)
                            }
                        })
                    }
                }.show()
            }
        } else {
            alert {
                isCancelable = false
                title = getString(R.string.akses_ditolak)
                message = getString(R.string.diperlukan_izin_lokasi)
                negativeButton(R.string.tutup) {
                    it.dismiss()
                }
                positiveButton(getString(R.string.izinkan)) {
                    Permissions.check(context, permissions, null, null, object : PermissionHandler() {
                        override fun onGranted() {
                            setCurrentLocation()
                        }
                    })
                }
            }.show()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun alertRadius() {
        alertInterface = alert {
            isCancelable = false
            title = getString(R.string.masukan_nilai_radius)
            customView {
                verticalLayout {
                    padding = dip(16)

                    edtRadius = editText {
                        setText(radius.toInt().toString())
                        hint = getString(R.string.nilai_radius)
                        inputType = InputType.TYPE_CLASS_NUMBER
                    }
                }
                negativeButton(R.string.batal) {
                    it.dismiss()
                }
                positiveButton(R.string.submit) {
                    if (radius < ConstantVariable.limitRadius) {
                        radius = edtRadius.text.toString().toFloat()
                        // simpan dalam sharedpref
                        userPref.edit().putFloat(ConstantVariable.radius, radius).apply()

                        tv_radius.text = "Radius: ${radius.toInt()} m"

                        mMap.clear()
                        setItemMap(myPos.latitude, myPos.longitude)
                        loopMarker()
                    } else {
                        toast(getString(R.string.anda_melebihi_batas_radius))
                    }
                }
            }
        }.show()
    }

    private fun setItemMap(lat: Double, lng: Double) {
        myPos = LatLng(lat, lng)
        mMap.clear()
        mMap.uiSettings.isMyLocationButtonEnabled = false
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myPos, 14.5f))
        mMap.addCircle(CircleOptions()
            .center(myPos)
            .radius(radius.toDouble())
            .strokeColor(Color.rgb(0, 136, 255))
            .fillColor(Color.argb(20, 0, 136, 255))
        )
    }

    override fun showLoading() {
        alertLoading()
    }

    override fun showMarker(items: List<Lokasi>) {
        mMap.clear()
        setItemMap(myPos.latitude, myPos.longitude)
        lokasiMarkerData.clear()
        lokasiMarkerData.addAll(items)

        loopMarker()
    }

    private fun loopMarker() {
        lokasiMarkerData.forEach {
            val position = LatLng(it.latitude.toDouble(), it.longitude.toDouble())
            val marker = mMap.addMarker(MarkerOptions()
                .position(position)
                .title(it.locationName)
                .snippet(it.alamat)
            )

            marker.isVisible = SphericalUtil.computeDistanceBetween(myPos, marker.position) < radius
        }
    }

    override fun error(msg: String) {
        alertError(msg)
        mMap.clear()
    }

    override fun hideLoading() {
        alertLoading.dismiss()
    }

    private fun alertLoading() {
        alertLoading = alert {
            isCancelable = false
            customView {
                verticalLayout {
                    horizontalProgressBar {
                        isIndeterminate = true
                        padding = dip(32)
                    }
                }
            }
        }.show()
    }

    private fun alertError(msg: String) {
        alert {
            isCancelable = false
            message = msg
            positiveButton(R.string.coba_lagi) {
                mapPresenter.getAllCategory(
                    codeLang = getMyLang(ctx),
                    context = ctx
                )
            }
        }.show()
    }

    override fun showKategori(data: List<Category>) {
        kategoriMarkerData.clear()
        kategoriMarkerData.addAll(data)
        kategoriMarkerAdapter.notifyDataSetChanged()
        tv_kategori.text = kategoriMarkerData[0].nameCategory

        idCategory = kategoriMarkerData[0].id.toInt()

        setCurrentLocation()
    }

    private fun alertKategori() {
        alertInterface = alert {
            isCancelable = false
            title = getString(R.string.pilih_kategori)
            customView {
                verticalLayout {
                    recyclerView {
                        adapter = kategoriMarkerAdapter
                        layoutManager = LinearLayoutManager(ctx)
                        addItemDecoration(ItemDecoration(32))
                    }
                }
            }
            negativeButton(R.string.batal) {
                it.dismiss()
            }
        }.show()
    }

    override fun onDetachView() {
        mapPresenter.onDetach()
        mapPresenter.disposable()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        onDetachView()
    }
}
