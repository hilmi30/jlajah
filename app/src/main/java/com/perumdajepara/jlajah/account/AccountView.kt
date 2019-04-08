package com.perumdajepara.jlajah.account

import com.perumdajepara.jlajah.basecontract.BaseView
import com.perumdajepara.jlajah.model.EditProfileModel

interface AccountView: BaseView {
    fun showLoading()
    fun suksesResetPass()
    fun hideLoading()
    fun error(msg: String?)
    fun suksesGantiProfil(it: EditProfileModel)
    fun showProfilLoading()
    fun hideProfilLoading()
    fun submitResetBtnIsEnabled(b: Boolean)
}