package com.example.weknot_android.widget

import com.example.weknot_android.model.user.FbUser

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

import kotlin.system.exitProcess

class UncaughtExceptionHandler(var roomKey: String) : Thread.UncaughtExceptionHandler {

    private lateinit var databaseMessageReference: DatabaseReference
    private lateinit var databaseRoomReference: DatabaseReference

    override fun uncaughtException(thread: Thread, ex: Throwable) {
        onDestroy()

        android.os.Process.killProcess(android.os.Process.myPid())
        exitProcess(10)

        //androidDefaultUEH.uncaughtException(thread, ex);
    }

    private fun onDestroy() {
        setDataBase()

        databaseMessageReference.child("users")
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

    private fun setDataBase() {
        databaseRoomReference = FirebaseDatabase.getInstance().reference
                .child("groupchatrooms")
                .child(roomKey)
        databaseMessageReference = databaseRoomReference.child("message")
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