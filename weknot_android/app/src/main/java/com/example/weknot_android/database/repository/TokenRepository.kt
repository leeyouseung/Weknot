package com.example.weknot_android.database.repository

import android.content.Context

import com.example.weknot_android.database.sharedpreference.Token

class TokenRepository(private val context: Context) {
    val token: Token = Token(context)

    fun setToken(token: String) {
        Token(context).token = token
    }
}