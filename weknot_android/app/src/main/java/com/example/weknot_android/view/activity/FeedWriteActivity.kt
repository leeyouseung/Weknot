package com.example.weknot_android.view.activity

import android.content.Intent

import android.os.Bundle

import android.view.Menu
import android.view.MenuItem

import androidx.lifecycle.Observer

import com.bumptech.glide.Glide

import com.example.weknot_android.BR
import com.example.weknot_android.R
import com.example.weknot_android.base.activity.BasePictureActivity
import com.example.weknot_android.databinding.FeedWriteActivityBinding
import com.example.weknot_android.viewmodel.FeedWriteViewModel

class FeedWriteActivity : BasePictureActivity<FeedWriteActivityBinding, FeedWriteViewModel>() {

    override val TAG: String
        get() = this.javaClass.name

    override fun getLayoutId(): Int {
        return R.layout.feed_write_activity
    }

    override fun getViewModel(): Class<FeedWriteViewModel> {
        return FeedWriteViewModel::class.java
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun initObserver() {
        with(viewModel) {

            openMain.observe(this@FeedWriteActivity, Observer {
                startActivityWithFinish(MainActivity::class.java)
            })

            goToCrop.observe(this@FeedWriteActivity, Observer {
                goToCropPage(viewModel.tempPictureUri.value, viewModel.pictureUri.value)
            })

            backMessageToast.observe(this@FeedWriteActivity, Observer {
                simpleToast(R.string.exist_message)
                startActivityWithFinish(MainActivity::class.java)
            })

            nullPointerException.observe(this@FeedWriteActivity, Observer {
                simpleToast(R.string.empty_message)
            })

            onErrorEvent.observe(this@FeedWriteActivity, Observer {
                simpleToast(it.message)
            })
        }
    }

    override fun requestNotOkEvent() {
        viewModel.deleteFile()
    }

    override fun pickNextEvent(data: Intent) {
        viewModel.savePickData(data)
        viewModel.cropImage()
    }

    override fun cropNextEvent() {
        super.cropNextEvent()
        Glide.with(this).load(viewModel.pictureUri.value).into(binding.image)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (supportActionBar != null) supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setUp()
    }

    private fun setUp() {
        tedPermission()
        goToAlbum()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_write, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        else if (item.itemId == R.id.menu_write) {
            viewModel.postFeed()
            return true
        }
        return false
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivityWithFinish(MainActivity::class.java)
    }
}