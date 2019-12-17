package com.example.weknot_android.viewmodel

import android.app.Application

import android.os.Bundle

import androidx.lifecycle.MutableLiveData

import com.example.weknot_android.base.viewmodel.BaseViewModel
import com.example.weknot_android.model.user.Profile
import com.example.weknot_android.model.user.User
import com.example.weknot_android.model.videocall.VideoCall
import com.example.weknot_android.network.comm.UserComm
import com.example.weknot_android.network.comm.VideoCallComm
import com.example.weknot_android.util.Constants
import com.example.weknot_android.widget.SingleLiveEvent

import io.reactivex.observers.DisposableSingleObserver

class VideoCallViewModel(application: Application) : BaseViewModel<VideoCall>(application) {

    private val videoCallComm = VideoCallComm()
    private val userComm = UserComm()

    private val channel = MutableLiveData<String>()

    val userInfo: Bundle = Bundle()

    private val otherUser: MutableLiveData<String> = MutableLiveData()

    val myPhoto = MutableLiveData<String>()
    val otherPhoto = MutableLiveData<String>()
    val otherName = MutableLiveData<String>()
    val isFinish = MutableLiveData<Boolean>(false)
    val isLike = MutableLiveData<Boolean>(false)

    val connectVideoCall: SingleLiveEvent<VideoCall> = SingleLiveEvent()
    val openProfile: SingleLiveEvent<String> = SingleLiveEvent()

    fun setUp() {
        getUser()
    }

    private fun requestCall() {
        addDisposable(videoCallComm.requestCall(token), dataObserver)
    }

    private fun getUser() {
        addDisposable(repository.getUser(userId), object : DisposableSingleObserver<User>() {
            override fun onSuccess(user: User) {
                userInfo.putString("displayName", user.name)
                userInfo.putString("email", user.phoneNumber)
                userInfo.putString("avatarURL", Constants.MAIN_HOST + "/image/" + user.photo)
                myPhoto.value = Constants.MAIN_HOST + "/image/" + user.photo
            }

            override fun onError(e: Throwable) {
                onErrorEvent.value = e
            }
        })
    }

    fun finishRoom() {
        val observer = object : DisposableSingleObserver<String>() {
            override fun onSuccess(t: String) {
                otherUser.value = t
                getOtherUser(t)
                isFinish.value = true
            }

            override fun onError(e: Throwable) {
                onErrorEvent.value = e
            }
        }

        addDisposable(videoCallComm.finishRoom(token, channel.value!!), observer)
    }

    private fun getOtherUser(id: String) {
        val observer = object : DisposableSingleObserver<Profile>() {
            override fun onSuccess(t: Profile) {
                isLoading.value = false
                otherPhoto.value = Constants.MAIN_HOST + "/image/" + t.photo
                otherName.value = t.name
            }

            override fun onError(e: Throwable) {
                isLoading.value = false
                onErrorEvent.value = e
            }
        }

        addDisposableLoading(userComm.getProfile(token, id), observer)
    }

    fun onClickMatching() {
        requestCall()
    }

    fun onClickProfile() {
        openProfile.value = otherUser.value
    }

    fun onClickLike() {
        val observer = object : DisposableSingleObserver<String>() {
            override fun onSuccess(s: String) {
                isLoading.value = false
                isLike.value = true
            }

            override fun onError(e: Throwable) {
                isLoading.value = false
                onErrorEvent.value = e
            }
        }
        addDisposable(videoCallComm.addPoint(token, otherUser.value!!), observer)
    }

    fun onClickBack() {
        isFinish.value = false
        isLike.value = false
    }

    override fun onRetrieveDataSuccess(data: VideoCall) {
        connectVideoCall.value = data
        channel.value = data.channel
    }
}