package com.perumdajepara.jlajah.login

import com.perumdajepara.jlajah.basecontract.BaseView
import com.perumdajepara.jlajah.model.LoginModel

interface LoginView: BaseView {
    fun showLoading()
    fun hideLoading()
    fun cekKoneksi()
    fun suksesLogin(it: LoginModel)
    fun usernamePasswordSalah()
    fun terjadiKesalahan()
}