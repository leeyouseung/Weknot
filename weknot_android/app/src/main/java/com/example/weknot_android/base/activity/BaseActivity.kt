package com.example.weknot_android.base.activity

import android.app.ActivityManager

import android.content.Context
import android.content.Intent

import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle

import androidx.annotation.LayoutRes

import androidx.appcompat.app.AppCompatActivity

import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

import androidx.fragment.app.Fragment

import androidx.lifecycle.ViewModelProviders

import com.example.weknot_android.base.viewmodel.BaseViewModel
import com.example.weknot_android.databinding.AppBarBinding

abstract class BaseActivity<VB : ViewDataBinding, VM : BaseViewModel<*>> : AppCompatActivity() {

    protected lateinit var binding: VB
    protected lateinit var viewModel: VM

    protected abstract val TAG: String

    @LayoutRes
    protected abstract fun getLayoutId(): Int

    protected abstract fun getViewModel(): Class<VM>

    protected abstract fun getBindingVariable(): Int

    protected abstract fun initObserver()

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        performDataBinding()
        initObserver()
    }

    private fun performDataBinding() {
        performViewDataBinding()
        performAppBarDataBinding()
    }

    private fun performViewDataBinding() {
        binding = DataBindingUtil.setContentView(this, getLayoutId())
        this.viewModel = if(::viewModel.isInitialized) viewModel else ViewModelProviders.of(this).get(getViewModel())
        binding.lifecycleOwner = this
        binding.setVariable(getBindingVariable(), viewModel)
        binding.executePendingBindings()
    }

    private fun performAppBarDataBinding() {
        val appBarBinding: AppBarBinding
        try {
            val appBarField = binding.javaClass.getField("appbarLayout")
            appBarBinding = appBarField.get(binding) as AppBarBinding
            setSupportActionBar(appBarBinding.toolbar)
        }
        catch (e: Exception) {

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if(::binding.isInitialized) binding.unbind()
    }

    override fun onStop() {
        super.onStop()

        val mngr = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

        val taskList: List<ActivityManager.RunningTaskInfo> = mngr.getRunningTasks(10)

        if (taskList[0].topActivity!!.className == "com.sec.android.app.launcher.activities.LauncherActivity"
                || taskList[0].topActivity!!.className == TAG) {
            onHome()
        }
    }

    /**
     * Home 키를 눌렀을 때 실행
     */
    protected open fun onHome() { }

    override fun setRequestedOrientation(requestedOrientation: Int) {
        if (VERSION.SDK_INT != VERSION_CODES.O) {
            super.setRequestedOrientation(requestedOrientation)
        }
    }

    companion object {
        fun <T : Fragment?> newInstance(fragment: T): T {
            val args = Bundle()
            fragment!!.arguments = args
            return fragment
        }
    }
}