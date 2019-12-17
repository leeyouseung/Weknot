package com.example.weknot_android.view.dialog

import androidx.fragment.app.FragmentManager

import androidx.lifecycle.Observer

import com.example.weknot_android.BR
import com.example.weknot_android.R
import com.example.weknot_android.base.BaseDialog
import com.example.weknot_android.databinding.LogoutDialogBinding
import com.example.weknot_android.view.activity.LoginActivity
import com.example.weknot_android.viewmodel.LogoutViewModel

class LogoutDialog : BaseDialog<LogoutDialogBinding, LogoutViewModel>() {

    private val TAG: String? = LogoutDialog::class.java.simpleName

    override fun getLayoutId(): Int {
        return R.layout.logout_dialog
    }

    override fun getViewModel(): Class<LogoutViewModel> {
        return LogoutViewModel::class.java
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun initObserver() {
        with(viewModel) {
            backEvent.observe(this@LogoutDialog, Observer {
                dismiss()
            })

            openLogin.observe(this@LogoutDialog, Observer {
                startActivityWithFinish(LoginActivity::class.java)
            })
        }
    }

    fun show(fragmentManager: FragmentManager?) {
        super.show(fragmentManager!!, TAG)
    }
}