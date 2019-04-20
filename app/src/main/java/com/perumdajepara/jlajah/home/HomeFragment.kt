package com.perumdajepara.jlajah.home


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.perumdajepara.jlajah.R
import com.perumdajepara.jlajah.detaillokasi.DetailLokasiActivity
import com.perumdajepara.jlajah.lokasibycategory.LokasiByCategoryActivity
import com.perumdajepara.jlajah.model.data.Category
import com.perumdajepara.jlajah.util.ConstantVariable
import com.perumdajepara.jlajah.util.hilang
import com.perumdajepara.jlajah.util.showAlert
import com.perumdajepara.jlajah.util.terlihat
import kotlinx.android.synthetic.main.fragment_home.*
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.startActivity

class HomeFragment : Fragment(), HomeView {

    private val categoryData = mutableListOf<Category>()
    private lateinit var adapterKategori: KategoriAdapter
    private val homePresenter = HomePresenter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        onAttachView()
    }

    override fun showCategoryLoading() {
        pb_kategori.terlihat()
    }

    override fun error(msg: String) {
        showAlert(ctx, msg)
    }

    override fun hideCategoryLoading() {
        pb_kategori.hilang()
    }

    override fun showData(data: List<Category>) {
        categoryData.clear()
        categoryData.addAll(data)
        adapterKategori.notifyDataSetChanged()
    }

    override fun onAttachView() {
        homePresenter.onAttach(this)

        // panggil user sharedpref
        val userPref = ctx.getSharedPreferences(ConstantVariable.userPref, Context.MODE_PRIVATE)
        // get item userpref
        val lang = userPref.getString(ConstantVariable.myLang, "in")

        homePresenter.getAllCategory(lang as String, ctx)

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
        }

        // rv lokasi populer
        val lokasiPopulerData = mutableListOf<LokasiPopulerModel>()
        for (i in 1..10) {
            lokasiPopulerData.add(LokasiPopulerModel(R.drawable.ic_launcher_background, "Testing", "Testing alamat",
                4.0F, 4.0))
        }

        val adapterLokasiPopuler = LokasiPopulerAdapter(lokasiPopulerData) {
            startActivity<DetailLokasiActivity>()
        }

        rv_lokasi_populer.apply {
            adapter = adapterLokasiPopuler
            layoutManager = LinearLayoutManager(context, LinearLayout.HORIZONTAL, false)
        }
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
