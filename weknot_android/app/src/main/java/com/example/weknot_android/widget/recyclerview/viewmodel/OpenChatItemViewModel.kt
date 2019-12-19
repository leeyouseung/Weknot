package com.example.weknot_android.widget.recyclerview.viewmodel

import androidx.lifecycle.MutableLiveData

import com.example.weknot_android.R
import com.example.weknot_android.base.viewmodel.BaseItemViewModel
import com.example.weknot_android.model.chat.ChatRoom
import com.example.weknot_android.widget.recyclerview.navigator.openchat.OpenChatItemNavigator

class OpenChatItemViewModel : BaseItemViewModel<ChatRoom, OpenChatItemNavigator>() {

    val name = MutableLiveData<String>()
    val number = MutableLiveData<String>()
    val master = MutableLiveData<String>()
    val type = MutableLiveData<Int>()

    override fun bind(data: ChatRoom) {
        name.value = data.roomName
        number.value = data.roomNumber.toString() + "."
        master.value = "개설자 : " + data.masterName
        type.value = getRoomTypeDrawable(data.roomType)
    }

    private fun getRoomTypeDrawable(roomType: String?) : Int {
        return when (roomType) {
            "free" -> R.drawable.ic_room_type_free
            "game" -> R.drawable.ic_room_type_game
            "worry" -> R.drawable.ic_room_type_worry
            "friend" -> R.drawable.ic_room_type_friend
            else -> R.drawable.ic_room_type_free
        }
    }

    fun onClickItem() {
        getNavigator().openChatRoom()
    }
}