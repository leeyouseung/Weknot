package com.example.weknot_android.network.client

import com.example.weknot_android.base.BaseClient
import com.example.weknot_android.model.user.User
import com.example.weknot_android.network.api.SignApi
import com.example.weknot_android.network.request.LoginRequest
import com.example.weknot_android.network.response.data.LoginData

import io.reactivex.Single

import okhttp3.MultipartBody
import okhttp3.RequestBody

class SignClient : BaseClient<SignApi>() {

    fun login(loginRequest: LoginRequest): Single<LoginData> {
        return api.login(loginRequest).map(getResponseObjectsFunction())
    }

    fun signUp(photo: MultipartBody.Part,
               id: RequestBody, pw: RequestBody, name: RequestBody, birth: RequestBody,
               gender: RequestBody, phoneNumber: RequestBody, intro: RequestBody): Single<String> {
        return api.signUp(photo, id, pw, name, birth, gender, phoneNumber, intro).map(getResponseMessageFunction())
    }

    fun autoLogin(token: String): Single<User> {
        return api.autoLogin(token).map(getResponseObjectsFunction())
    }

    override fun type(): Class<SignApi> {
        return SignApi::class.java
    }
}