package com.perumdajepara.jlajah.basecontract

import android.view.View

interface BasePresenter<T: View> {
    fun onAttach(view: T)
    fun onDetach()
}