package com.perumdajepara.jlajah.model.data

import com.google.gson.annotations.SerializedName

data class Lokasi (
    @SerializedName("id")
    var id: String = "",

    @SerializedName("location_name")
    var locationName: String = "",

    @SerializedName("alamat")
    var alamat: String = "",

    @SerializedName("icon")
    var icon: String = "",

    @SerializedName("location_description")
    var desc: String = "",

    @SerializedName("nomer_tlp")
    var noTelp: String = "",

    @SerializedName("email")
    var email: String = "",

    @SerializedName("rating_score")
    var ratingScore: String = "",

    @SerializedName("rating_star")
    var ratingStar: String = "",

    @SerializedName("longitude")
    var longitude: String = "",

    @SerializedName("latitude")
    var latitude: String = ""
)