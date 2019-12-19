package com.example.weknot_android.model.chat

class PrivateChatRoom {
    var user_1: String? = null
    var user_2: String? = null
    var messages: Map<String, Chat>? = HashMap()

    constructor()

    constructor(user_1: String, user_2: String) {
        this.user_1 = user_1
        this.user_2 = user_2
    }
}