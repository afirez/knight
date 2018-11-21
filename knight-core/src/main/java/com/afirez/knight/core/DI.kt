package com.afirez.knight.core

import android.app.Application
import android.content.Context
import android.content.pm.ApplicationInfo
import android.support.v4.app.FragmentManager
import com.afirez.knight.core.api.*
import com.afirez.knight.core.imageloader.IImageLoader
import com.afirez.knight.core.internal.*
import com.afirez.knight.core.utils.FileUtils
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.rx_cache2.internal.RxCache
import io.victoralbertos.jolyglot.GsonSpeaker
import io.victoralbertos.jolyglot.JolyglotGenerics
import okhttp3.OkHttpClient
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.*
import retrofit2.Retrofit
import timber.log.Timber
import java.io.File

val core = Kodein.Module("core") {
//    bind(tag = "any") from setBinding<Any>()
    bind() from setBinding<AppLike>()
    bind<Application.ActivityLifecycleCallbacks>(tag = "internal") with singleton { ActivityLike() }
    bind<FragmentManager.FragmentLifecycleCallbacks>(tag = "internal") with singleton { FragmentLike() }
    bind() from setBinding<FragmentManager.FragmentLifecycleCallbacks>()
    bind() from setBinding<BuildGson>()
    bind() from setBinding<BuildOkHttpClient>()
    bind() from setBinding<BuildRetrofit>()
    bind() from setBinding<BuildRxCache>()
    bind() from setBinding<IImageLoader>()
    bind<File>(tag = "cache") with singleton { FileUtils.getCacheDirectory(instance(), "") }
    bind<File>(tag = "rxCache") with singleton { File(instance<File>(tag = "cache"), "RxCache") }

    bind<GsonBuilder>() with singleton { GsonBuilder() }
    bind<Gson>() with singleton {
        val gsonBuilder: GsonBuilder by kodein.instance()
        val buildGsons: Set<BuildGson> by kodein.instance()
        buildGsons.forEach { it.buildGson(instance(), gsonBuilder) }
        gsonBuilder.create()
    }

    bind<OkHttpClient.Builder>() with singleton { OkHttpClient.Builder() }
    bind<OkHttpClient>() with singleton {
        val okHttpClientBuilder: OkHttpClient.Builder by kodein.instance()
        val buildOkHttpClients: Set<BuildOkHttpClient> by kodein.instance()
        buildOkHttpClients.forEach { it.buildOkHttpClient(instance(), okHttpClientBuilder) }
        okHttpClientBuilder.build()
    }

    bind<Retrofit.Builder>() with singleton { Retrofit.Builder() }
    bind<Retrofit>() with singleton {
        val retrofitBuilder: Retrofit.Builder by kodein.instance()
        val buildRetrofits: Set<BuildRetrofit> by kodein.instance()
        buildRetrofits.forEach { it.buildRetrofit(instance(), retrofitBuilder) }
        retrofitBuilder.build()
    }

    bind<RxCache.Builder>() with singleton { RxCache.Builder() }
    bind<JolyglotGenerics>() with singleton { GsonSpeaker(instance()) }
    bind<RxCache>() with singleton {
        val rxCacheDir: File by kodein.instance("rxCache")
        val rxCacheBuilder: RxCache.Builder by kodein.instance()
        val buildRxCaches: Set<BuildRxCache> by kodein.instance()
        buildRxCaches.forEach { it.buildRxCache(instance(), rxCacheBuilder) }
        rxCacheBuilder.persistence(rxCacheDir, instance())
    }
}

val internal = Kodein.Module("internal") {
    bind<AppLike>().inSet() with singleton { AppLikeImpl() }
    bind<BuildGson>().inSet() with singleton { BuildGsonImpl() }
    bind<BuildOkHttpClient>().inSet() with singleton { BuildOkHttpClientImpl() }
    bind<BuildRetrofit>().inSet() with singleton { BuildRetrofitImpl() }
    bind<BuildRxCache>().inSet() with singleton { BuildRxCacheImpl() }
    bind<String>(tag = "buglyAppId") with singleton { instance<Context>().getString(R.string.app_id_bugly) }
}

object PointCuts {
    const val buildKodin: String = "execution( * com.afirez.knight.core.api.BuildKodein.buildKodein(..))"
    const val appAttachBaseContext: String = "execution( * android.app.Application.attachBaseContext(..))"
    const val appOnCreate: String = "execution( * android.app.Application.onCreate(..))"
    const val activityOnCreate: String = "execution( * android.support.v4.app.FragmentActivity.onCreate(..))"
    const val fragmentOnCreate: String = "execution( * android.support.v4.app.Fragment.onCreate(..))"
}

@Aspect
class AppAspect {

    @Around(PointCuts.buildKodin)
    @Throws(Throwable::class)
    fun aopBuildKodein(joinPoint: ProceedingJoinPoint) {
        val builder = joinPoint.args[0] as Kodein.MainBuilder

        builder.import(core)
        builder.import(internal)

        joinPoint.proceed()
        Timber.i("<<---- buildKodin")
    }

    @Around(PointCuts.appAttachBaseContext)
    @Throws(Throwable::class)
    fun aopAppAttachBaseContextAdvice(joinPoint: ProceedingJoinPoint) {
        val app = joinPoint.`this` as Application
        val base = joinPoint.args[0] as Context
        joinPoint.proceed()
        val debug = app.isDebug()
        if (debug) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(TimberTreeImpl())
        }
        val kodein = (app as KodeinAware).kodein
        val apps: Set<AppLike> by kodein.instance()
        apps.forEach {
            it.attachBaseContext(app, base)
            Timber.i("<<---- app.attachBaseContext: $it")
        }
    }

    @Around(PointCuts.appOnCreate)
    @Throws(Throwable::class)
    fun aopAppOnCreateAdvice(joinPoint: ProceedingJoinPoint) {
        val app = joinPoint.`this` as Application
        val kodein = (app as KodeinAware).kodein
        joinPoint.proceed()
        val apps: Set<AppLike> by kodein.instance()
        apps.forEach {
            it.onCreate(app)
            Timber.i("<<---- app.onCreate: $it")
        }
    }

//    @Around(PointCuts.activityOnCreate)
//    @Throws(Throwable::class)
//    fun aopActivityOnCreateAdvice(joinPoint: ProceedingJoinPoint) {
//        joinPoint.proceed()
//    }
//
//    @Around(PointCuts.fragmentOnCreate)
//    @Throws(Throwable::class)
//    fun aopFragmentOnCreateAdvice(joinPoint: ProceedingJoinPoint) {
//        joinPoint.proceed()
//    }
}

fun Context.appKodein() = if (this is Application) {
    (this as KodeinAware).kodein
} else {
    (this.applicationContext as KodeinAware).kodein
}

fun Context.isDebug(): Boolean = this.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
