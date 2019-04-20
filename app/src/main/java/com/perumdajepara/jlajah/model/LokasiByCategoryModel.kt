package com.perumdajepara.jlajah.model

import com.google.gson.annotations.SerializedName
import com.perumdajepara.jlajah.model.data.Lokasi

data class LokasiByCategoryModel (
    @SerializedName("status")
    var status: String = "",

    @SerializedName("statusCode")
    var statusCode: String = "",

    @SerializedName("items")
    var items: List<Lokasi> = listOf(),

    @SerializedName("pageCount")
    var pageCount: Int = 0,

    @SerializedName("totalCount")
    var totalCount: Int = 0,

    @SerializedName("page")
    var page: Int = 0,

    @SerializedName("count")
    var count: Int = 0
)