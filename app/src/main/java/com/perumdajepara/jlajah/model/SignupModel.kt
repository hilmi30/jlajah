package com.perumdajepara.jlajah.model

import com.google.gson.annotations.SerializedName
import com.perumdajepara.jlajah.model.data.User

data class SignupModel (
    @SerializedName("status_code")
    var statusCode: Int = 0,

    @SerializedName("status")
    var status: String = "",

    @SerializedName("message")
    var message: String = "",

    @SerializedName("data")
    var data: User = User()
)