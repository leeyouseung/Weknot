package com.example.weknot_android.widget.extension

import android.content.Intent

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment

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

fun Fragment.startActivity(activity: Class<*>) {
    startActivity(Intent(context, activity))
}

fun Fragment.startActivityWithFinish(activity: Class<*>) {
    startActivityWithFinish(Intent(context, activity))
}

fun Fragment.startActivityWithFinish(intent: Intent) {
    startActivity(intent)
    activity!!.finish()
}

fun DialogFragment.startActivity(activity: Class<*>) {
    startActivity(Intent(context, activity))
}

fun DialogFragment.startActivityWithFinish(activity: Class<*>) {
    startActivityWithFinish(Intent(context, activity))
}

fun DialogFragment.startActivityWithFinish(intent: Intent) {
    startActivity(intent)
    activity!!.finish()
}