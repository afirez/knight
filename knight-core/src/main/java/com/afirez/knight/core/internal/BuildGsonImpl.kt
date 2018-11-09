package com.afirez.knight.core.internal

import android.content.Context
import com.afirez.knight.core.api.BuildGson
import com.google.gson.GsonBuilder

class BuildGsonImpl: BuildGson {
    override fun buildGson(context: Context, builder: GsonBuilder) {
        builder.enableComplexMapKeySerialization()
    }
}