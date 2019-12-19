package com.example.weknot_android.view.activity

import android.content.Intent

import android.graphics.Bitmap
import android.graphics.Color

import android.net.Uri

import android.os.Bundle

import androidx.lifecycle.Observer

import com.example.weknot_android.BR
import com.example.weknot_android.R
import com.example.weknot_android.base.activity.BasePictureActivity
import com.example.weknot_android.databinding.PictureActivityBinding
import com.example.weknot_android.viewmodel.PictureViewModel
import com.example.weknot_android.widget.extension.shortToast

class PictureActivity : BasePictureActivity<PictureActivityBinding, PictureViewModel>() {

    override val TAG: String
        get() = this.javaClass.name

    override fun getLayoutId(): Int {
        return R.layout.picture_activity
    }

    override fun getViewModel(): Class<PictureViewModel> {
        return PictureViewModel::class.java
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun initObserver() {
        with(viewModel) {

            downloadEvent.observe(this@PictureActivity, Observer {
                sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(it)))
                this@PictureActivity.shortToast(R.string.download_message)
            })

            backEvent.observe(this@PictureActivity, Observer {
                finish()
            })
        }
    }

    override fun setBitmap(bitmap: Bitmap?) {
        super.setBitmap(bitmap)
        viewModel.bitmap.value = bitmap
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUp()
    }

    private fun setUp() {
        tedPermission()
        setBaseColor()
        viewModel.picture.value = intent.getStringExtra("url")
        initBitmapImage(viewModel.picture.value)
    }

    private fun setBaseColor() {
        window.statusBarColor = Color.BLACK
        window.navigationBarColor = Color.BLACK
    }
}