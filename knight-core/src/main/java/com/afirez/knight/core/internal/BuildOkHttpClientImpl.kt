package com.afirez.knight.core.internal

import   android.content.Context
import com.afirez.knight.core.api.BuildOkHttpClient
import com.afirez.knight.core.isDebug
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

class BuildOkHttpClientImpl : BuildOkHttpClient {
    override fun buildOkHttpClient(context: Context, builder: OkHttpClient.Builder) {
        if (context.isDebug()) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(loggingInterceptor)
        }
        builder.readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
    }
}