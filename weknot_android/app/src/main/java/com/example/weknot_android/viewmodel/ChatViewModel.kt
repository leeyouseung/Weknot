package com.example.weknot_android.viewmodel

import android.app.Application

import androidx.lifecycle.MutableLiveData

import com.example.weknot_android.R
import com.example.weknot_android.base.viewmodel.BaseViewModel
import com.example.weknot_android.model.chat.Chat
import com.example.weknot_android.model.user.FbUser
import com.example.weknot_android.model.user.User
import com.example.weknot_android.widget.SingleLiveEvent
import com.example.weknot_android.widget.recyclerview.adapter.ChatMemberAdapter
import com.example.weknot_android.widget.recyclerview.adapter.MessageAdapter

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

import io.reactivex.observers.DisposableSingleObserver

import java.text.SimpleDateFormat

import java.util.*

import kotlin.collections.ArrayList

class ChatViewModel(application: Application) : BaseViewModel<Any>(application) {

    var roomKey: String? = null

    val messageAdapter: MessageAdapter = MessageAdapter()
    val chatMemberAdapter: ChatMemberAdapter = ChatMemberAdapter()

    val chat = Chat()
    private val fbUser = FbUser()
    private val chats: ArrayList<Chat> = ArrayList()
    private val members: ArrayList<FbUser> = ArrayList()
    private lateinit var databaseRoomReference: DatabaseReference
    private lateinit var databaseMessageReference: DatabaseReference
    private lateinit var singleValueEventListener: ValueEventListener
    private lateinit var valueEventListener: ValueEventListener

    val roomName = MutableLiveData<String>()
    val roomType = MutableLiveData<Int>()

    val sentEvent = SingleLiveEvent<Unit>()
    val receivedEvent = SingleLiveEvent<ArrayList<Chat>>()
    val terminateEvent = SingleLiveEvent<Unit>()

    fun insertUser() {
        addDisposable(repository.getUser(userId), object : DisposableSingleObserver<User>() {
            override fun onSuccess(t: User) {
                onRetrieveUserSuccess(t)
            }

            override fun onError(e: Throwable) {
                onErrorEvent.value = e
            }
        })
    }

    fun getChatting() {
        setDataBase()
        setChatRoom()
        setListener()
        addListener()
    }

    private fun setChatRoom() {
        setRoomName()
        setRoomType()
    }

    private fun setRoomName() {
        FirebaseDatabase.getInstance().reference
                .child("groupchatrooms")
                .child(roomKey!!)
                .child("roomName").addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        roomName.value = dataSnapshot.getValue(String::class.java)
                    }

                    override fun onCancelled(dataSnapshot: DatabaseError) { }
                })
    }

    private fun setRoomType() {
        FirebaseDatabase.getInstance().reference
                .child("groupchatrooms")
                .child(roomKey!!)
                .child("roomType").addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        roomType.value = getRoomTypeDrawable(dataSnapshot.getValue(String::class.java))
                    }

                    override fun onCancelled(dataSnapshot: DatabaseError) { }
                })
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

    private fun onRetrieveUserSuccess(user: User) {
        with(fbUser) {
            id = user.id
            name = user.name
            photo = user.photo
            uid = FirebaseAuth.getInstance().currentUser!!.uid
        }

        FirebaseDatabase.getInstance().reference
                .child("groupchatrooms")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        var isExistRoom = false

                        for (item in dataSnapshot.children) {
                            if (item.key == roomKey!!) {
                                isExistRoom = true
                            }
                        }

                        if (!isExistRoom) {
                            terminateEvent.call()
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) { }
                })

        FirebaseDatabase.getInstance().reference
                .child("groupchatrooms")
                .child(roomKey!!)
                .child("users")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        var isExistUser = false

                        for (item in dataSnapshot.children) {
                            val member = item.getValue(FbUser::class.java)!!

                            if (member.uid == fbUser.uid) {
                                isExistUser = true
                            }
                        }

                        if (!isExistUser) {
                            insertUserIntoFb()
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) { }
                })

        FirebaseDatabase.getInstance().reference
                .child("groupchatrooms")
                .child(roomKey!!)
                .child("users")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        members.clear()

                        for (item in dataSnapshot.children) {
                            val member = item.getValue(FbUser::class.java)!!

                            if (member.uid != fbUser.uid) {
                                members.add(member)
                            }
                        }

                        chatMemberAdapter.updateList(members)
                    }

                    override fun onCancelled(databaseError: DatabaseError) { }
                })
    }

    private fun insertUserIntoFb() {
        FirebaseDatabase.getInstance().reference
                .child("groupchatrooms")
                .child(roomKey!!)
                .child("users").push().setValue(fbUser)
    }

    private fun setDataBase() {
        databaseRoomReference = FirebaseDatabase.getInstance().reference
                .child("groupchatrooms")
                .child(roomKey!!)

        databaseMessageReference = databaseRoomReference.child("message")
    }

    private fun setListener() {
        singleValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (item in dataSnapshot.children) {
                    chats.add(item.getValue(Chat::class.java)!!)
                }

                messageAdapter.updateList(chats)
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        }

        valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                chats.clear()

                for (item in dataSnapshot.children) {
                    chats.add(item.getValue(Chat::class.java)!!)
                }

                receivedEvent.value = chats
            }

            override fun onCancelled(databaseError: DatabaseError) { }
        }
    }

    private fun addListener() {
        databaseMessageReference.addListenerForSingleValueEvent(singleValueEventListener)
        databaseMessageReference.addValueEventListener(valueEventListener)
    }

    private fun setChat() {
        val simpleDateFormat = SimpleDateFormat("E hh:mm", Locale.KOREA)
        chat.timeStamp = simpleDateFormat.format(Date())

        setChatWriter()
    }

    private fun setChatWriter() {
        addDisposable(repository.getUser(userId), object : DisposableSingleObserver<User>() {
            override fun onSuccess(user: User) {
                chat.writer = FbUser(user, FirebaseAuth.getInstance().currentUser!!.uid)
                sendMessage()
            }

            override fun onError(e: Throwable) {
                onErrorEvent.value = e
            }
        })
    }

    private fun sendMessage() {
        FirebaseDatabase.getInstance().reference
                .child("groupchatrooms")
                .child(roomKey!!)
                .child("message").push().setValue(chat)
                .addOnCompleteListener {
                    sentEvent.call()
                }
    }

    fun onClickSend() {
        setChat()
    }

    fun onDestroy() {
        databaseRoomReference.child("users")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                        var count = 0

                        for (item in dataSnapshot.children) {
                            count++

                            val user = item.getValue(FbUser::class.java)!!

                            if (user.uid == FirebaseAuth.getInstance().currentUser!!.uid) {
                                removeUser(item.key!!)

                                if (count > 1) {
                                    return
                                }
                            }
                        }

                        if (count <= 1) {
                            removeRoom()
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {}
                })
    }

    private fun removeRoom() {
        databaseRoomReference.removeValue()
    }

    private fun removeUser(userKey: String) {
        databaseRoomReference
                .child("users")
                .child(userKey).removeValue()
    }
}