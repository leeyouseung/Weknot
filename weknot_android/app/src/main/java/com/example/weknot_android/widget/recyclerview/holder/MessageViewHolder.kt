package com.example.weknot_android.widget.recyclerview.holder

import android.graphics.Color

import android.view.Gravity
import android.view.View

import com.example.weknot_android.R
import com.example.weknot_android.base.BaseViewHolder
import com.example.weknot_android.databinding.MessageItemBinding
import com.example.weknot_android.model.chat.Chat
import com.example.weknot_android.widget.recyclerview.navigator.message.MessageAdapterNavigator
import com.example.weknot_android.widget.recyclerview.navigator.message.MessageItemNavigator
import com.example.weknot_android.widget.recyclerview.viewmodel.MessageItemViewModel

import com.google.firebase.auth.FirebaseAuth

class MessageViewHolder(val binding: MessageItemBinding) : BaseViewHolder<MessageAdapterNavigator>(binding.root), MessageItemNavigator {

    private val viewModel = MessageItemViewModel()

    private lateinit var id: String
    private lateinit var chat: Chat

    fun bind(chat: Chat, userId: String?) {
        viewModel.bind(chat)
        viewModel.setNavigator(this)
        this.chat = chat

        if (userId == null) {
            this.id = chat.writer!!.id!!
        }
        else {
            id = userId
        }
        binding.viewModel = viewModel

        if (chat.writer == null) {
            if (chat.id == userId) {
                setMyMessage()
            }
            else {
                setOtherMessage()
            }
        }
        else {
            if (chat.writer!!.uid!! == FirebaseAuth.getInstance().currentUser!!.uid) {
                setMyMessage()
            }
            else {
                setOtherMessage()
            }
        }
    }

    private fun setOtherMessage() {
        binding.message.setTextColor(Color.WHITE)
        binding.message.setBackgroundResource(R.drawable.background_receive_message)
        binding.messageItemLinearlayoutMain.gravity = Gravity.START
        if (chat.writer == null) {
            binding.writer.visibility = View.GONE
            binding.photo.visibility = View.GONE
        }
        else {
            binding.writer.visibility = View.VISIBLE
            binding.photo.visibility = View.VISIBLE
        }
        binding.receiveTime.visibility = View.VISIBLE
        binding.sendTime.visibility = View.GONE
    }

    private fun setMyMessage() {
        binding.message.setBackgroundResource(R.drawable.background_send_message)
        binding.message.setTextColor(Color.BLACK)
        binding.messageItemLinearlayoutMain.gravity = Gravity.END
        binding.writer.visibility = View.GONE
        binding.photo.visibility = View.GONE
        binding.receiveTime.visibility = View.GONE
        binding.sendTime.visibility = View.VISIBLE
    }

    override fun openProfile() {
        getNavigator().openProfile(id)
    }
}