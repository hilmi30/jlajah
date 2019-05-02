package com.perumdajepara.jlajah.map


import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.perumdajepara.jlajah.R
import kotlinx.android.synthetic.main.fragment_map.*
import com.google.maps.android.SphericalUtil
import com.nabinbhandari.android.permissions.PermissionHandler
import com.nabinbhandari.android.permissions.Permissions
import com.perumdajepara.jlajah.model.data.Category
import com.perumdajepara.jlajah.model.data.Lokasi
import com.perumdajepara.jlajah.util.ConstantVariable
import com.perumdajepara.jlajah.util.ItemDecoration
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.toast

class MapFragment : Fragment(), OnMapReadyCallback, MapView {

    private lateinit var mMap: GoogleMap
    private val mapPresenter = MapPresenter()
    private lateinit var alertLoading: DialogInterface
    private lateinit var userPref: SharedPreferences
    private lateinit var myLang: String

    private lateinit var kategoriMarkerAdapter: KategoriMarkerAdapter
    private var kategoriMarkerData: MutableList<Category> = mutableListOf()
    private var lokasiMarkerData: MutableList<Lokasi> = mutableListOf()
    private lateinit var alertInterface: DialogInterface

    private var radius = 300.0
    private lateinit var edtRadius: EditText

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

        izinLokasi()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        onAttachView()
    }

    @SuppressLint("SetTextI18n")
    override fun onAttachView() {
        mapPresenter.onAttach(this)

        setItemMap()
        tv_radius.text = "Radius: $radius"

        userPref = ctx.getSharedPreferences(ConstantVariable.userPref, Context.MODE_PRIVATE)
        myLang = userPref.getString(ConstantVariable.myLang, ConstantVariable.indonesia) as String

        mapPresenter.getAllCategory(
            codeLang = myLang,
            context = ctx
        )

        kategoriMarkerAdapter = KategoriMarkerAdapter(kategoriMarkerData) {
            alertInterface.dismiss()
            tv_kategori.text = it.nameCategory

            mapPresenter.getRadiusByCategory(
                context = ctx,
                codeLang = myLang,
                idCategory = it.id.toInt()
            )
        }

        fbtn_category.onClick {
            alertKategori()
        }

        fbtn_radius.onClick {
            alertRadius()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun alertRadius() {
        alertInterface = alert {
            isCancelable = false
            title = "Masukkan Nilai Radius"
            customView {
                verticalLayout {
                    padding = dip(16)

                    edtRadius = editText {
                        setText(radius.toString())
                        hint = "Nilai radius"
                        inputType = InputType.TYPE_CLASS_NUMBER
                    }
                }
                negativeButton(R.string.batal) {
                    it.dismiss()
                }
                positiveButton(R.string.submit) {
                    if (radius < 10000.0) {
                        radius = edtRadius.text.toString().toDouble()
                        tv_radius.text = "Radius: $radius"
                        mMap.clear()
                        setItemMap()
                        loopMarker()
                    } else {
                        toast("Anda melebihi batas radius")
                    }
                }
            }
        }.show()
    }

    private fun setItemMap() {
        val center = LatLng(-6.59064703, 110.66732168)
        mMap.addMarker(MarkerOptions().position(center).title("Jepara"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(center))
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15F))
        mMap.addCircle(CircleOptions()
            .center(center)
            .radius(radius)
            .strokeColor(Color.rgb(0, 136, 255))
            .fillColor(Color.argb(20, 0, 136, 255))
        )
    }

    override fun showLoading() {
        alertLoading()
    }

    override fun showMarker(items: List<Lokasi>) {
        mMap.clear()
        setItemMap()
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
            )

            val center = LatLng(-6.59064703, 110.66732168)
            marker.isVisible = SphericalUtil.computeDistanceBetween(center, marker.position) < radius
        }
    }

    override fun error(msg: String) {
        alertError(msg)
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
                    codeLang = myLang,
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
        mapPresenter.getRadiusByCategory(
            context = ctx,
            codeLang = myLang,
            idCategory = kategoriMarkerData[0].id.toInt()
        )
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
        onDestroyView()
    }

    private fun izinLokasi() {
        Permissions.check(ctx, Manifest.permission.ACCESS_FINE_LOCATION, null, object: PermissionHandler() {
            override fun onGranted() {
                //
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
}
