package com.perumdajepara.jlajah.model

import com.google.gson.annotations.SerializedName
import com.perumdajepara.jlajah.model.data.Lokasi
import com.perumdajepara.jlajah.model.data.LokasiImage

data class DetailLokasiModel (
    @SerializedName("status")
    var status: String = "",

    @SerializedName("statusCode")
    var statusCode: String,

    @SerializedName("data")
    var data: List<Lokasi> = listOf(),

    @SerializedName("image")
    var image: List<LokasiImage> = listOf()
)