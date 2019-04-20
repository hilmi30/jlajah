package com.perumdajepara.jlajah.detaillokasi

import com.perumdajepara.jlajah.basecontract.BaseView
import com.perumdajepara.jlajah.model.DetailLokasiModel

interface DetailLokasiView: BaseView {
    fun error(msg: String)
    fun showLoading()
    fun hideLoading()
    fun showData(it: DetailLokasiModel)
}