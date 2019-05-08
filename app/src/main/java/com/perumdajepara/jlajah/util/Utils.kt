package com.perumdajepara.jlajah.util

import android.content.Context
import android.content.res.Configuration
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.perumdajepara.jlajah.R
import org.jetbrains.anko.*
import java.util.*

fun View.terlihat() {
    visibility = View.VISIBLE
}

fun View.hilang() {
    visibility = View.GONE
}

fun setLocale(context: Context, lang: String) {
    val locale = Locale(lang)
    Locale.setDefault(locale)
    val config = Configuration()
    config.locale = locale
    context.resources.updateConfiguration(config, context.resources.displayMetrics)

    // simpan bahasa
    context.getSharedPreferences(ConstantVariable.userPref, Context.MODE_PRIVATE).edit().apply {
        putString(ConstantVariable.myLang, lang)
        apply()
    }
}

fun showAlert(context: Context, msg: String) {
    context.alert {
        isCancelable = false
        message = msg
        negativeButton(context.getString(R.string.tutup)) {
            it.dismiss()
        }
    }.show()
}

fun hideKeyboard(context: Context, view: View) {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun getMyLang(context: Context): String {
    val userPref = context.getSharedPreferences(ConstantVariable.userPref, Context.MODE_PRIVATE)
    return userPref.getString(ConstantVariable.myLang, "in") as String
}

fun getToken(context: Context): String {
    val userPref = context.getSharedPreferences(ConstantVariable.userPref, Context.MODE_PRIVATE)
    return userPref.getString(ConstantVariable.accessToken, "") as String
}

fun getUserID(context: Context): Int {
    val userPref = context.getSharedPreferences(ConstantVariable.userPref, Context.MODE_PRIVATE)
    return userPref.getInt(ConstantVariable.id, 0)
}
