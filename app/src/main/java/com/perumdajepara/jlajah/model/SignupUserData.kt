package com.perumdajepara.jlajah.model

import com.google.gson.annotations.SerializedName

data class SignupUserData (
    @SerializedName("username")
    var username: String = "",

    @SerializedName("email")
    var email: String = "",

    @SerializedName("gender")
    var gender: SignupGenderData = SignupGenderData(),

    @SerializedName("nomer_telp")
    var noTelp: String = "",

    @SerializedName("full_name")
    var fullName: String = "",

    @SerializedName("id")
    var id: Int = 0,

    @SerializedName("access-token")
    var accessToken: String = ""
)