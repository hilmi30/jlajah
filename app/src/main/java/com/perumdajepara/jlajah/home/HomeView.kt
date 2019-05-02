package com.perumdajepara.jlajah.home

import com.perumdajepara.jlajah.basecontract.BaseView
import com.perumdajepara.jlajah.model.data.Category
import com.perumdajepara.jlajah.model.data.Lokasi

interface HomeView: BaseView {
    fun showCategoryLoading()
    fun errorKategori(msg: String)
    fun hideCategoryLoading()
    fun showData(data: List<Category>)
    fun errorLokasiPopuler(msg: String)
    fun showLokasiPopulerLoading()
    fun hideLokasiPopulerLoading()
    fun showDataLokasiPopuler(data: List<Lokasi>)
}