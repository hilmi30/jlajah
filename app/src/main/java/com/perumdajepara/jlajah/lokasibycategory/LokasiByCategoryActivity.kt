package com.perumdajepara.jlajah.lokasibycategory

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.pwittchen.infinitescroll.library.InfiniteScrollListener
import com.perumdajepara.jlajah.R
import com.perumdajepara.jlajah.detaillokasi.DetailLokasiActivity
import com.perumdajepara.jlajah.model.data.Lokasi
import com.perumdajepara.jlajah.util.*
import github.frosquivel.infinitescroll.Logic.InfiniteRecyclerOnScrollListener
import kotlinx.android.synthetic.main.activity_kategori_lokasi.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import github.frosquivel.infinitescroll.Model.InfiniteScrollBuilder
import github.frosquivel.infinitescroll.Model.InfiniteScrollObject




class LokasiByCategoryActivity : AppCompatActivity(), LokasiByCategoryView {

    private val kategoriLokasiPresenter = LokasiByCategoryPresenter()
    private val dataLokasi: MutableList<Lokasi> = mutableListOf()
    private lateinit var adapterKategoriLokasi: LokasiByCategoryAdapter

    private var startPage = 1
    private var pageCount = 0
    private val perPage = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kategori_lokasi)
        onAttachView()
    }

    override fun onAttachView() {
        kategoriLokasiPresenter.onAttach(this)

        val idCategory = intent.getIntExtra(ConstantVariable.id, 1)
        val nameCategory = intent.getStringExtra(ConstantVariable.nameCategory)

        setSupportActionBar(toolbar_kategori_lokasi)
        supportActionBar?.apply {
            title = nameCategory
            setDisplayHomeAsUpEnabled(true)
        }

        val codeLang = getSharedPreferences(ConstantVariable.userPref, Context.MODE_PRIVATE)
            .getString(ConstantVariable.myLang, "in")

        adapterKategoriLokasi = LokasiByCategoryAdapter(dataLokasi) {
            startActivity<DetailLokasiActivity>(
                ConstantVariable.id to it.id.toInt()
            )
        }

        kategoriLokasiPresenter.getLokasiByCategory(this, codeLang as String, idCategory, startPage, perPage)

        rv_kategori_lokasi.apply {
            adapter = adapterKategoriLokasi
            layoutManager = LinearLayoutManager(this@LokasiByCategoryActivity)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)

                    if (!recyclerView.canScrollVertically(1) && startPage <= pageCount && pageCount != 1) {
                        startPage = startPage.plus(1)
                        kategoriLokasiPresenter.getLokasiByCategory(this@LokasiByCategoryActivity, codeLang, idCategory, startPage, perPage)
                    }
                }
            })
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
    }

    override fun hideLokasiLoading() {
        pb_kategori_lokasi.hilang()
    }

    override fun showData(items: List<Lokasi>) {
        dataLokasi.addAll(items)
        adapterKategoriLokasi.notifyDataSetChanged()
    }

    override fun onDetachView() {
        kategoriLokasiPresenter.onDetach()
        kategoriLokasiPresenter.disposable()
    }

    override fun onDestroy() {
        super.onDestroy()
        onDetachView()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
