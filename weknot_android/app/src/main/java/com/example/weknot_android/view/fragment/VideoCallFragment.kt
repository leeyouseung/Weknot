package com.example.weknot_android.view.fragment

import android.content.Intent

import android.os.Bundle

import android.view.View

import androidx.lifecycle.Observer

import com.example.weknot_android.BR
import com.example.weknot_android.R
import com.example.weknot_android.base.fragment.BaseFragment
import com.example.weknot_android.databinding.VideoCallFragmentBinding
import com.example.weknot_android.view.activity.ProfileActivity
import com.example.weknot_android.view.activity.VideoCallActivity
import com.example.weknot_android.view.navigator.VideoCallNavigator
import com.example.weknot_android.viewmodel.VideoCallViewModel

import org.jitsi.meet.sdk.*
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions.Builder

class VideoCallFragment : BaseFragment<VideoCallFragmentBinding, VideoCallViewModel>(), VideoCallNavigator {

    override fun getLayoutId(): Int {
        return R.layout.video_call_fragment
    }

    override fun getViewModel(): Class<VideoCallViewModel> {
        return VideoCallViewModel::class.java
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun finishEvent() {
        viewModel.finishRoom()
    }

    override fun initObserver() {
        with(viewModel) {

            connectVideoCall.observe(this@VideoCallFragment, Observer {

                val options: JitsiMeetConferenceOptions? = Builder()
                        .setRoom("channel" + it.channel)
                        .setSubject("랜덤채팅")
                        .setUserInfo(JitsiMeetUserInfo(viewModel.userInfo))
                        .setAudioMuted(true)
                        .build()

                VideoCallActivity.videoLaunch(context!!, options!!, it.channel)
            })

            openProfile.observe(this@VideoCallFragment, Observer {
                val intent = Intent(context, ProfileActivity::class.java)
                intent.putExtra("id", it)
                startActivity(intent)
            })
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        VideoCallActivity.setNavigator(this)
        viewModel.setUp()
    }
}