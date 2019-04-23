package com.perumdajepara.jlajah.ulasan

import com.perumdajepara.jlajah.basecontract.BaseView

interface UlasanView: BaseView {
    fun showLoading()
    fun hideLoading()
    fun error(msg: String)
    fun suksesReview()

}