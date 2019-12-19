package com.example.weknot_android.view.activity

import androidx.lifecycle.Observer

import com.example.weknot_android.BR
import com.example.weknot_android.R
import com.example.weknot_android.base.activity.BaseActivity
import com.example.weknot_android.databinding.LoginActivityBinding
import com.example.weknot_android.viewmodel.LoginViewModel
import com.example.weknot_android.widget.extension.shortToast

class LoginActivity : BaseActivity<LoginActivityBinding, LoginViewModel>() {

    override val TAG: String
        get() = this.javaClass.name

    override fun getLayoutId(): Int {
        return R.layout.login_activity
    }

    override fun getViewModel(): Class<LoginViewModel> {
        return LoginViewModel::class.java
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun initObserver() {
        with(viewModel) {

            openSignUp.observe(this@LoginActivity, Observer {
                startActivity(SignUpActivity::class.java)
            })

            loginEvent.observe(this@LoginActivity, Observer {
                if (isEmpty()) {
                    this@LoginActivity.shortToast(R.string.empty_message)
                    return@Observer
                }

                login()
            })

            onErrorEvent.observe(this@LoginActivity, Observer {
                this@LoginActivity.shortToast(it.message)
            })

            onSuccessEvent.observe(this@LoginActivity, Observer {
                startActivityWithFinish(MainActivity::class.java)
            })
        }
    }

    private fun isEmpty(): Boolean {
        return viewModel.request.id == null || viewModel.request.password == null
    }
}