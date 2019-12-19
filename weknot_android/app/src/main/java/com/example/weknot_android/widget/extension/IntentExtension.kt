package com.example.weknot_android.widget.extension

import android.content.Intent

import androidx.appcompat.app.AppCompatActivity

fun AppCompatActivity.startActivity(activity: Class<*>) {
    startActivity(Intent(this, activity))
}

fun AppCompatActivity.startActivityWithFinish(activity: Class<*>) {
    startActivityWithFinish(Intent(this, activity))
}

fun AppCompatActivity.startActivityWithFinish(intent: Intent) {
    startActivity(intent)
    finish()
}
