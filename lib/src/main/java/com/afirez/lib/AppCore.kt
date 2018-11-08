package com.afirez.lib

import android.app.Application
import android.content.Context
import com.afirez.lib.api.BuildKodein
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

class AppCore : Application(), KodeinAware {
    override val kodein: Kodein = Kodein.lazy {
        bind<Context>() with singleton { this@AppCore }

        val buildKodein = BuildKodein()
//        buildKodein.prebuildKodein(this)
        buildKodein.buildKodein(this)
    }


    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()

    }
}