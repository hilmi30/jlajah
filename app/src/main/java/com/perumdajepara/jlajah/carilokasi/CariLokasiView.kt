package com.perumdajepara.jlajah.carilokasi

import com.perumdajepara.jlajah.basecontract.BaseView
import com.perumdajepara.jlajah.model.data.Lokasi

interface CariLokasiView: BaseView {
    fun showLoading()
    fun error(msg: String)
    fun hideLoading()
    fun cekKoneksi(msg: String)
    fun showData(items: List<Lokasi>)
    fun totalPage(count: Int)
    fun resetData(items: List<Lokasi>)
}