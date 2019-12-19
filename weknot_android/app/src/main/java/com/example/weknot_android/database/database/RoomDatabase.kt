package com.example.weknot_android.database.database

import android.content.Context

import androidx.room.Database
import androidx.room.Room

import com.example.weknot_android.database.dao.OpenChatDao
import com.example.weknot_android.database.dao.UserDao
import com.example.weknot_android.model.chat.OpenChatRoom
import com.example.weknot_android.model.user.User

@Database(entities = [User::class, OpenChatRoom::class], version = 3, exportSchema = false)
abstract class RoomDatabase : androidx.room.RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun openChatDao(): OpenChatDao

    companion object {

        private var instance: RoomDatabase? = null

        @Synchronized
        fun getInstance(context: Context): RoomDatabase? {
            if (instance == null) {
                instance = Room.databaseBuilder(context.applicationContext,
                        RoomDatabase::class.java, "weknot_database")
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build()
            }
            return instance
        }
    }
}
