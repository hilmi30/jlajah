package com.perumdajepara.jlajah.model

import com.google.gson.annotations.SerializedName
import com.perumdajepara.jlajah.model.data.User

data class EditProfileModel (
    @SerializedName("statusCode")
    var statusCode: Int = 0,

    @SerializedName("status")
    var status: String = "",

    @SerializedName("message")
    var message: String = "",

    @SerializedName("data")
    var data: User = User()
)