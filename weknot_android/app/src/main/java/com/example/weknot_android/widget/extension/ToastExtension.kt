package com.example.weknot_android.widget.extension

import android.content.Context

import android.widget.Toast

fun Context.shortToast(message: String?) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.shortToast(message: Int) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.longToast(message: String?) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.longToast(message: Int) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}