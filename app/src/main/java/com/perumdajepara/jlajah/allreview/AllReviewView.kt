package com.perumdajepara.jlajah.allreview

import com.perumdajepara.jlajah.basecontract.BaseView
import com.perumdajepara.jlajah.model.data.Review

interface AllReviewView: BaseView {
    fun showLoading()
    fun hideLoading()
    fun error(msg: String)
    fun showData(review: List<Review>)
    fun totalPage(count: Int)

}