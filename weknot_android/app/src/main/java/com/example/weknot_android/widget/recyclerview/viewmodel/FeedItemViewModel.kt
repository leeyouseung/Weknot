package com.example.weknot_android.widget.recyclerview.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.weknot_android.base.viewmodel.BaseItemViewModel
import com.example.weknot_android.model.feed.Feed
import com.example.weknot_android.util.Constants
import com.example.weknot_android.widget.recyclerview.navigator.feed.FeedItemNavigator

class FeedItemViewModel : BaseItemViewModel<Feed, FeedItemNavigator>() {
    val feed = MutableLiveData<Feed>()
    val name = MutableLiveData<String>()
    val photo = MutableLiveData<String>()
    val picture = MutableLiveData<String>()
    val time = MutableLiveData<String>()
    val comment = MutableLiveData<String>()
    val isLike = MutableLiveData<Boolean>()
    val likeCount = MutableLiveData<String>()

    override fun bind(data: Feed) {
        feed.value = data
        name.value = data.name
        photo.value = Constants.MAIN_HOST + "/image/" + data.photo
        picture.value = Constants.MAIN_HOST + "/image/" +data.picture
        time.value = data.time.split("T")[0] + " " + data.time.split("T")[1].split(".")[0]
        isLike.value = data.like == 1
        likeCount.value = data.likeCount.toString() + "개"
        comment.value = data.comment
    }

    fun onClickLikeOn() {
        getNavigator().likeOnEvent()
    }

    fun onClickLikeOff() {
        getNavigator().likeOffEvent()
    }

    fun onClickProfile() {
        getNavigator().openProfile()
    }

    fun onClickPicture() {
        getNavigator().openPicture()
    }
}