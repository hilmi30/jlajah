package com.perumdajepara.jlajah.model

import com.google.gson.annotations.SerializedName

data class User (
    @SerializedName("username")
    var username: String = "",

    @SerializedName("email")
    var email: String = "",

    @SerializedName("gender")
    var gender: Gender = Gender(),

    @SerializedName("nomer_telp")
    var noTelp: String = "",

    @SerializedName("full_name")
    var fullName: String = "",

    @SerializedName("id")
    var id: Int = 0,

    @SerializedName("access-token")
    var accessToken: String = "",

    @SerializedName("token")
    var token: String = "",

    @SerializedName("id_user")
    var idUser: String = "",

    @SerializedName("alamat")
    var alamat: String = "",

    @SerializedName("nama_lengkap")
    var namaLengkap: String = "",

    @SerializedName("nomer_hp")
    var nomerHp: String = ""
)