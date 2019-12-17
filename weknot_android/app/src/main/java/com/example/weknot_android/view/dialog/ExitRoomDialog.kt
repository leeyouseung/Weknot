package com.example.weknot_android.view.dialog

import android.os.Bundle

import androidx.fragment.app.FragmentManager

import androidx.lifecycle.Observer

import com.example.weknot_android.BR
import com.example.weknot_android.R
import com.example.weknot_android.base.BaseDialog
import com.example.weknot_android.databinding.ExitRoomDialogBinding
import com.example.weknot_android.view.activity.MainActivity
import com.example.weknot_android.viewmodel.ExitRoomViewModel

class ExitRoomDialog(var key: String) : BaseDialog<ExitRoomDialogBinding, ExitRoomViewModel>() {

    private val TAG: String? = ExitRoomDialog::class.java.simpleName

    override fun getLayoutId(): Int {
        return R.layout.exit_room_dialog
    }

    override fun getViewModel(): Class<ExitRoomViewModel> {
        return ExitRoomViewModel::class.java
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun initObserver() {
        with(viewModel) {
            backEvent.observe(this@ExitRoomDialog, Observer {
                dismiss()
            })

            exitEvent.observe(this@ExitRoomDialog, Observer {
                startActivityWithFinish(MainActivity::class.java)
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.roomKey = key
        viewModel.onCreate()
    }

    fun show(fragmentManager: FragmentManager?) {
        super.show(fragmentManager!!, TAG)
    }
}