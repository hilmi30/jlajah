package com.perumdajepara.jlajah.map

import com.perumdajepara.jlajah.basecontract.BaseView
import com.perumdajepara.jlajah.model.data.Category
import com.perumdajepara.jlajah.model.data.Lokasi

interface MapView: BaseView {
    fun showLoading()
    fun showMarker(items: List<Lokasi>)
    fun error(msg: String)
    fun hideLoading()
    fun showKategori(data: List<Category>)

}