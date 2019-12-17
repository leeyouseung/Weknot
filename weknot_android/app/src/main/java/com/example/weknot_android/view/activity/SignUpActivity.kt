package com.example.weknot_android.view.activity

import android.content.Intent

import android.os.Bundle

import android.view.MenuItem

import androidx.lifecycle.Observer

import com.bumptech.glide.Glide

import com.example.weknot_android.BR
import com.example.weknot_android.R
import com.example.weknot_android.base.activity.BasePictureActivity
import com.example.weknot_android.databinding.SignUpActivityBinding
import com.example.weknot_android.viewmodel.SignUpViewModel

class SignUpActivity : BasePictureActivity<SignUpActivityBinding, SignUpViewModel>() {

    override val TAG: String
        get() = this.javaClass.name

    override fun getLayoutId(): Int {
        return R.layout.sign_up_activity
    }

    override fun getViewModel(): Class<SignUpViewModel> {
        return SignUpViewModel::class.java
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun initObserver() {
        with(viewModel) {

            onSuccessEvent.observe(this@SignUpActivity, Observer {
                simpleToast(it)
            })

            signUpEvent.observe(this@SignUpActivity, Observer {
                if (isEmpty()) {
                    simpleToast(R.string.empty_message)
                    return@Observer
                }
                viewModel.signUp()
            })

            nullPointEvent.observe(this@SignUpActivity, Observer {
                simpleToast(R.string.empty_picture_message)
            })

            openLogin.observe(this@SignUpActivity, Observer {
                startActivityWithFinish(LoginActivity::class.java)
            })

            goToAlbum.observe(this@SignUpActivity, Observer {
                tedPermission()
                goToAlbum()
            })

            goToCrop.observe(this@SignUpActivity, Observer {
                goToCropPage(viewModel.tempPictureUri.value, viewModel.pictureUri.value)
            })

            backMessageToast.observe(this@SignUpActivity, Observer {
                simpleToast(R.string.exist_message)
            })

            onErrorEvent.observe(this@SignUpActivity, Observer {
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
        Glide.with(this).load(viewModel.pictureUri.value).into(binding.inputImage)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (supportActionBar != null) supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        longToast(R.string.signup_error_message)
    }

    private fun isEmpty(): Boolean {
        return viewModel.request.id.isEmpty() || viewModel.request.pw.isEmpty() ||
                viewModel.request.birth.isEmpty() || viewModel.request.name.isEmpty() ||
                viewModel.request.gender.isEmpty() || viewModel.request.phoneNumber.isEmpty() ||
                viewModel.request.intro.isEmpty()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            startActivityWithFinish(LoginActivity::class.java)
            return true
        }
        return false
    }
}