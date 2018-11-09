package com.afirez.knight.core.api

import android.app.Application
import android.content.Context

interface AppLike {
    fun attachBaseContext(app: Application, base: Context?)

    fun onCreate(app: Application)
}