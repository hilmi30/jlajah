package com.perumdajepara.jlajah.model.data

import com.google.gson.annotations.SerializedName

data class Review (
    @SerializedName("id")
    var id: Int= 1,

    @SerializedName("rating_star")
    var ratingStar: Int = 1,

    @SerializedName("review")
    var review: String = "",

    @SerializedName("location_id")
    var locationId: Int = 1,

    @SerializedName("created_at")
    var createdAt: String = "",

    @SerializedName("updated_at")
    var updatedAt: String = "",

    @SerializedName("created_by")
    var createdBy: String = "",

    @SerializedName("updated_by")
    var updatedBy: String = "",

    @SerializedName("full_name")
    var fullName: String = "",

    @SerializedName("username")
    var username: String = ""
)