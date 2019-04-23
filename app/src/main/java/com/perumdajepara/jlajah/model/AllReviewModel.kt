package com.perumdajepara.jlajah.model

import com.google.gson.annotations.SerializedName
import com.perumdajepara.jlajah.model.data.Review

data class AllReviewModel (
    @SerializedName("status")
    var status: String = "",

    @SerializedName("statusCode")
    var statusCode: Int = 404,

    @SerializedName("review")
    var review: List<Review> = listOf(),

    @SerializedName("pageCount")
    var pageCount: Int = 0,

    @SerializedName("totalCount")
    var totalCount: Int = 0,

    @SerializedName("page")
    var page: Int = 0,

    @SerializedName("count")
    var count: Int = 0
)