package com.example.weknot_android.viewmodel

import android.app.Application

import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat

import android.os.Environment

import androidx.lifecycle.MutableLiveData

import com.example.weknot_android.base.viewmodel.BaseViewModel
import com.example.weknot_android.widget.SingleLiveEvent

import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

import java.text.SimpleDateFormat

import java.util.*

class PictureViewModel(application: Application) : BaseViewModel<Any>(application) {

    val picture = MutableLiveData<String>()
    val bitmap = MutableLiveData<Bitmap>()

    val downloadEvent = SingleLiveEvent<File>()
    val backEvent = SingleLiveEvent<Unit>()

    fun onClickDownload() {
        download()
    }

    fun onClickBack() {
        backEvent.call()
    }

    private fun download() {
        val day = Date()
        val sdf = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.KOREA)
        val fileName = sdf.format(day).toString()

        saveBitmapToFileCache(Environment.getExternalStorageDirectory().toString() + "/Weknot", "/Download_$fileName.jpg")
    }

    private fun saveBitmapToFileCache(strFilePath: String, filename: String) {
        val file = File(strFilePath)
        if (!file.exists()) file.mkdirs()
        val fileCacheItem = File(strFilePath + filename)
        var out: OutputStream? = null
        try {
            fileCacheItem.createNewFile()
            out = FileOutputStream(fileCacheItem)
            bitmap.value!!.compress(CompressFormat.JPEG, 100, out)
            downloadEvent.value = fileCacheItem
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                out!!.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}