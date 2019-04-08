package com.perumdajepara.jlajah.network

import com.perumdajepara.jlajah.model.*
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiRepository {
    @FormUrlEncoded
    @POST("auth/signup")
    fun signup(
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("full_name") fullName: String,
        @Field("nomer_hp") nomerHp: String,
        @Field("gender_id") genderId: Int
    ): Observable<SignupModel>

    @FormUrlEncoded
    @POST("auth/login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Observable<LoginModel>

    @FormUrlEncoded
    @POST("auth/forgetpassword")
    fun forgetPassword(
        @Query("email") email: String,
        @Field("password") password: String
    ): Observable<ForgetPasswordModel>

    @FormUrlEncoded
    @POST("profile/resetpassword")
    fun resetPassword(
        @Query("access-token") accessToken: String,
        @Field("password_lama") passwordLama: String,
        @Field("password_baru") passwordBaru: String
    ): Observable<ResetPasswordModel>

    @FormUrlEncoded
    @POST("profile/update")
    fun editProfile(
        @Query("access-token") accessToken: String,
        @Query("id") id: Int,
        @Field("full_name") fullName: String,
        @Field("gender_id") genderId: Int,
        @Field("nomer_hp") nomerHp: String
    ): Observable<EditProfileModel>
}