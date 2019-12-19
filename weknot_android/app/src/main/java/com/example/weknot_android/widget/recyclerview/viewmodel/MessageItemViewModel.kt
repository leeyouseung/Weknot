package com.example.weknot_android.widget.recyclerview.viewmodel

import androidx.lifecycle.MutableLiveData

import com.example.weknot_android.base.viewmodel.BaseItemViewModel
import com.example.weknot_android.model.chat.Chat
import com.example.weknot_android.util.Constants
import com.example.weknot_android.widget.recyclerview.navigator.message.MessageItemNavigator

class MessageItemViewModel : BaseItemViewModel<Chat, MessageItemNavigator>() {

    val writer = MutableLiveData<String>()
    val photo = MutableLiveData<String>()
    val message = MutableLiveData<String>()
    val time = MutableLiveData<String>()

    override fun bind(data: Chat) {
        if (data.writer != null) {
            writer.value = data.writer!!.name
            photo.value =  Constants.MAIN_HOST + "/image/" + data.writer!!.photo
        }
        message.value = data.message
        time.value = data.timeStamp
    }

    fun openProfile() {
        getNavigator().openProfile()
    }
}