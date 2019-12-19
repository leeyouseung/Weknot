package com.example.weknot_android.widget.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

import com.example.weknot_android.base.fragment.BaseFragment.Companion.newInstance
import com.example.weknot_android.view.fragment.*

class MainPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(i: Int): Fragment {
        return when (i) {
            0 -> newInstance(FeedFragment())
            1 -> newInstance(VideoCallFragment())
            2 -> newInstance(OpenChatFragment())
            3 -> newInstance(SocialFragment())
            4 -> newInstance(MyinfoFragment())
            else -> newInstance(FeedFragment())
        }
    }

    override fun getCount(): Int {
        return PAGE_NUMBER
    }

    companion object {
        private const val PAGE_NUMBER = 5
    }
}