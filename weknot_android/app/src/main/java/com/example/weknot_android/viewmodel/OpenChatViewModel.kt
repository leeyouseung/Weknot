package com.example.weknot_android.viewmodel

import android.app.Application

import androidx.lifecycle.MutableLiveData

import com.example.weknot_android.base.viewmodel.BaseViewModel
import com.example.weknot_android.model.chat.ChatRoom
import com.example.weknot_android.network.request.OpenChatRequest
import com.example.weknot_android.view.dialog.CreateRoomDialog
import com.example.weknot_android.widget.SingleLiveEvent
import com.example.weknot_android.widget.recyclerview.adapter.OpenChatAdapter

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class OpenChatViewModel(application: Application) : BaseViewModel<Any>(application) {

    var request = MutableLiveData<OpenChatRequest>()

    val openChatAdapter = OpenChatAdapter()

    val createRoomDialog = CreateRoomDialog()

    val openCreateRoom = SingleLiveEvent<Unit>()

    fun getChattingRooms() {
        isLoading.value = true

        FirebaseDatabase.getInstance().reference.child("groupchatrooms")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                        val chatRooms: ArrayList<ChatRoom> = ArrayList()
                        val keys: ArrayList<String> = ArrayList()

                        for (item in dataSnapshot.children) {
                            chatRooms.add(item.getValue(ChatRoom::class.java)!!)
                            keys.add(item.key!!)
                        }

                        openChatAdapter.updateList(chatRooms as List<ChatRoom>)
                        openChatAdapter.updateKey(keys as List<String>)

                        isLoading.value = false
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        isLoading.value = false
                    }
                })
    }

    fun onClickCreate() {
        openCreateRoom.call()
    }
}