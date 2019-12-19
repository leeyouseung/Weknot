package com.example.weknot_android.widget.recyclerview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.databinding.DataBindingUtil

import androidx.recyclerview.widget.RecyclerView.Adapter

import com.example.weknot_android.R
import com.example.weknot_android.model.chat.ChatRoom
import com.example.weknot_android.widget.SingleLiveEvent
import com.example.weknot_android.widget.recyclerview.holder.OpenChatViewHolder
import com.example.weknot_android.widget.recyclerview.navigator.openchat.OpenChatAdapterNavigator

class OpenChatAdapter : Adapter<OpenChatViewHolder>(), OpenChatAdapterNavigator {

    private lateinit var openChatRooms: List<ChatRoom>
    private lateinit var keys: List<String>

    val openChatRoom = SingleLiveEvent<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OpenChatViewHolder {
        return OpenChatViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.open_chat_item, parent, false))
    }

    override fun onBindViewHolder(holder: OpenChatViewHolder, position: Int) {
        holder.setNavigator(this)
        holder.bind(openChatRooms[position], keys[position])
    }

    override fun openChatRoom(key: String) {
        openChatRoom.value = key
    }

    fun updateList(openChatRooms: List<ChatRoom>) {
        this.openChatRooms = openChatRooms
        notifyDataSetChanged()
    }

    fun updateKey(keys: List<String>) {
        this.keys = keys
    }

    override fun getItemCount(): Int {
        return if(::openChatRooms.isInitialized) openChatRooms.size else 0
    }
}