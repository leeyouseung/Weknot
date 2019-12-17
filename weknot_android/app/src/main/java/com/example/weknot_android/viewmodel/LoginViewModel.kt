package com.example.weknot_android.viewmodel

import android.app.Application
import android.util.Log
import com.example.weknot_android.base.viewmodel.BaseViewModel
import com.example.weknot_android.model.user.User
import com.example.weknot_android.network.comm.SignComm
import com.example.weknot_android.network.request.LoginRequest
import com.example.weknot_android.network.response.data.LoginData
import com.example.weknot_android.widget.SingleLiveEvent
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class LoginViewModel(application: Application) : BaseViewModel<LoginData>(application) {
    private val signComm = SignComm()

    var request = LoginRequest()

    val loginEvent: SingleLiveEvent<Unit> = SingleLiveEvent()
    val openSignUp: SingleLiveEvent<Unit> = SingleLiveEvent()
    val onSuccessEvent: SingleLiveEvent<Unit> = SingleLiveEvent()

    fun login() {
        addDisposable(signComm.login(request), dataObserver)
    }

    private fun fbLogin() {
        FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(request.id + "@ryan.com", request.password + "111111")
                .addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.e("LOGIN Error", task.exception!!.message!!)
                        return@addOnCompleteListener
                    }
                    onSuccessEvent.call()
                }
    }

    private fun insertLoginData(loginData: LoginData) {
        insertToken(loginData.token)
        insertId(loginData.user.id)
        insertUser(loginData.user)
    }

    private fun insertToken(token: String) {
        this.token = token
    }

    private fun insertUser(user: User) {
        CompositeDisposable().add(repository.insertUser(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { fbLogin() },
                        { error -> onErrorEvent.value = error }))
    }

    private fun insertId(id: String) {
        userId = id
    }

    fun onClickLogin() {
        loginEvent.call()
    }

    fun onClickSignUp() {
        openSignUp.call()
    }

    override fun onRetrieveDataSuccess(data: LoginData) {
        insertLoginData(data)
    }
}