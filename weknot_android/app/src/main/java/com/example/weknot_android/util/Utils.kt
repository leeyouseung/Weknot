package com.example.weknot_android.util

import com.example.weknot_android.BuildConfig

import com.google.gson.GsonBuilder

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level

import retrofit2.Retrofit
import retrofit2.Retrofit.Builder
import retrofit2.converter.gson.GsonConverterFactory

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

object Utils {
    var RETROFIT: Retrofit = Builder()
            .client(client)
            .baseUrl(Constants.MAIN_HOST)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .callbackExecutor(Executors.newSingleThreadExecutor())
            .build()

    private val client: OkHttpClient
        get() {
            val builder = OkHttpClient.Builder()
                    .connectTimeout(1, TimeUnit.HOURS)
                    .writeTimeout(1, TimeUnit.HOURS)
                    .readTimeout(1, TimeUnit.HOURS)

            if (BuildConfig.DEBUG) {
                val httpLoggingInterceptor = HttpLoggingInterceptor()
                builder.addInterceptor(httpLoggingInterceptor.apply { httpLoggingInterceptor.level = Level.BODY })
            }
            return builder.build()
        }

    @Throws(NoSuchAlgorithmException::class)
    fun encryptSHA512(target: String): String {
        val messageDigest: MessageDigest = MessageDigest.getInstance("SHA-512")
        val encryptedPassword = StringBuilder()
        messageDigest.update(target.toByteArray())
        val buffer: ByteArray = messageDigest.digest()
        for (temp in buffer) {
            var sb = StringBuilder(Integer.toHexString(temp.toInt()))
            while (sb.length < 2) {
                sb.insert(0, "0")
            }
            sb = StringBuilder(sb.substring(sb.length - 2))
            encryptedPassword.append(sb)
        }
        return encryptedPassword.toString()
    }
}