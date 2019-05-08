package com.perumdajepara.jlajah.bookmark

import com.perumdajepara.jlajah.basecontract.BaseView
import com.perumdajepara.jlajah.model.data.Lokasi

interface BookmarkView: BaseView {
    fun showLoading()
    fun hideLoading()
    fun showData(items: List<Lokasi>)
    fun error(msg: String)
    fun suksesDelete(msg: String)
    fun showDataKosong()
    fun hideDataKosong()

}