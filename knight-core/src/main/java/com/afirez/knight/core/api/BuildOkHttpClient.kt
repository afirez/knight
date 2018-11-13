package com.afirez.knight.core.api

import android.content.Context
import okhttp3.OkHttpClient

interface BuildOkHttpClient {
    fun buildOkHttpClient(context: Context, builder: OkHttpClient.Builder)
}