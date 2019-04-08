package com.perumdajepara.jlajah.login

import com.perumdajepara.jlajah.basecontract.BaseView
import com.perumdajepara.jlajah.model.LoginModel

interface LoginView: BaseView {
    fun showLoading()
    fun hideLoading()
    fun suksesLogin(it: LoginModel)
    fun error(msg: String)
    fun suksesForgetPass()
    fun showLupaPassLoading()
    fun hideLupaPassLoading()
    fun submitBtnIsEnabled(b: Boolean)
}