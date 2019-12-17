package com.example.weknot_android.view.activity

import android.annotation.SuppressLint

import android.content.Intent

import android.os.Bundle

import android.view.Gravity
import android.view.Menu
import android.view.MenuItem

import androidx.lifecycle.Observer

import com.example.weknot_android.BR
import com.example.weknot_android.R
import com.example.weknot_android.base.activity.BaseActivity
import com.example.weknot_android.databinding.ChatActivityBinding
import com.example.weknot_android.view.dialog.ExitRoomDialog
import com.example.weknot_android.viewmodel.ChatViewModel

class ChatActivity : BaseActivity<ChatActivityBinding, ChatViewModel>() {

    override val TAG: String
        get() = this.javaClass.name

    override fun getLayoutId(): Int {
        return R.layout.chat_activity
    }

    override fun getViewModel(): Class<ChatViewModel> {
        return ChatViewModel::class.java
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun initObserver() {
        with(viewModel) {
            sentEvent.observe(this@ChatActivity, Observer {
                binding.messageText.setText("")

            })

            receivedEvent.observe(this@ChatActivity, Observer {
                binding.messageRecyclerview.scrollToPosition(it.size - 1)
            })

            terminateEvent.observe(this@ChatActivity, Observer {
                startActivityWithFinish(MainActivity::class.java)
            })

            messageAdapter.openProfile.observe(this@ChatActivity, Observer {
                openProfile(it)
            })

            chatMemberAdapter.openProfile.observe(this@ChatActivity, Observer {
                openProfile(it)
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (supportActionBar != null) supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setUp()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getChatting()
    }

    override fun onStart() {
        super.onStart()
        viewModel.insertUser()
    }

    private fun setUp() {
        viewModel.roomKey = intent.getStringExtra("key")
    }

    private fun openProfile(id: String) {
        val intent = Intent(this, ProfileActivity::class.java)
        intent.putExtra("id", id)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_chat, menu)
        return super.onCreateOptionsMenu(menu)
    }

    @SuppressLint("RtlHardcoded")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            newInstance(ExitRoomDialog(viewModel.roomKey!!)).show(supportFragmentManager)
            return true
        }
        else if (item.itemId == R.id.menu_member) {
            binding.drawerLayout.openDrawer(Gravity.RIGHT)
            return true
        }
        return false
    }

    override fun onHome() {
        super.onHome()
        viewModel.onDestroy()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }

    override fun onBackPressed() {
        newInstance(ExitRoomDialog(viewModel.roomKey!!)).show(supportFragmentManager)
    }
}