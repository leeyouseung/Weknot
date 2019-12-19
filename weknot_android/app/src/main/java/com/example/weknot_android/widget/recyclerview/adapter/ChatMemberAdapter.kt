package com.example.weknot_android.widget.recyclerview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.databinding.DataBindingUtil

import androidx.recyclerview.widget.RecyclerView

import com.example.weknot_android.R
import com.example.weknot_android.model.user.FbUser
import com.example.weknot_android.widget.SingleLiveEvent
import com.example.weknot_android.widget.recyclerview.holder.ChatMemberViewHolder
import com.example.weknot_android.widget.recyclerview.navigator.chatmember.ChatMemberAdapterNavigator

class ChatMemberAdapter : RecyclerView.Adapter<ChatMemberViewHolder>(), ChatMemberAdapterNavigator {

    private lateinit var members: ArrayList<FbUser>

    val openProfile = SingleLiveEvent<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatMemberViewHolder {
        return ChatMemberViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.chat_member_item, parent, false))
    }

    override fun onBindViewHolder(holder: ChatMemberViewHolder, position: Int) {
        holder.setNavigator(this)
        holder.bind(members[position])
    }

    override fun openProfile(id: String) {
        openProfile.value = id
    }

    fun updateList(members: ArrayList<FbUser>) {
        this.members = members
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return if(::members.isInitialized) members.size else 0
    }
}