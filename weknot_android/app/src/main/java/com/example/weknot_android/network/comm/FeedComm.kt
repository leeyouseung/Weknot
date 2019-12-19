package com.example.weknot_android.network.comm

import com.example.weknot_android.base.BaseComm
import com.example.weknot_android.model.feed.Feed
import com.example.weknot_android.network.api.FeedApi

import io.reactivex.Single

import okhttp3.MultipartBody
import okhttp3.RequestBody

class FeedComm : BaseComm<FeedApi>() {

    fun postFeed(token: String, picture: MultipartBody.Part, comment: RequestBody): Single<String> {
        return api.postFeed(token, picture, comment).map(getResponseMessageFunction())
    }

    fun getFeeds(token: String): Single<List<Feed>> {
        return api.getFeeds(token).map(getResponseObjectsFunction())
    }

    fun getUserFeeds(token: String, userId: String): Single<List<Feed>> {
        return api.getUserFeeds(token, userId).map(getResponseObjectsFunction())
    }

    fun postFeedLike(token: String, feedId: Int): Single<String> {
        return api.postFeedLike(token, feedId).map(getResponseMessageFunction())
    }

    override fun type(): Class<FeedApi> {
        return FeedApi::class.java
    }
}