package com.example.weknot_android.widget.recyclerview.viewmodel

import androidx.lifecycle.MutableLiveData

import com.example.weknot_android.base.viewmodel.BaseItemViewModel
import com.example.weknot_android.model.user.Friend
import com.example.weknot_android.util.Constants
import com.example.weknot_android.widget.recyclerview.navigator.social.SocialItemNavigator

class SocialItemViewModel : BaseItemViewModel<Friend, SocialItemNavigator>() {

    val name = MutableLiveData<String>()
    val point = MutableLiveData<String>()
    val picture = MutableLiveData<String>()
    val isFriend = MutableLiveData<Boolean>()

    override fun bind(data: Friend) {
        name.value = data.friendName
        point.value = data.friendPoint.toString() + "점"
        picture.value = Constants.MAIN_HOST + "/image/" + data.friendPhoto
        isFriend.value = data.friendState != 0
    }

    fun onClickAcceptBtn() {
        getNavigator().acceptFriend()
    }

    fun onClickRejectBtn() {
        getNavigator().rejectFriend()
    }

    fun onClickMessage() {
        getNavigator().openChatRoom()
    }

    fun onClickItem() {
        getNavigator().openProfile()
    }
}