package com.example.weknot_android.database.repository

import android.app.Application

import com.example.weknot_android.database.dao.OpenChatDao
import com.example.weknot_android.database.dao.UserDao
import com.example.weknot_android.database.database.RoomDatabase
import com.example.weknot_android.model.chat.OpenChatRoom
import com.example.weknot_android.model.user.User

import io.reactivex.Completable
import io.reactivex.Single

class RoomRepository(application: Application) {

    private var userDao: UserDao
    private var openChatDao: OpenChatDao

    init {
        val database = RoomDatabase.getInstance(application)!!
        userDao = database.userDao()
        openChatDao = database.openChatDao()
    }

    fun insertUser(entity: User): Completable { return userDao.insert(entity) }
    fun updateUser(entity: User): Completable { return userDao.update(entity) }
    fun deletUser(entity: User): Completable { return userDao.delete(entity) }
    fun getUser(id: String): Single<User> {
        return userDao.getUser(id)
    }

    fun insertOpenChat(entity: OpenChatRoom): Completable { return openChatDao.insert(entity) }
    fun insertOpenChat(vararg entity: OpenChatRoom): Completable { return openChatDao.insert(entity) }
    fun getOpenChat(roomNumber: String): Single<OpenChatRoom>? { return openChatDao.getOpenChat(roomNumber) }
}
