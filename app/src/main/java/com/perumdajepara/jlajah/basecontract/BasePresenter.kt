package com.perumdajepara.jlajah.basecontract

interface BasePresenter<T: BaseView> {
    fun onAttach(view: T)
    fun onDetach()
}