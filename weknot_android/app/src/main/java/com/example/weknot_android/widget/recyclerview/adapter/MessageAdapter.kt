package com.example.weknot_android.widget.recyclerview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.databinding.DataBindingUtil

import androidx.recyclerview.widget.RecyclerView

import com.example.weknot_android.R
import com.example.weknot_android.model.chat.Chat
import com.example.weknot_android.widget.SingleLiveEvent
import com.example.weknot_android.widget.recyclerview.holder.MessageViewHolder
import com.example.weknot_android.widget.recyclerview.navigator.message.MessageAdapterNavigator

class MessageAdapter : RecyclerView.Adapter<MessageViewHolder>(), MessageAdapterNavigator {

    private lateinit var chats: ArrayList<Chat>

    private var userId: String? = null

    val openProfile = SingleLiveEvent<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return MessageViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.message_item, parent, false))
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(chats[position], userId)
        holder.setNavigator(this)
    }

    override fun openProfile(id: String) {
        openProfile.value = id
    }

    fun updateList(chats: ArrayList<Chat>) {
        this.chats = chats
        notifyDataSetChanged()
    }

    fun setUserId(userId: String) {
        this.userId = userId
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return if(::chats.isInitialized) chats.size else 0
    }
}