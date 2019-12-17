package com.example.weknot_android.viewmodel

import android.app.Application

import com.example.weknot_android.base.viewmodel.BaseViewModel
import com.example.weknot_android.model.chat.ChatRoom
import com.example.weknot_android.model.chat.OpenChatRoom
import com.example.weknot_android.model.user.FbUser
import com.example.weknot_android.model.user.User
import com.example.weknot_android.widget.SingleLiveEvent

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class CreateRoomViewModel(application: Application) : BaseViewModel<Any>(application) {

    private val fbUser = FbUser()
    val chatRoom = ChatRoom()
    private var chatRoomUid: String? = null

    var selectedPosition: Int = 0

    val createEvent = SingleLiveEvent<Unit>()
    val openChatRoom = SingleLiveEvent<String>()
    val closeEvent = SingleLiveEvent<Unit>()

    fun onClickCreate() {
        createEvent.call()
    }

    fun onClickBack() {
        closeEvent.call()
    }

    fun getUser() {
        addDisposable(repository.getUser(userId), object : DisposableSingleObserver<User>() {
            override fun onSuccess(user: User) {
                onRetrieveUserSuccess(user)
            }

            override fun onError(e: Throwable) {
                onErrorEvent.value = e
            }
        })
    }

    private fun setChatRoom(user: User) {
        chatRoom.masterName = user.name
        setRoomCount()
    }

    private fun setFbUser(user: User) {
        fbUser.name = user.name
        fbUser.uid = FirebaseAuth.getInstance().currentUser!!.uid
        fbUser.id = user.id
    }

    private fun insertFirebase() {
        chatRoomUid = FirebaseDatabase.getInstance().reference.child("groupchatrooms").push().key!!

        FirebaseDatabase.getInstance().reference.child("groupchatrooms").child(chatRoomUid!!).setValue(chatRoom)
        FirebaseDatabase.getInstance().reference.child("groupchatrooms").child(chatRoomUid!!).child("users").push().setValue(fbUser)

        openChatRoom.value = chatRoomUid
    }

    private fun insertOpenChatRoom() {
        CompositeDisposable().add(repository.insertOpenChat(OpenChatRoom(chatRoom))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { insertFirebase() },
                        { error -> onErrorEvent.value = error }))
    }

    private fun setRoomCount() {
        FirebaseDatabase.getInstance().reference.child("groupchatrooms")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        var count = 1

                        for (item in dataSnapshot.children) {
                            count = item.getValue(ChatRoom::class.java)!!.roomNumber!! + 1
                        }

                        chatRoom.roomNumber = count

                        insertOpenChatRoom()
                    }
                    override fun onCancelled(databaseError: DatabaseError) {}
                })
    }

    private fun onRetrieveUserSuccess(user: User) {
        setFbUser(user)
        setChatRoom(user)
    }
}