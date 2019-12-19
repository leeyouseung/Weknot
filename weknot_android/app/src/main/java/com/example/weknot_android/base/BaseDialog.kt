package com.example.weknot_android.base

import android.app.Dialog

import android.content.Intent

import android.graphics.Color
import android.graphics.drawable.ColorDrawable

import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.view.Window

import android.widget.RelativeLayout

import androidx.annotation.LayoutRes

import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

import androidx.lifecycle.ViewModelProviders

import com.example.weknot_android.base.viewmodel.BaseViewModel
import com.example.weknot_android.widget.SingleLiveEvent

abstract class BaseDialog<VB : ViewDataBinding, VM : BaseViewModel<*>> : DialogFragment() {

    protected lateinit var binding: VB
    protected lateinit var viewModel: VM

    val dialogCloseEvent = SingleLiveEvent<Unit>()

    @LayoutRes
    protected abstract fun getLayoutId(): Int

    protected abstract fun getViewModel(): Class<VM>

    protected abstract fun getBindingVariable(): Int

    protected abstract fun initObserver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.viewModel = if(::viewModel.isInitialized) viewModel else ViewModelProviders.of(this).get(getViewModel())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
                inflater, getLayoutId(), container, false)!!
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUp()
        initObserver()
    }

    private fun setUp() {
        binding.setVariable(getBindingVariable(),viewModel)
        binding.lifecycleOwner = this
        binding.executePendingBindings()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val root = RelativeLayout(activity)
        root.layoutParams = LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT)

        val dialog = Dialog(context!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(root)
        if (dialog.window != null) {
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window!!.setLayout(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT)
        }
        dialog.setCanceledOnTouchOutside(true)
        return dialog
    }

    override fun show(fragmentManager: FragmentManager, tag: String?) {
        val transaction = fragmentManager.beginTransaction()
        val prevFragment = fragmentManager.findFragmentByTag(tag)
        if (prevFragment != null) {
            transaction.remove(prevFragment)
        }
        transaction.addToBackStack(null)
        show(transaction, tag)
    }

    override fun dismiss() {
        super.dismiss()
        dialogCloseEvent.call()
    }

    companion object {
        fun <T : Fragment?> newInstance(dialog: T): T {
            val args = Bundle()
            dialog!!.arguments = args
            return dialog
        }
    }
}