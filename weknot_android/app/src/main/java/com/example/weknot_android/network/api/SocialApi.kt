package com.example.weknot_android.network.api

import com.example.weknot_android.model.user.Friend
import com.example.weknot_android.network.request.FriendAddRequest
import com.example.weknot_android.network.request.FriendRequest
import com.example.weknot_android.network.response.Response

import io.reactivex.Single

import retrofit2.http.*

interface SocialApi {

    @GET("/friends")
    fun getFriends(@Header("Authorization") token: String): Single<retrofit2.Response<Response<List<Friend>>>>

    @PUT("/friends")
    fun putFriend(@Header("Authorization") token: String,
                  @Body friendRequest: FriendRequest): Single<retrofit2.Response<Response<Any>>>

    @POST("/friends")
    fun addFriend(@Header("Authorization") token: String,
                  @Body friendAddRequest: FriendAddRequest): Single<retrofit2.Response<Response<Any>>>
}