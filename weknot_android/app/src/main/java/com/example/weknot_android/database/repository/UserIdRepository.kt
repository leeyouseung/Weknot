package com.example.weknot_android.database.repository

import android.content.Context

import com.example.weknot_android.database.sharedpreference.UserId

class UserIdRepository(private val context: Context) {
    val userId: UserId = UserId(context)

    fun setUserId(id: String) {
        UserId(context).id = id
    }
}