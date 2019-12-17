package com.example.weknot_android.view.activity

import android.os.Bundle

import android.view.MenuItem

import androidx.lifecycle.Observer

import androidx.viewpager.widget.ViewPager.OnPageChangeListener

import com.example.weknot_android.BR
import com.example.weknot_android.R
import com.example.weknot_android.base.activity.BaseActivity
import com.example.weknot_android.databinding.MainActivityBinding
import com.example.weknot_android.view.dialog.LogoutDialog
import com.example.weknot_android.viewmodel.MainViewModel
import com.example.weknot_android.widget.viewpager.MainPagerAdapter

import com.google.android.material.tabs.TabLayout

class MainActivity: BaseActivity<MainActivityBinding, MainViewModel>() {

    private val PAGE_COUNT: Int = 5

    override val TAG: String
        get() = this.javaClass.name

    override fun getLayoutId(): Int {
        return R.layout.main_activity
    }

    override fun getViewModel(): Class<MainViewModel> {
        return MainViewModel::class.java
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun initObserver() {
        with(viewModel) {

            onErrorEvent.observe(this@MainActivity, Observer {
                simpleToast(it.message)
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (supportActionBar != null) supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setUp()
    }

    private fun setUp() {
        setUpViewPager()
    }

    private fun setUpViewPager() {
        val pagerAdapter = MainPagerAdapter(supportFragmentManager)

        binding.viewPager.offscreenPageLimit = PAGE_COUNT
        binding.viewPager.adapter = pagerAdapter

        setViewPagerListener()
        setTabLayoutListener()
    }

    private fun setViewPagerListener() {
        binding.viewPager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }
            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> setTitle(R.string.feed_title)
                    1 -> setTitle(R.string.video_call_title)
                    2 -> setTitle(R.string.open_chat_title)
                    3 -> setTitle(R.string.social_title)
                    4 -> setTitle(R.string.myinfo_title)
                }
            }
        })

        binding.viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.tablayout))
    }

    private fun setTabLayoutListener() {
        binding.tablayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab) {
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {
            }
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.viewPager.currentItem = tab.position
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            newInstance(LogoutDialog()).show(supportFragmentManager)
            return true
        }
        return false
    }
}