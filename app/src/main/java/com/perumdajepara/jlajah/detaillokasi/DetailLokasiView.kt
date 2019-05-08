package com.perumdajepara.jlajah.detaillokasi

import com.perumdajepara.jlajah.basecontract.BaseView
import com.perumdajepara.jlajah.model.DetailLokasiModel
import com.perumdajepara.jlajah.model.data.Review

interface DetailLokasiView: BaseView {
    fun error(msg: String)
    fun showLoading()
    fun hideLoading()
    fun showData(it: DetailLokasiModel)
    fun showReviewLoading()
    fun hideReviewLoading()
    fun showReview(data: Review)
    fun hideReview()
    fun suksesDelete()
    fun cekBookmark(count: Int)
    fun suksesBookmark()
}