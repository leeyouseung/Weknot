package com.example.weknot_android.network.api

import com.example.weknot_android.model.user.User
import com.example.weknot_android.network.request.LoginRequest
import com.example.weknot_android.network.response.Response
import com.example.weknot_android.network.response.data.LoginData

import io.reactivex.Single

import okhttp3.MultipartBody
import okhttp3.RequestBody

import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST

interface SignApi {

    @POST("/auth/login")
    fun login(@Body loginRequest: LoginRequest): Single<retrofit2.Response<Response<LoginData>>>

    @Multipart
    @POST("/auth/register")
    fun signUp(@retrofit2.http.Part photo: MultipartBody.Part,
               @retrofit2.http.Part("id") id: RequestBody,
               @retrofit2.http.Part("password") password: RequestBody,
               @retrofit2.http.Part("name") name: RequestBody,
               @retrofit2.http.Part("birth") birth: RequestBody,
               @retrofit2.http.Part("gender") gender: RequestBody,
               @retrofit2.http.Part("phoneNumber") phoneNumber: RequestBody,
               @retrofit2.http.Part("intro") intro: RequestBody): Single<retrofit2.Response<Response<Any>>>

    @POST("/auth/autoLogin")
    fun autoLogin(@Header("Authorization") token: String): Single<retrofit2.Response<Response<User>>>
}