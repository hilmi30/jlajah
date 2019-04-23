package com.perumdajepara.jlajah.model

import com.google.gson.annotations.SerializedName
import com.perumdajepara.jlajah.model.data.Review

data class ReviewModel (
    @SerializedName("statusCode")
    var statusCode: Int = 404,

    @SerializedName("message")
    var message: String,

    @SerializedName("status")
    var status: Int = 0,

    @SerializedName("data")
    var data: Review = Review()
)