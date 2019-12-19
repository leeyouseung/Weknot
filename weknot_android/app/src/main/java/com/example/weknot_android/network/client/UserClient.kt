package com.example.weknot_android.network.client

import com.example.weknot_android.base.BaseClient
import com.example.weknot_android.model.user.Profile
import com.example.weknot_android.network.api.UserApi

import io.reactivex.Single

class UserClient : BaseClient<UserApi>() {

    fun getProfile(token: String, userId: String): Single<Profile> {
        return api.getProfile(token, userId).map(getResponseObjectsFunction())
    }

    override fun type(): Class<UserApi> {
        return UserApi::class.java
    }
}