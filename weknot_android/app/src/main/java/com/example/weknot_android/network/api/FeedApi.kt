package com.example.weknot_android.network.api

import com.example.weknot_android.model.feed.Feed
import com.example.weknot_android.network.response.Response

import io.reactivex.Single

import okhttp3.MultipartBody.Part
import okhttp3.RequestBody

import retrofit2.http.*

interface FeedApi {

    @Multipart
    @POST("/feeds")
    fun postFeed(@Header("Authorization") token: String,
                  @retrofit2.http.Part picture: Part,
                  @retrofit2.http.Part("comment") name: RequestBody): Single<retrofit2.Response<Response<Any>>>

    @GET("/feeds")
    fun getFeeds(@Header("Authorization") token: String): Single<retrofit2.Response<Response<List<Feed>>>>

    @GET("/feeds/writer/{userId}")
    fun getUserFeeds(@Header("Authorization") token: String,
                     @Path("userId") userId: String): Single<retrofit2.Response<Response<List<Feed>>>>

    @POST("/feeds/{id}/like")
    fun postFeedLike(@Header("Authorization") token: String,
                     @Path("id") feedId: Int): Single<retrofit2.Response<Response<Any>>>
}