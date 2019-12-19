package com.example.weknot_android.base.activity

import android.Manifest

import android.annotation.SuppressLint

import android.app.Activity

import android.content.Intent

import android.graphics.Bitmap
import android.graphics.BitmapFactory

import android.net.Uri

import android.os.AsyncTask

import android.provider.MediaStore

import android.widget.Toast

import androidx.databinding.ViewDataBinding

import com.example.weknot_android.base.viewmodel.BaseViewModel
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission

import java.io.IOException
import java.io.InputStream

import java.net.HttpURLConnection
import java.net.URL

import java.util.*

abstract class BasePictureActivity<VB : ViewDataBinding, VM : BaseViewModel<*>> : BaseActivity<VB, VM>() {

    private val PICK_FROM_ALBUM = 1
    private val REQUEST_IMAGE_CROP = 2

    private var bitmap: Bitmap? = null

    protected fun tedPermission() {
        val permissionListener: PermissionListener = object : PermissionListener {
            override fun onPermissionGranted() {}
            override fun onPermissionDenied(deniedPermissions: ArrayList<String?>?) {
                Toast.makeText(applicationContext, "접근을 허용해야 사진을 등록할 수 있습니다", Toast.LENGTH_LONG).show()
            }
        }
        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check()
    }

    protected fun goToAlbum() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        startActivityForResult(intent, PICK_FROM_ALBUM)
    }

    protected fun goToCropPage(tempUri: Uri?, uri: Uri?) {
        val cropIntent = Intent("com.android.camera.action.CROP")
        cropIntent.flags = Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        cropIntent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        cropIntent.setDataAndType(tempUri, "image/*")
        cropIntent.putExtra("aspectX", 1)
        cropIntent.putExtra("aspectY", 1)
        cropIntent.putExtra("scale", true)
        cropIntent.putExtra("output", uri)
        startActivityForResult(cropIntent, REQUEST_IMAGE_CROP)
    }

    protected fun initBitmapImage(src: String?) {
        BitmapTask().execute(src)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            requestNotOkEvent()
        }
        when (requestCode) {
            PICK_FROM_ALBUM -> {
                if (data == null) {
                    return
                }
                pickNextEvent(data)
            }
            REQUEST_IMAGE_CROP -> {
                cropNextEvent()
            }
        }
    }

    protected open fun requestNotOkEvent() {}

    protected open fun pickNextEvent(data: Intent) {}

    protected open fun cropNextEvent() {}

    protected open fun setBitmap(bitmap: Bitmap?) {}

    @SuppressLint("StaticFieldLeak")
    inner class BitmapTask : AsyncTask<String?, Int?, Bitmap?>() {
        override fun doInBackground(vararg url: String?): Bitmap? {
            var bitmap: Bitmap? = null
            try {
                val myFileUrl = URL(url[0])
                val conn = myFileUrl.openConnection() as HttpURLConnection
                conn.doInput = true
                conn.connect()
                val inputStream: InputStream? = conn.inputStream
                bitmap = BitmapFactory.decodeStream(inputStream)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return bitmap
        }

        override fun onPostExecute(bitmap: Bitmap?) {
            setBitmap(bitmap)
        }
    }
}