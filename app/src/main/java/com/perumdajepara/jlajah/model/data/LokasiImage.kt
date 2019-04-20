package com.perumdajepara.jlajah.model.data

import com.google.gson.annotations.SerializedName

data class LokasiImage (
    @SerializedName("id")
    var id: Int = 1,

    @SerializedName("image")
    var image: String = "",

    @SerializedName("location_id")
    var locationId: Int = 1
)