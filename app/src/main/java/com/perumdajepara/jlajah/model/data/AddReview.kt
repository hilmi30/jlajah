package com.perumdajepara.jlajah.model.data

import com.google.gson.annotations.SerializedName

data class AddReview (
    @SerializedName("review")
    var review: String = "",

    @SerializedName("rating_star")
    var ratingStar: Int = 1,

    @SerializedName("location_id")
    var locationId: Int = 1
)