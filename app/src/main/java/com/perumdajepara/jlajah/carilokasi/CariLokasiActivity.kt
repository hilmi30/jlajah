package com.perumdajepara.jlajah.carilokasi

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.RadioButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.perumdajepara.jlajah.R
import com.perumdajepara.jlajah.detaillokasi.DetailLokasiActivity
import com.perumdajepara.jlajah.lokasibycategory.LokasiByCategoryAdapter
import com.perumdajepara.jlajah.model.data.Lokasi
import com.perumdajepara.jlajah.util.*
import kotlinx.android.synthetic.main.activity_cari_lokasi.*
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.alert

class CariLokasiActivity : AppCompatActivity(), CariLokasiView {

    private lateinit var asc: RadioButton
    private lateinit var dsc: RadioButton
    private lateinit var dkt: RadioButton
    private var filterTag = ConstantVariable.ascending
    private var namaLokasi = ""
    private lateinit var userPref: SharedPreferences
    private var myLang = ""

    private val perPage = 10
    private var page = 1
    private var pageCount = 0

    private val cariLokasiPresenter = CariLokasiPresenter()
    private val lokasiData: MutableList<Lokasi> = mutableListOf()
    private lateinit var cariLokasiAdapter: LokasiByCategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cari_lokasi)

        onAttachView()
    }

    override fun onAttachView() {
        cariLokasiPresenter.onAttach(this)

        namaLokasi = intent.getStringExtra(ConstantVariable.namaLokasi)
        filterTag = intent.getStringExtra(ConstantVariable.filterTag)
        userPref = getSharedPreferences(ConstantVariable.userPref, Context.MODE_PRIVATE)
        myLang = userPref.getString(ConstantVariable.myLang, ConstantVariable.indonesia) as String

        edt_cari_lokasi.setText(namaLokasi)

        cariLokasiAdapter = LokasiByCategoryAdapter(lokasiData) {
            startActivity<DetailLokasiActivity>(
                ConstantVariable.id to it.id.toInt()
            )
        }

        cariLokasiPresenter.cariLokasi(
            context = this,
            key = namaLokasi,
            sort = filterTag,
            page = page,
            perPage = perPage,
            codeLanguage = myLang
        )

        rv_cari_lokasi.apply {
            addItemDecoration(ItemDecoration(32))
            layoutManager = LinearLayoutManager(this@CariLokasiActivity)
            adapter = cariLokasiAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)

                    if (!recyclerView.canScrollVertically(1) && page <= pageCount && pageCount != 1) {
                        page = page.plus(1)
                        cariLokasiPresenter.cariLokasi(
                            context = this@CariLokasiActivity,
                            key = namaLokasi,
                            sort = filterTag,
                            page = page,
                            perPage = perPage,
                            codeLanguage = myLang
                        )
                    }
                }
            })
        }

        imgbtn_filter.onClick {
            alertFilter()
        }

        imgbtn_back.onClick {
            finish()
        }
    }

    override fun onDetachView() {
        cariLokasiPresenter.onDetach()
        cariLokasiPresenter.disposable()
    }

    override fun onDestroy() {
        super.onDestroy()
        onDetachView()
    }

    override fun showLoading() {
        pb_cari_lokasi.terlihat()
        lyt_error_cari_lokasi.hilang()
    }

    override fun error(msg: String) {
        showAlert(this, msg)
    }

    override fun hideLoading() {
        pb_cari_lokasi.hilang()
    }

    override fun cekKoneksi(msg: String) {
        lyt_error_cari_lokasi.terlihat()
    }

    override fun showData(items: List<Lokasi>) {
        lokasiData.addAll(items)
        cariLokasiAdapter.notifyDataSetChanged()
    }

    override fun resetData(items: List<Lokasi>) {
        lokasiData.clear()
        lokasiData.addAll(items)
        cariLokasiAdapter.notifyDataSetChanged()
    }

    override fun totalPage(count: Int) {
        pageCount = count
    }

    private fun alertFilter() {
        alert {
            isCancelable = false
            title = getString(R.string.filter)
            customView {
                verticalLayout {
                    padding = dip(16)
                    radioGroup {
                        asc = radioButton {
                            text = context.getString(R.string.ascending)
                            tag = ConstantVariable.ascending
                        }
                        dsc = radioButton {
                            text = context.getString(R.string.descending)
                            tag = ConstantVariable.descending
                        }
                        dkt = radioButton {
                            text = context.getString(R.string.terdekat)
                            tag = ConstantVariable.dekat
                        }

                        if (filterTag == ConstantVariable.ascending) check(asc.id)
                        if (filterTag == ConstantVariable.descending) check(dsc.id)
                        if (filterTag == ConstantVariable.dekat) check(dkt.id)
                    }
                }
            }
            positiveButton(R.string.pilih) {
                if (asc.isChecked) filterTag = asc.tag.toString()
                if (dsc.isChecked) filterTag = dsc.tag.toString()
                if (dkt.isChecked) filterTag = dkt.tag.toString()
            }
            negativeButton(R.string.batal) {
                it.dismiss()
            }
        }.show()
    }
}
