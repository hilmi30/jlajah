package com.perumdajepara.jlajah.model.data

import com.google.gson.annotations.SerializedName

data class Gender (
    @SerializedName("id")
    var id: Int = 1,

    @SerializedName("gender")
    var gender: String = ""
)