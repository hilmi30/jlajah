package com.perumdajepara.jlajah.signup

import com.perumdajepara.jlajah.basecontract.BaseView

interface SignupView: BaseView {
    fun showLoading()
    fun hideLoading()
    fun suksesRegister()
    fun error(msg: String)
}