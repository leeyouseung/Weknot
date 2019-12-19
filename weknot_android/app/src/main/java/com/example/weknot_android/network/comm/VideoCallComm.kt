package com.example.weknot_android.network.comm

import com.example.weknot_android.base.BaseComm
import com.example.weknot_android.model.videocall.VideoCall
import com.example.weknot_android.network.api.VideoCallApi

import io.reactivex.Single

class VideoCallComm : BaseComm<VideoCallApi>() {

    fun requestCall(token: String): Single<VideoCall> {
        return api.requestCall(token).map(getResponseObjectsFunction())
    }

    fun finishRoom(token: String, channel: String): Single<String> {
        return api.finishRoom(token, channel).map(getResponseObjectsFunction())
    }

    fun addPoint(token: String, id: String): Single<String> {
        return api.addPoint(token, id).map(getResponseMessageFunction())
    }

    override fun type(): Class<VideoCallApi> {
        return VideoCallApi::class.java
    }
}