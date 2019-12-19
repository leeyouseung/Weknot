package com.example.weknot_android.widget

import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.weknot_android.R
import com.example.weknot_android.widget.extension.getParentActivity

@BindingAdapter("adapter")
fun setAdapter(view: RecyclerView, adapter: RecyclerView.Adapter<*>) {
    view.adapter = adapter
}

@BindingAdapter("mutableVisibility")
fun setMutableVisibility(view: View,  visibility: MutableLiveData<Int>?) {
    val parentActivity: AppCompatActivity = view.getParentActivity() ?: return

    visibility?.observe(parentActivity, Observer { value -> view.visibility = value?:View.VISIBLE})
}

@BindingAdapter("mutableText")
fun setMutableText(view: TextView,  text: MutableLiveData<String>?) {
    val parentActivity: AppCompatActivity = view.getParentActivity() ?: return

    text?.observe(parentActivity, Observer { value -> view.text = value?:""})
}

@BindingAdapter("mutableImageDrawable")
fun setMutableImageDrawable(view: ImageView, resid: MutableLiveData<Int>?) {
    val parentActivity: AppCompatActivity = view.getParentActivity() ?: return

    if(resid != null) {
        resid.observe(parentActivity, Observer { value -> view.setImageResource(value)})
    }
    else {
        Glide.with(view.context)
                .load(R.drawable.ic_profile)
                .into(view)
    }
}

@BindingAdapter("mutableImageUrl")
fun setMutableImageUrl(view: ImageView, url: MutableLiveData<String>?) {
    val parentActivity: AppCompatActivity = view.getParentActivity() ?: return

    val requestOptions: RequestOptions by lazy {
        RequestOptions()
                .error(R.drawable.ic_profile)
                .placeholder(R.drawable.loading)
    }

    if(url != null) {
        url.observe(parentActivity, Observer { value -> Glide.with(view.context)
                .load(value)
                .apply(requestOptions)
                .into(view)})
    }
    else {
        Glide.with(view.context)
                .load(R.drawable.ic_profile)
                .into(view)
    }
}

@BindingAdapter("mutableImageUri")
fun setMutableImageUri(view: ImageView, uri: MutableLiveData<Uri>?) {
    val parentActivity: AppCompatActivity = view.getParentActivity() ?: return

    if(uri!!.value != null) {
        uri.observe(parentActivity, Observer { value -> Glide.with(view.context)
                .load(value)
                .into(view)})
    }
    else {
        Glide.with(view.context)
                .load(R.drawable.ic_profile)
                .into(view)
    }
}

@BindingAdapter("mutablePhotoUri")
fun setMutablePhotoUri(view: ImageView, uri: MutableLiveData<Uri>?) {
    val parentActivity: AppCompatActivity = view.getParentActivity() ?: return

    if(uri!!.value != null) {
        uri.observe(parentActivity, Observer { value -> Glide.with(view.context)
                .load(value)
                .apply(RequestOptions()
                        .error(R.drawable.ic_profile))
                .into(view)})
    }
    else {
        Glide.with(view.context)
                .load(R.drawable.ic_profile)
                .into(view)
    }
}