package com.example.weknot_android.view.fragment

import android.content.Intent

import android.os.Bundle

import android.view.View

import androidx.lifecycle.Observer

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

import com.example.weknot_android.BR
import com.example.weknot_android.R
import com.example.weknot_android.base.fragment.BaseListFragment
import com.example.weknot_android.databinding.OpenChatFragmentBinding
import com.example.weknot_android.view.activity.ChatActivity
import com.example.weknot_android.viewmodel.OpenChatViewModel
import com.example.weknot_android.widget.extension.shortToast
import com.example.weknot_android.widget.extension.startActivityWithFinish

import com.google.android.material.floatingactionbutton.FloatingActionButton

class OpenChatFragment : BaseListFragment<OpenChatFragmentBinding, OpenChatViewModel>() , SwipeRefreshLayout.OnRefreshListener  {

    override fun getLayoutId(): Int {
        return R.layout.open_chat_fragment
    }

    override fun getViewModel(): Class<OpenChatViewModel> {
        return OpenChatViewModel::class.java
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun initObserver() {
        with(viewModel) {

            onErrorEvent.observe(this@OpenChatFragment, Observer {
                this@OpenChatFragment.shortToast(it.message)
            })

            openCreateRoom.observe(this@OpenChatFragment, Observer {
                newInstance(createRoomDialog).show(fragmentManager)
            })

            createRoomDialog.dialogCloseEvent.observe(this@OpenChatFragment, Observer {
                getChattingRooms()
            })

            openChatAdapter.openChatRoom.observe(this@OpenChatFragment, Observer {
                this@OpenChatFragment.startActivityWithFinish(Intent(context, ChatActivity::class.java)
                        .putExtra("key", it))
            })
        }
    }

    override fun setBtn(): FloatingActionButton {
        return binding.createBtn
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUp()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getChattingRooms()
    }

    override fun onRefresh() {
        viewModel.getChattingRooms()
        binding.swipeRefreshLayout.isRefreshing = false
    }

    private fun setUp() {
        binding.chatRoomRecyclerview.addOnScrollListener(scrollListener)

        binding.swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent)
        binding.swipeRefreshLayout.setOnRefreshListener(this)
    }
}