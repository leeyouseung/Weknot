package com.example.weknot_android.network.client

import com.example.weknot_android.base.BaseClient
import com.example.weknot_android.model.chat.PrivateChatRoom
import com.example.weknot_android.model.user.Friend
import com.example.weknot_android.network.api.SocialApi
import com.example.weknot_android.network.request.FriendAddRequest
import com.example.weknot_android.network.request.FriendRequest

import com.google.firebase.database.FirebaseDatabase

import io.reactivex.Single

class SocialClient : BaseClient<SocialApi>() {

    fun getFriends(token: String): Single<List<Friend>> {
        return api.getFriends(token).map(getResponseObjectsFunction())
    }

    fun putFriend(token: String, userId: String, friendRequest: FriendRequest): Single<String> {

        val chatRoomUid = FirebaseDatabase.getInstance().reference.child("privatechatrooms").push().key!!
        FirebaseDatabase.getInstance().reference.child("privatechatrooms").child(chatRoomUid).setValue(PrivateChatRoom(userId, friendRequest.friend))

        return api.putFriend(token, friendRequest).map(getResponseMessageFunction())
    }

    fun addFriend(token: String, receiver: String): Single<String> {
        return api.addFriend(token, FriendAddRequest(receiver)).map(getResponseMessageFunction())
    }

    override fun type(): Class<SocialApi> {
        return SocialApi::class.java
    }
}