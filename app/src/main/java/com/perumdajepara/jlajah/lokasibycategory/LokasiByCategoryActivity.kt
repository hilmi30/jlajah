package com.perumdajepara.jlajah.lokasibycategory

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.KeyEvent
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.perumdajepara.jlajah.R
import com.perumdajepara.jlajah.carilokasi.CariLokasiActivity
import com.perumdajepara.jlajah.detaillokasi.DetailLokasiActivity
import com.perumdajepara.jlajah.model.data.Lokasi
import com.perumdajepara.jlajah.util.*
import kotlinx.android.synthetic.main.activity_lokasi_by_kategori.*
import kotlinx.android.synthetic.main.fragment_home.*
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.startActivity


class LokasiByCategoryActivity : AppCompatActivity(), LokasiByCategoryView {

    private val kategoriLokasiPresenter = LokasiByCategoryPresenter()
    private val dataLokasi: MutableList<Lokasi> = mutableListOf()
    private lateinit var adapterKategoriLokasi: LokasiByCategoryAdapter

    private lateinit var asc: RadioButton
    private lateinit var dsc: RadioButton
    private lateinit var dkt: RadioButton
    private var filterTag = ConstantVariable.ascending
    private var idCategory = 0

    private var startPage = 1
    private var pageCount = 0
    private val perPage = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lokasi_by_kategori)
        onAttachView()
    }

    override fun onAttachView() {
        kategoriLokasiPresenter.onAttach(this)

        idCategory = intent.getIntExtra(ConstantVariable.id, 1)
        val nameCategory = intent.getStringExtra(ConstantVariable.nameCategory)

        setSupportActionBar(toolbar_kategori_lokasi)
        supportActionBar?.apply {
            title = nameCategory
            setDisplayHomeAsUpEnabled(true)
        }

        adapterKategoriLokasi = LokasiByCategoryAdapter(dataLokasi) {
            startActivity<DetailLokasiActivity>(
                ConstantVariable.id to it.id.toInt()
            )
        }

        kategoriLokasiPresenter.getLokasiByCategory(
            context = this,
            codeLang = getMyLang(this),
            idCategory = idCategory,
            page = startPage,
            perPage = perPage
        )

        rv_kategori_lokasi.apply {
            adapter = adapterKategoriLokasi
            layoutManager = LinearLayoutManager(this@LokasiByCategoryActivity)
            addItemDecoration(ItemDecoration(32))
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)

                    if (!recyclerView.canScrollVertically(1) && startPage <= pageCount && pageCount != 1) {
                        startPage = startPage.plus(1)
                        kategoriLokasiPresenter.getLokasiByCategory(
                            context = this@LokasiByCategoryActivity,
                            codeLang =  getMyLang(this@LokasiByCategoryActivity),
                            idCategory =  idCategory,
                            page =  startPage,
                            perPage =  perPage
                        )
                    }
                }
            })
        }

        val rootView = window.decorView.rootView
        edt_cari_lokasi_kategori.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {

                startPage = 1

                kategoriLokasiPresenter.cariLokasi(
                    context = this@LokasiByCategoryActivity,
                    key = edt_cari_lokasi_kategori.text.toString(),
                    sort = filterTag,
                    page = startPage,
                    perPage = perPage,
                    codeLanguage = getMyLang(this),
                    category = idCategory
                )

                hideKeyboard(ctx, rootView)
                return@setOnKeyListener true
            }
            false
        }
    }

    override fun totalPage(count: Int) {
        pageCount = count
    }

    override fun error(msg: String) {
        showAlert(this, msg)
    }

    override fun showLokasiLoading() {
        pb_kategori_lokasi.terlihat()
        lyt_error_lokasi.hilang()
    }

    override fun hideLokasiLoading() {
        pb_kategori_lokasi.hilang()
    }

    override fun showData(items: List<Lokasi>) {
        dataLokasi.addAll(items)
        adapterKategoriLokasi.notifyDataSetChanged()
    }

    override fun resetData(items: List<Lokasi>) {
        dataLokasi.clear()
        dataLokasi.addAll(items)
        adapterKategoriLokasi.notifyDataSetChanged()
    }

    override fun cekKoneksi(msg: String) {
        lyt_error_lokasi.terlihat()
    }

    override fun onDetachView() {
        kategoriLokasiPresenter.onDetach()
        kategoriLokasiPresenter.disposable()
    }

    override fun onDestroy() {
        super.onDestroy()
        onDetachView()
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

                startPage = 1

                if (asc.isChecked) filterTag = asc.tag.toString()
                if (dsc.isChecked) filterTag = dsc.tag.toString()
                if (dkt.isChecked) filterTag = dkt.tag.toString()

                kategoriLokasiPresenter.cariLokasi(
                    context = this@LokasiByCategoryActivity,
                    key = edt_cari_lokasi_kategori.text.toString(),
                    sort = filterTag,
                    page = startPage,
                    perPage = perPage,
                    codeLanguage = getMyLang(this@LokasiByCategoryActivity),
                    category = idCategory
                )
            }
            negativeButton(R.string.batal) {
                it.dismiss()
            }
        }.show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
