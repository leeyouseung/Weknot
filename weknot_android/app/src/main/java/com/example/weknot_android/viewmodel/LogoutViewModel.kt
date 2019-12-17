package com.example.weknot_android.viewmodel

import android.app.Application

import com.example.weknot_android.base.viewmodel.BaseViewModel
import com.example.weknot_android.widget.SingleLiveEvent

class LogoutViewModel(application: Application) : BaseViewModel<Any>(application) {

    val backEvent = SingleLiveEvent<Unit>()
    val openLogin = SingleLiveEvent<Unit>()

    fun onClickLogout() {
        token = ""
        userId = ""
        openLogin.call()
    }

    fun onClickBack() {
        backEvent.call()
    }
}