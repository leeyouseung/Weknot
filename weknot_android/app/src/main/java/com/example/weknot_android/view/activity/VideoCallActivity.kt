package com.example.weknot_android.view.activity

import android.content.Context
import android.content.Intent

import android.util.Log

import com.example.weknot_android.view.navigator.VideoCallNavigator

import org.jitsi.meet.sdk.*

import java.lang.ref.WeakReference

class VideoCallActivity : JitsiMeetActivity() {

    companion object {
        fun videoLaunch(context: Context, options: JitsiMeetConferenceOptions, channel: String) {
            val intent = Intent(context, VideoCallActivity::class.java)
            intent.action = "org.jitsi.meet.CONFERENCE"
            intent.putExtra("JitsiMeetConferenceOptions", options)
            intent.putExtra("channel", channel)
            context.startActivity(intent)
        }

        private lateinit var navigator: WeakReference<VideoCallNavigator>

        private fun getNavigator(): VideoCallNavigator {
            return navigator.get()!!
        }
        fun setNavigator(navigator: VideoCallNavigator) {
            this.navigator = WeakReference(navigator)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        getNavigator().finishEvent()
    }

    override fun onConferenceTerminated(data: Map<String?, Any?>) {
        Log.d(TAG, "Conference terminated: $data")
        getNavigator().finishEvent()
        finish()
    }
}
