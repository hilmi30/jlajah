package com.perumdajepara.jlajah.home

import com.perumdajepara.jlajah.basecontract.BaseView
import com.perumdajepara.jlajah.model.data.Category

interface HomeView: BaseView {
    fun showCategoryLoading()
    fun error(msg: String)
    fun hideCategoryLoading()
    fun showData(data: List<Category>)
}