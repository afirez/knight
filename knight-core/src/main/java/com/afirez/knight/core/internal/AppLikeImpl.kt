package com.afirez.knight.core.internal

import android.app.Application
import android.content.Context
import com.afirez.knight.core.api.AppLike
import com.afirez.knight.core.appKodein
import com.afirez.knight.core.imageloader.IImageLoader
import com.afirez.knight.core.imageloader.ImageLoader
import com.afirez.knight.core.isDebug
import com.github.moduth.blockcanary.BlockCanary
import com.tencent.bugly.crashreport.CrashReport
import okhttp3.OkHttpClient
import org.kodein.di.generic.instance
import java.io.File

internal class AppLikeImpl : AppLike {

    override fun attachBaseContext(app: Application, base: Context?) {

    }

    override fun onCreate(app: Application) {
        val kodein = app.appKodein()
        val debug = app.isDebug()

        if (!debug) {
            val buglyAppId: String by kodein.instance(tag = "buglyAppId")
            CrashReport.initCrashReport(app, buglyAppId, debug)
        }

        LeakCanaryHelper.install(app)

        BlockCanary.install(app, BlockCanaryContextImpl()).start()


        val imageLoaders: Set<IImageLoader> by kodein.instance()
        val cache: File by kodein.instance(tag = "cache")
        val client: OkHttpClient by kodein.instance()
        ImageLoader.instance().context = app
        ImageLoader.instance().cacheDir = cache
        ImageLoader.instance().client = client
        imageLoaders.forEach { ImageLoader.instance().registerLoader(it) }
    }

}
