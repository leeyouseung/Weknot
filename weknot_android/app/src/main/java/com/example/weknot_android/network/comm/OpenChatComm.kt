package com.example.weknot_android.network.comm

import com.example.weknot_android.base.BaseComm
import com.example.weknot_android.model.chat.OpenChatRoom
import com.example.weknot_android.model.user.User
import com.example.weknot_android.network.api.OpenChatApi
import com.example.weknot_android.network.request.OpenChatRequest

import io.reactivex.Single

class OpenChatComm : BaseComm<OpenChatApi>() {

    fun getChattingRooms(token: String): Single<List<OpenChatRoom>> {
        return api.getChattingRooms(token).map(getResponseObjectsFunction())
    }

    fun createChattingRoom(token: String, openChatRequest: OpenChatRequest): Single<String> {
        return api.createChattingRoom(token, openChatRequest).map(getResponseMessageFunction())
    }

    fun getChattingRoomUsers(token: String, roomNumber: String): Single<List<User>> {
        return api.getChattingRoomUsers(token, roomNumber).map(getResponseObjectsFunction())
    }

    override fun type(): Class<OpenChatApi> {
        return OpenChatApi::class.java
    }
}