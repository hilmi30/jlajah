package com.perumdajepara.jlajah.util

import android.content.Context
import android.content.res.Configuration
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.perumdajepara.jlajah.R
import org.jetbrains.anko.*
import java.util.*
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import android.content.IntentSender
import com.perumdajepara.jlajah.main.MainActivity
import com.google.android.gms.location.LocationSettingsResult
import android.app.Activity
import android.util.Log
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.common.api.ResultCallbacks


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

fun displayLocationSettingRequest(context: Context) {
    val googleApiClient = GoogleApiClient.Builder(context).addApi(LocationServices.API).build()
    googleApiClient.connect()

    val locationRequest = LocationRequest.create()
    locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
//    locationRequest.interval = 1000
//    locationRequest.fastestInterval = 1000 / 2

    val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
    builder.setAlwaysShow(true)

    val result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build())
    result.setResultCallback {
        val status = it.status
        when (status.statusCode) {
            LocationSettingsStatusCodes.SUCCESS ->  Log.i("sukses", "All location settings are satisfied.")
            LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                Log.i(
                    "sukses",
                    "Location settings are not satisfied. Show the user a dialog to upgrade location settings "
                )

                try {
                    // Show the dialog by calling startResolutionForResult(), and check the result
                    // in onActivityResult().
                    status.startResolutionForResult(context as Activity, 101)
                } catch (e: IntentSender.SendIntentException) {
                    Log.i("sukses", "PendingIntent unable to execute request.")
                }

            }
            LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> Log.i(
                "sukses",
                "Location settings are inadequate, and cannot be fixed here. Dialog not created."
            )
        }
    }
}
