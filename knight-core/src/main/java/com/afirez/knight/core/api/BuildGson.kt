package com.afirez.knight.core.api

import android.content.Context
import com.google.gson.GsonBuilder

interface BuildGson {
    fun buildGson(context: Context, builder: GsonBuilder)
}