package com.perumdajepara.jlajah.model

import com.google.gson.annotations.SerializedName
import com.perumdajepara.jlajah.model.data.Lokasi

data class LokasiPopulerModel (
    @SerializedName("statusCode")
    var statusCode: Int = 404,

    @SerializedName("status")
    var status: String = "",

    @SerializedName("data")
    var data: List<Lokasi> = listOf()
)

