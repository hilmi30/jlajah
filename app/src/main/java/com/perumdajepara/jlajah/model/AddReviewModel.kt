package com.perumdajepara.jlajah.model

import com.google.gson.annotations.SerializedName
import com.perumdajepara.jlajah.model.data.AddReview

data class AddReviewModel (
    @SerializedName("status")
    var status: String,

    @SerializedName("statusCode")
    var statusCode: Int = 404,

    @SerializedName("message")
    var message: String = "",

    @SerializedName("data")
    var data: AddReview = AddReview()
)