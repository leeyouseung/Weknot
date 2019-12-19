package com.example.weknot_android.base.viewmodel

import androidx.lifecycle.ViewModel

import java.lang.ref.WeakReference

abstract class BaseItemViewModel<T, N> : ViewModel() {

    private lateinit var navigator: WeakReference<N>

    fun getNavigator(): N {
        return navigator.get()!!
    }
    fun setNavigator(navigator: N) {
        this.navigator = WeakReference(navigator)
    }

    abstract fun bind(data: T)
}