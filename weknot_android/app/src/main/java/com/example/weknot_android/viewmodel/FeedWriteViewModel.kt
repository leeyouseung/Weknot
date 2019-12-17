package com.example.weknot_android.viewmodel

import android.app.Application

import android.content.Intent

import android.net.Uri

import android.os.Environment

import androidx.lifecycle.MutableLiveData

import com.example.weknot_android.base.viewmodel.BaseViewModel
import com.example.weknot_android.network.comm.FeedComm
import com.example.weknot_android.widget.SingleLiveEvent

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody

import java.io.File
import java.io.IOException

import java.lang.NullPointerException

import java.util.*

class FeedWriteViewModel(application: Application) : BaseViewModel<Any>(application) {

    private val feedComm = FeedComm()

    val tempPictureUri: MutableLiveData<Uri> = MutableLiveData()
    val pictureUri: MutableLiveData<Uri> = MutableLiveData()
    private val pictureFile: MutableLiveData<File> = MutableLiveData()
    private val picture: MutableLiveData<MultipartBody.Part> = MutableLiveData()

    val commentText: MutableLiveData<String> = MutableLiveData()
    private val comment: MutableLiveData<RequestBody> = MutableLiveData()

    val nullPointerException = SingleLiveEvent<Unit>()
    val goToCrop: SingleLiveEvent<Unit> = SingleLiveEvent()
    val backMessageToast: SingleLiveEvent<Unit> = SingleLiveEvent()
    val openMain: SingleLiveEvent<Unit> = SingleLiveEvent()

    fun postFeed() {
        if (!setRequest()) return
        addDisposable(feedComm.postFeed(token, picture.value!!, comment.value!!), baseObserver)
    }

    fun savePickData(data: Intent) {
        tempPictureUri.value = data.data
    }

    fun cropImage() {
        createFile()
        goToCrop.call()
    }

    private fun createFile() {
        val file = File(Environment.getExternalStorageDirectory().toString() + "/Weknot")
        if (!file.exists()) file.mkdirs()
        pictureFile.value = File(Environment.getExternalStorageDirectory().toString() + "/Weknot/" + Random().nextInt(999999999).toString() + ".jpg")
        try {
            pictureFile.value!!.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        pictureUri.value = Uri.fromFile(pictureFile.value)
    }

    private fun setRequest(): Boolean {
        try {
            val requestFile: RequestBody = RequestBody.create("image/*".toMediaTypeOrNull(), pictureFile.value!!)
            picture.value = MultipartBody.Part.createFormData("picture", pictureFile.value!!.name, requestFile)
            comment.value = RequestBody.create("text/plain".toMediaTypeOrNull(),commentText.value!!)
            return true
        }
        catch (e: NullPointerException) {
            nullPointerException.call()
        }
        return false
    }

    fun deleteFile() {
        tempPictureUri.value = null
        pictureFile.value = null
        pictureUri.value = null
        backMessageToast.call()
    }

    override fun onRetrieveBaseSuccess(message: String) {
        openMain.call()
    }
}