package com.example.weknot_android.widget.recyclerview.viewmodel

import androidx.lifecycle.MutableLiveData

import com.example.weknot_android.base.viewmodel.BaseItemViewModel
import com.example.weknot_android.model.user.FbUser
import com.example.weknot_android.util.Constants
import com.example.weknot_android.widget.recyclerview.navigator.chatmember.ChatMemberItemNavigator

class ChatMemberItemViewModel : BaseItemViewModel<FbUser, ChatMemberItemNavigator>() {

    val photo = MutableLiveData<String>()
    val name = MutableLiveData<String>()

    override fun bind(data: FbUser) {
        photo.value = Constants.MAIN_HOST + "/image/" + data.photo
        name.value = data.name
    }

    fun onClickItem() {
        getNavigator().openProfile()
    }
}