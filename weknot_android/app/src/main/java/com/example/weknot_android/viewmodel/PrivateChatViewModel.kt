package com.example.weknot_android.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.weknot_android.base.viewmodel.BaseViewModel
import com.example.weknot_android.model.chat.Chat
import com.example.weknot_android.model.chat.PrivateChatRoom
import com.example.weknot_android.model.user.Profile
import com.example.weknot_android.network.comm.UserComm
import com.example.weknot_android.util.Constants
import com.example.weknot_android.widget.SingleLiveEvent
import com.example.weknot_android.widget.recyclerview.adapter.MessageAdapter
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PrivateChatViewModel(application: Application) : BaseViewModel<Profile>(application) {
    private val userComm = UserComm()

    var otherUserId: String? = null

    val messageAdapter: MessageAdapter = MessageAdapter()

    val chat = Chat()
    private var roomKey: String? = null
    private val chats: ArrayList<Chat> = ArrayList()
    private lateinit var databaseRoomReference: DatabaseReference
    private lateinit var databaseMessageReference: DatabaseReference
    private lateinit var singleValueEventListener: ValueEventListener
    private lateinit var valueEventListener: ValueEventListener

    val name = MutableLiveData<String>()
    val photo = MutableLiveData<String>()

    val sentEvent = SingleLiveEvent<Unit>()
    val receivedEvent = SingleLiveEvent<ArrayList<Chat>>()
    val nullPointEvent = SingleLiveEvent<Unit>()
    val openProfile = SingleLiveEvent<Unit>()

    fun setUp() {
        getUserInfo()
        getRoomKey()
    }

    private fun getUserInfo() {
        addDisposableLoading(userComm.getProfile(token, otherUserId!!), dataObserver)
    }

    private fun getRoomKey() {
        FirebaseDatabase.getInstance().reference
                .child("privatechatrooms")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                        for (item in dataSnapshot.children) {
                            val chatRoom = item.getValue(PrivateChatRoom::class.java)!!

                            if (chatRoom.user_1 == userId) {
                                if (chatRoom.user_2 == otherUserId) {
                                    roomKey = item.key
                                    getChatting()
                                    return
                                }
                            }
                            else if (chatRoom.user_2 == userId) {
                                if (chatRoom.user_1 == otherUserId) {
                                    roomKey = item.key
                                    getChatting()
                                    return
                                }
                            }
                        }

                        nullPointEvent.call()
                    }

                    override fun onCancelled(databaseError: DatabaseError) { }
                })
    }

    private fun getChatting() {
        setDataBase()
        setListener()
        addListener()
    }

    private fun setDataBase() {
        databaseRoomReference = FirebaseDatabase.getInstance().reference
                .child("privatechatrooms")
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
                messageAdapter.setUserId(userId)
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
        chat.id = userId
    }

    private fun sendMessage() {
        FirebaseDatabase.getInstance().reference
                .child("privatechatrooms")
                .child(roomKey!!)
                .child("message").push().setValue(chat)
                .addOnCompleteListener {
                    sentEvent.call()
                }
    }

    fun onClickSend() {
        setChat()
        sendMessage()
    }

    fun onClickPhoto() {
        openProfile.call()
    }

    override fun onRetrieveDataSuccess(data: Profile) {
        super.onRetrieveDataSuccess(data)

        name.value = data.name
        photo.value = Constants.MAIN_HOST + "/image/" + data.photo
    }
}