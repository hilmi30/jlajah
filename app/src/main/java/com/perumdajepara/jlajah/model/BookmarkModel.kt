package com.perumdajepara.jlajah.model

import com.google.gson.annotations.SerializedName
import com.perumdajepara.jlajah.model.data.Lokasi

data class BookmarkModel (
    @SerializedName("statusCode")
    var statusCode: Int = 404,

    @SerializedName("status")
    var status: String = "",

    @SerializedName("id")
    var id: Int = 0,

    @SerializedName("user_id")
    var userId: Int = 0,

    @SerializedName("location_id")
    var locationId: Int = 0,

    @SerializedName("count")
    var count: Int = 0,

    @SerializedName("message")
    var message: String = "",

    @SerializedName("items")
    var items: List<Lokasi> = listOf()
)