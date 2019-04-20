package com.perumdajepara.jlajah.model

import com.google.gson.annotations.SerializedName
import com.perumdajepara.jlajah.model.data.Category

data class CategoryModel (
    @SerializedName("status")
    var status: String = "",

    @SerializedName("statusResponse")
    var statusResponse: Int = 0,

    @SerializedName("data")
    var data: List<Category> = listOf()
)