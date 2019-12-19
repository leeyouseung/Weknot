package com.example.weknot_android.widget.recyclerview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.databinding.DataBindingUtil

import androidx.recyclerview.widget.RecyclerView.Adapter

import com.example.weknot_android.R
import com.example.weknot_android.model.user.Friend
import com.example.weknot_android.network.request.FriendRequest
import com.example.weknot_android.widget.SingleLiveEvent
import com.example.weknot_android.widget.recyclerview.holder.SocialViewHolder
import com.example.weknot_android.widget.recyclerview.navigator.social.SocialAdapterNavigator

class SocialAdapter : Adapter<SocialViewHolder>(), SocialAdapterNavigator {

    private lateinit var friends: List<Friend>

    val checkFriendEvent = SingleLiveEvent<FriendRequest>()
    val openChatRoom = SingleLiveEvent<String>()
    val openProfile = SingleLiveEvent<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SocialViewHolder {
        return SocialViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.social_item, parent, false))
    }

    override fun onBindViewHolder(holder: SocialViewHolder, position: Int) {
        holder.setNavigator(this)
        holder.bind(friends[position])
    }

    override fun checkFriend(message: String, friend: Friend) {
        checkFriendEvent.value = FriendRequest(friend.friendId, message)
    }

    override fun openChatRoom(id: String) {
        openChatRoom.value = id
    }

    override fun openProfile(id: String) {
        openProfile.value = id
    }

    fun updateList(friends: List<Friend>) {
        this.friends = friends
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return if(::friends.isInitialized) friends.size else 0
    }
}