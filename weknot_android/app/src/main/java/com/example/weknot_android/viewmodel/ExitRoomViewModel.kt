package com.example.weknot_android.viewmodel

import android.app.Application

import com.example.weknot_android.base.viewmodel.BaseViewModel
import com.example.weknot_android.widget.SingleLiveEvent

import com.google.firebase.database.*

class ExitRoomViewModel(application: Application) : BaseViewModel<Any>(application) {

    lateinit var roomKey: String

    private lateinit var databaseReference: DatabaseReference

    val exitEvent = SingleLiveEvent<Unit>()
    val backEvent = SingleLiveEvent<Unit>()

    fun onCreate() {
        databaseReference = FirebaseDatabase.getInstance().reference
                .child("groupchatrooms")
                .child(roomKey)
    }

    fun onClickExit() {
        exitEvent.call()
    }

    fun onClickBack() {
        backEvent.call()
    }

    fun onClickReport() {

        FirebaseDatabase.getInstance().reference
                .child("reports").push().setValue(roomKey)

        exitEvent.call()
    }
}