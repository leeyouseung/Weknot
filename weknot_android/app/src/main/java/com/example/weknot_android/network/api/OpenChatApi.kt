package com.example.weknot_android.network.api

import com.example.weknot_android.model.chat.OpenChatRoom
import com.example.weknot_android.model.user.User
import com.example.weknot_android.network.request.OpenChatRequest
import com.example.weknot_android.network.response.Response

import io.reactivex.Single

import retrofit2.http.*

interface OpenChatApi {

    @GET("/chattingRooms")
    fun getChattingRooms(@Header("Authorization") token: String): Single<retrofit2.Response<Response<List<OpenChatRoom>>>>

    @POST("/createChattingRoom")
    fun createChattingRoom(@Header("Authorization") token: String,
                           @Body openChatRequest: OpenChatRequest): Single<retrofit2.Response<Response<Any>>>

    @GET("/chattingRooms/{roomNumber}")
    fun getChattingRoomUsers(@Header("Authorization") token: String,
                             @Path("roomNumber") roomNumber: String): Single<retrofit2.Response<Response<List<User>>>>
}