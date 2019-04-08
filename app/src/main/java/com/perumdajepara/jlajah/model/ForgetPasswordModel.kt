package com.perumdajepara.jlajah.model

import com.google.gson.annotations.SerializedName

data class ForgetPasswordModel (
    @SerializedName("statusCode")
    var statusCode: Int = 0,

    @SerializedName("status")
    var status: String = "",

    @SerializedName("message")
    var message: String = ""
)