package com.perumdajepara.jlajah.model.data

import com.google.gson.annotations.SerializedName

data class Category (
    @SerializedName("id")
    var id: String = "",

    @SerializedName("icon")
    var icon: String = "",

    @SerializedName("name_category")
    var nameCategory: String = ""
)