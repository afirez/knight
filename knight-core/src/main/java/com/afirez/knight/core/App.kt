package com.afirez.knight.core

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import com.afirez.knight.core.api.AppLike
import com.afirez.knight.core.api.BuildKodein
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton
import java.util.*


class App : Application(), KodeinAware {
    override val kodein: Kodein = Kodein.lazy {
        bind<Context>() with singleton { this@App }

        BuildKodein().buildKodein(this)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(base)
    }

    override fun onCreate() {
        super.onCreate()
        val apps = ServiceLoader.load(AppLike::class.java)
        apps.forEach { app -> app.onCreate(this) }
    }
}

