package com.example.weknot_android.network.api

import com.example.weknot_android.model.user.Profile
import com.example.weknot_android.network.response.Response

import io.reactivex.Single

import retrofit2.http.*

interface UserApi {

    @GET("users/profile/{userId}")
    fun getProfile(@Header("Authorization") token: String, @Path("userId") userId: String): Single<retrofit2.Response<Response<Profile>>>
}