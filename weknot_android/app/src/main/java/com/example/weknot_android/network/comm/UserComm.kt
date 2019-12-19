package com.example.weknot_android.network.comm

import com.example.weknot_android.base.BaseComm
import com.example.weknot_android.model.user.Profile
import com.example.weknot_android.network.api.UserApi

import io.reactivex.Single

class UserComm : BaseComm<UserApi>() {

    fun getProfile(token: String, userId: String): Single<Profile> {
        return api.getProfile(token, userId).map(getResponseObjectsFunction())
    }

    override fun type(): Class<UserApi> {
        return UserApi::class.java
    }
}