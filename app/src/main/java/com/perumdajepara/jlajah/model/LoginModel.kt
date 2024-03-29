package com.perumdajepara.jlajah.model

import com.google.gson.annotations.SerializedName
import com.perumdajepara.jlajah.model.data.Gender

data class LoginModel(
    @SerializedName("status")
    var status: String = "",

    @SerializedName("id")
    var id: Int = 0,

    @SerializedName("username")
    var username: String = "",

    @SerializedName("email")
    var email: String = "",

    @SerializedName("access_token")
    var accessToken: String = "",

    @SerializedName("full_name")
    var fullName: String = "",

    @SerializedName("gender")
    var gender: Gender = Gender(),

    @SerializedName("alamat")
    var alamat: String = "",

    @SerializedName("nomer_hp")
    var nomerHp: String = ""
)