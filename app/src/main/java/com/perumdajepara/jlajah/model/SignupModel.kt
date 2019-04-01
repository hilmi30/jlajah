package com.perumdajepara.jlajah.model

import com.google.gson.annotations.SerializedName

data class SignupModel (
    @SerializedName("status_code")
    var statusCode: Int = 0,

    @SerializedName("status")
    var status: String = "",

    @SerializedName("message")
    var message: String = "",

    @SerializedName("data")
    var data: SignupUserData = SignupUserData()
)