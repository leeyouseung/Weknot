package com.example.weknot_android.viewmodel

import android.app.Application

import androidx.lifecycle.MutableLiveData

import com.example.weknot_android.base.viewmodel.BaseViewModel
import com.example.weknot_android.model.feed.Feed
import com.example.weknot_android.network.comm.FeedComm
import com.example.weknot_android.widget.SingleLiveEvent
import com.example.weknot_android.widget.recyclerview.adapter.FeedAdapter

class FeedViewModel(application: Application) : BaseViewModel<List<Feed>>(application) {
    private val feedComm = FeedComm()

    val feedId: MutableLiveData<Int> = MutableLiveData()

    var feedAdapter = FeedAdapter()

    val openFeedWrite: SingleLiveEvent<Unit> = SingleLiveEvent()

    fun getFeeds() {
        addDisposableLoading(feedComm.getFeeds(token), dataObserver)
    }

    fun postFeedLike() {
        addDisposable(feedComm.postFeedLike(token, feedId.value!!), baseObserver)
    }

    fun onClickWrite() {
        openFeedWrite.call()
    }

    override fun onRetrieveDataSuccess(data: List<Feed>) {
        feedAdapter.updateList(data)
    }
}