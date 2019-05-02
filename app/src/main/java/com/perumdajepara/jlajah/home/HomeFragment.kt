package com.perumdajepara.jlajah.home


import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.perumdajepara.jlajah.R
import com.perumdajepara.jlajah.carilokasi.CariLokasiActivity
import com.perumdajepara.jlajah.detaillokasi.DetailLokasiActivity
import com.perumdajepara.jlajah.lokasibycategory.LokasiByCategoryActivity
import com.perumdajepara.jlajah.model.data.Category
import com.perumdajepara.jlajah.model.data.Lokasi
import com.perumdajepara.jlajah.util.*
import kotlinx.android.synthetic.main.fragment_home.*
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast

class HomeFragment : Fragment(), HomeView {

    private val categoryData = mutableListOf<Category>()
    private val lokasiPopulerData = mutableListOf<Lokasi>()
    private lateinit var adapterKategori: KategoriAdapter
    private lateinit var adapterLokasiPopuler: LokasiPopulerAdapter
    private lateinit var rootView: View
    private val homePresenter = HomePresenter()
    private lateinit var asc: RadioButton
    private lateinit var dsc: RadioButton
    private lateinit var dkt: RadioButton
    private var filterTag = ConstantVariable.ascending

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_home, container, false)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        onAttachView()
    }

    override fun onAttachView() {
        homePresenter.onAttach(this)

        // panggil user sharedpref
        val userPref = ctx.getSharedPreferences(ConstantVariable.userPref, Context.MODE_PRIVATE)
        // get item userpref
        val lang = userPref.getString(ConstantVariable.myLang, "in")

        homePresenter.getAllCategory(lang as String, ctx)
        homePresenter.getLokasiPopuler(ctx, lang)

        // rv kategori
        adapterKategori = KategoriAdapter(categoryData) {
            startActivity<LokasiByCategoryActivity>(
                ConstantVariable.id to it.id,
                ConstantVariable.nameCategory to it.nameCategory
            )
        }

        rv_kategori.apply {
            adapter = adapterKategori
            layoutManager = GridLayoutManager(context, 4)
            addItemDecoration(ItemDecorationGrid(32))
        }

        tv_coba_lagi.onClick {
            homePresenter.getAllCategory(lang, ctx)
        }

        imgbtn_filter.onClick {
            alertFilter()
        }

        edt_cari_lokasi.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                startActivity<CariLokasiActivity>(
                    ConstantVariable.namaLokasi to edt_cari_lokasi.text.toString(),
                    ConstantVariable.filterTag to filterTag
                )
                hideKeyboard(ctx, rootView)
                return@setOnKeyListener true
            }
            false
        }

        // rv lokasi populer
        // rv_lokasi populer
        adapterLokasiPopuler = LokasiPopulerAdapter(lokasiPopulerData) {
            startActivity<DetailLokasiActivity>(
                ConstantVariable.id to it.id.toInt()
            )
        }

        rv_lokasi_populer.apply {
            adapter = adapterLokasiPopuler
            layoutManager = LinearLayoutManager(context, LinearLayout.HORIZONTAL, false)
            addItemDecoration(ItemDecorationHorizontal(32))
        }
    }

    override fun showCategoryLoading() {
        pb_kategori.terlihat()
        lyt_error_kategori.hilang()
    }

    override fun errorKategori(msg: String) {
        lyt_error_kategori.terlihat()
        tv_msg_no_koneksi.text = msg
    }

    override fun hideCategoryLoading() {
        pb_kategori.hilang()
    }

    override fun showData(data: List<Category>) {
        categoryData.clear()
        categoryData.addAll(data)
        adapterKategori.notifyDataSetChanged()
    }

    override fun errorLokasiPopuler(msg: String) {
        lyt_error_lokasi_populer.terlihat()
        tv_msg_no_koneksi_lokasi_populer.text = msg
    }

    override fun showLokasiPopulerLoading() {
        pb_lokasi_populer.terlihat()
    }

    override fun hideLokasiPopulerLoading() {
        pb_lokasi_populer.hilang()
    }

    override fun showDataLokasiPopuler(data: List<Lokasi>) {
        lokasiPopulerData.clear()
        lokasiPopulerData.addAll(data)
        adapterLokasiPopuler.notifyDataSetChanged()
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

                        check(asc.id)
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

    override fun onDetachView() {
        homePresenter.onDetach()
        homePresenter.disposable()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        onDetachView()
    }
}