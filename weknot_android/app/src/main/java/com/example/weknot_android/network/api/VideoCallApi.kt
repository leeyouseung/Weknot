package com.example.weknot_android.network.api

import com.example.weknot_android.model.videocall.VideoCall
import com.example.weknot_android.network.response.Response

import io.reactivex.Single

import retrofit2.http.*

interface VideoCallApi {

    @POST("/matching")
    fun requestCall(@Header("Authorization") token: String): Single<retrofit2.Response<Response<VideoCall>>>

    @POST("/matching/{channel}")
    fun finishRoom(@Header("Authorization") token: String,
                   @Path("channel") channel: String): Single<retrofit2.Response<Response<String>>>

    @POST("/matching/addPoint/{id}")
    fun addPoint(@Header("Authorization") token: String,
                 @Path("id") id: String): Single<retrofit2.Response<Response<Any>>>
}