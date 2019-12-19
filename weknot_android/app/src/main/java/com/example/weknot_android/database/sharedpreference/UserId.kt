package com.example.weknot_android.database.sharedpreference

import android.content.Context
import android.content.ContextWrapper
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor

import com.f2prateek.rx.preferences2.Preference
import com.f2prateek.rx.preferences2.RxSharedPreferences

class UserId(context: Context) : ContextWrapper(context) {
    var id: String = ""
        get() {
            val sharedPreferences: SharedPreferences = getSharedPreferences("weknot", Context.MODE_PRIVATE)
            val rxPreferences = RxSharedPreferences.create(sharedPreferences)
            val tokenObservable: Preference<String> = rxPreferences.getString("id")
            tokenObservable.asObservable().subscribe { id: String -> field = id }
            return field
        }
        set(id) {
            val sharedPreferences: SharedPreferences = getSharedPreferences("weknot", Context.MODE_PRIVATE)
            val editor: Editor = sharedPreferences.edit()
            editor.putString("id", id)
            editor.commit()
        }
}