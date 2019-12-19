package com.example.weknot_android.viewmodel

import android.app.Application

import android.view.View

import androidx.lifecycle.MutableLiveData

import com.example.weknot_android.R
import com.example.weknot_android.base.viewmodel.BaseViewModel
import com.example.weknot_android.model.feed.Feed
import com.example.weknot_android.model.user.Profile
import com.example.weknot_android.network.client.FeedClient
import com.example.weknot_android.network.client.SocialClient
import com.example.weknot_android.network.client.UserClient
import com.example.weknot_android.network.request.FriendRequest
import com.example.weknot_android.util.Constants
import com.example.weknot_android.widget.SingleLiveEvent
import com.example.weknot_android.widget.recyclerview.adapter.FeedAdapter

import io.reactivex.observers.DisposableSingleObserver

open class ProfileViewModel(application: Application) : BaseViewModel<Profile>(application) {

    private val userComm = UserClient()
    private val feedComm = FeedClient()
    private val socialComm = SocialClient()

    var id: MutableLiveData<String> = MutableLiveData()
    val feedId: MutableLiveData<Int> = MutableLiveData()

    val addBtnVisibility: MutableLiveData<Int> = MutableLiveData()

    var userName: MutableLiveData<String> = MutableLiveData()
    var userBirth: MutableLiveData<String> = MutableLiveData()
    var userStatus: MutableLiveData<String> = MutableLiveData()
    var userIntro: MutableLiveData<String> = MutableLiveData()
    var userPoint: MutableLiveData<String> = MutableLiveData()
    var userPicture: MutableLiveData<String> = MutableLiveData()
    var userGender: MutableLiveData<Int> = MutableLiveData()
    var feedAdapter = FeedAdapter()

    val onRefreshEvent = SingleLiveEvent<Unit>()
    val openPictureEvent = SingleLiveEvent<String>()

    fun setUp() {
        getProfile()
        getProfileFeeds()
    }

    private fun getProfile() {
        addDisposableLoading(userComm.getProfile(token, id.value!!), dataObserver)
    }

    private fun getProfileFeeds() {
        val observer = object : DisposableSingleObserver<List<Feed>>() {
            override fun onSuccess(t: List<Feed>) {
                feedAdapter.updateList(t)
                isLoading.value = false
            }

            override fun onError(e: Throwable) {
                onErrorEvent.value = e
                isLoading.value = false
            }
        }

        addDisposableLoading(feedComm.getUserFeeds(token, id.value!!), observer)
    }

    fun postFeedLike() {
        addDisposable(feedComm.postFeedLike(token, feedId.value!!), baseObserver)
    }

    fun onClickAddFriend() {
        val observer = object : DisposableSingleObserver<String>() {
            override fun onSuccess(s: String) {
                onRefreshEvent.call()
                isLoading.value = false
            }

            override fun onError(e: Throwable) {
                onErrorEvent.value = e
                isLoading.value = false
            }
        }

        when (userStatus.value) {
            "친구 요청" -> addDisposable(socialComm.addFriend(token, id.value!!), observer)
            "친구 수락" -> addDisposable(socialComm.putFriend(token, userId, FriendRequest(id.value!!, "yes")), observer)
        }
    }

    fun onClickPhoto() {
        openPictureEvent.value = userPicture.value
    }

    override fun onRetrieveDataSuccess(data: Profile) {
        userName.value = data.name
        userBirth.value = data.birth
        userIntro.value = data.intro
        userPoint.value = data.point.toString() + "점"
        userPicture.value = Constants.MAIN_HOST + "/image/" + data.photo
        if (data.gender == "m") {
            userGender.value = R.drawable.man_icon
        }
        else {
            userGender.value = R.drawable.woman_icon
        }
        when (data.state) {
            0 -> userStatus.value = "친구 요청"
            10 -> userStatus.value = "친구"
            1 -> userStatus.value = "친구 수락"
            2 -> userStatus.value = "친구 요청중"
            100 -> addBtnVisibility.value = View.INVISIBLE
        }
    }
}