package com.example.weknot_android.widget.extension

import android.content.Context

import android.widget.Toast

import androidx.fragment.app.Fragment

fun Context.shortToast(message: String?) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.shortToast(message: Int) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.longToast(message: String?) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Context.longToast(message: Int) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Fragment.shortToast(message: String?) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun Fragment.shortToast(message: Int) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun Fragment.longToast(message: String?) {
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}

fun Fragment.longToast(message: Int) {
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}