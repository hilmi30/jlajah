package com.perumdajepara.jlajah.network

import com.perumdajepara.jlajah.model.LoginModel
import com.perumdajepara.jlajah.model.SignupModel
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiRepository {
    @FormUrlEncoded
    @POST("auth/signup")
    fun signup(
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("full_name") fullName: String,
        @Field("nomer_hp") nomerHp: String,
        @Field("gender_id") genderId: String
    ): Observable<SignupModel>

    @FormUrlEncoded
    @POST("auth/login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Observable<LoginModel>
}