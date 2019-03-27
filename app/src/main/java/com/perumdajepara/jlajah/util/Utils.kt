package com.perumdajepara.jlajah.util

import android.view.View

fun View.terlihat() {
    visibility = View.VISIBLE
}

fun View.takterlihat() {
    visibility = View.INVISIBLE
}

fun View.hilang() {
    visibility = View.GONE
}
