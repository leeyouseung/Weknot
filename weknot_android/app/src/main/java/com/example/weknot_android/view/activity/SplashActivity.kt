package com.example.weknot_android.view.activity

import android.os.Bundle

import androidx.lifecycle.Observer

import com.example.weknot_android.BR
import com.example.weknot_android.R
import com.example.weknot_android.base.activity.BaseActivity
import com.example.weknot_android.databinding.SplashActivityBinding
import com.example.weknot_android.viewmodel.SplashViewModel

class SplashActivity : BaseActivity<SplashActivityBinding, SplashViewModel>() {

    override val TAG: String
        get() = this.javaClass.name

    override fun getLayoutId(): Int {
        return R.layout.splash_activity
    }

    override fun getViewModel(): Class<SplashViewModel> {
        return SplashViewModel::class.java
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun initObserver() {
        with(viewModel) {
            openMain.observe(this@SplashActivity, Observer {
                startActivityWithFinish(MainActivity::class.java)
            })

            onErrorEvent.observe(this@SplashActivity, Observer {
                startActivityWithFinish(LoginActivity::class.java)
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUp()
    }

    private fun setUp() {
        if (viewModel.token == "") {
            startActivityWithFinish(LoginActivity::class.java)
        }
        else {
            viewModel.autoLogin()
        }
    }
}