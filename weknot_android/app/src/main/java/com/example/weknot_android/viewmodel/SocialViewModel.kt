package com.example.weknot_android.viewmodel

import android.app.Application

import androidx.lifecycle.MutableLiveData

import com.example.weknot_android.base.viewmodel.BaseViewModel
import com.example.weknot_android.model.user.Friend
import com.example.weknot_android.network.client.SocialClient
import com.example.weknot_android.network.request.FriendRequest
import com.example.weknot_android.widget.recyclerview.adapter.SocialAdapter

class SocialViewModel(application: Application) : BaseViewModel<List<Friend>>(application) {

    private val socialComm = SocialClient()

    private var receiveList: ArrayList<Friend> = ArrayList()
    private var friendList: ArrayList<Friend> = ArrayList()

    var receiveAdapter: SocialAdapter = SocialAdapter()
    var friendAdapter: SocialAdapter = SocialAdapter()

    val friendRequest = MutableLiveData<FriendRequest>()

    private val RECEIVE = 0
    private val FRIEND = 1

    fun setUp() {
        getFriends()
    }

    private fun getFriends() {
        addDisposableLoading(socialComm.getFriends(token), dataObserver)
    }

    fun putFriend() {
        addDisposable(socialComm.putFriend(token, userId, friendRequest.value!!), baseObserver)
    }

    override fun onRetrieveDataSuccess(data: List<Friend>) {
        receiveList.clear()
        friendList.clear()

        for (friend in data) {
            if (friend.friendState == RECEIVE) {
                receiveList.add(friend)
            } else if (friend.friendState == FRIEND) {
                friendList.add(friend)
            }
        }
        receiveAdapter.updateList(receiveList)
        friendAdapter.updateList(friendList)
    }

    override fun onRetrieveBaseSuccess(message: String) {
        setUp()
    }
}