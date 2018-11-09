package com.afirez.knight.core.api

import android.content.Context
import retrofit2.Retrofit

interface BuildRetrofit {
    fun buildRetrofit(context: Context, builder: Retrofit.Builder)
}