package com.afirez.knight.core.internal

import android.content.Context
import com.afirez.knight.core.api.BuildOkHttpClient
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

class BuildOkHttpClientImpl: BuildOkHttpClient {
    override fun buildOkHttpClient(context: Context, builder: OkHttpClient.Builder) {
        builder.addInterceptor(HttpLoggingInterceptor())
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
    }
}