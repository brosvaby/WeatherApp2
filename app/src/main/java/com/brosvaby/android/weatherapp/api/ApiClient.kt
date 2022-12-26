package com.brosvaby.android.weatherapp.api

import android.content.Context
import com.google.gson.GsonBuilder
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient(val context: Context) {
    lateinit var apiService: ApiService

    init {
        buildClient()
    }

    fun buildClient() {
        val sizeOfCache = (10 * 1024 * 1024).toLong()
        val cache = Cache(context.cacheDir, sizeOfCache)

        apiService = getBuilder(BASE_URL_API).build().create(ApiService::class.java)
    }

    private fun getBuilder(url: String) = Retrofit.Builder()
        .baseUrl(url)
        .client(getHttpClient())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder().create()
            )
        )

    private fun getHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().build()
    }
}