package com.example.weknot_android.view.activity

import android.content.Intent

import android.os.Bundle

import android.view.MenuItem

import androidx.lifecycle.Observer

import com.example.weknot_android.BR
import com.example.weknot_android.R
import com.example.weknot_android.base.activity.BaseActivity
import com.example.weknot_android.databinding.PrivateChatActivityBinding
import com.example.weknot_android.viewmodel.PrivateChatViewModel

class PrivateChatActivity : BaseActivity<PrivateChatActivityBinding, PrivateChatViewModel>() {

    override val TAG: String
        get() = this.javaClass.name

    override fun getLayoutId(): Int {
        return R.layout.private_chat_activity
    }

    override fun getViewModel(): Class<PrivateChatViewModel> {
        return PrivateChatViewModel::class.java
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun initObserver() {
        with(viewModel) {

            sentEvent.observe(this@PrivateChatActivity, Observer {
                binding.messageText.setText("")
            })

            receivedEvent.observe(this@PrivateChatActivity, Observer {
                binding.messageRecyclerview.scrollToPosition(it.size - 1)
            })

            nullPointEvent.observe(this@PrivateChatActivity, Observer {
                finish()
            })

            openProfile.observe(this@PrivateChatActivity, Observer {
                startActivity(Intent(this@PrivateChatActivity, ProfileActivity::class.java)
                        .putExtra("id", otherUserId))
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbar)
        if (supportActionBar != null) supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        setUp()
    }

    private fun setUp() {
        viewModel.otherUserId = intent.getStringExtra("id")
        viewModel.setUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return false
    }
}