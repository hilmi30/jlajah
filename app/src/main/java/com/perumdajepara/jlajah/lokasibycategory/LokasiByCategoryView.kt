package com.perumdajepara.jlajah.lokasibycategory

import com.perumdajepara.jlajah.basecontract.BaseView
import com.perumdajepara.jlajah.model.data.Lokasi

interface LokasiByCategoryView: BaseView {
    fun error(msg: String)
    fun showLokasiLoading()
    fun hideLokasiLoading()
    fun showData(items: List<Lokasi>)
    fun totalPage(count: Int)
}