package com.afirez.lib

import android.app.Application
import android.content.Context
import android.util.Log
import com.afirez.lib.api.AppLike
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.description
import org.kodein.di.generic.*


object PointCuts {
    const val prebuildKodein: String = "execution( * com.afirez.lib.api.BuildKodein.prebuildKodein(..))"
    const val buildKodein: String = "execution( * com.afirez.lib.api.BuildKodein.buildKodein(..))"
    const val appAttachBaseContext: String = "execution( * android.app.Application.attachBaseContext(..))"
    const val appOnCreate: String = "execution( * android.app.Application.onCreate(..))"
    const val activityOnCreate: String = "execution( * android.support.v7.app.AppCompatActivity.onCreate(..))"
    const val FragmentOnCreate: String = "execution( * android.support.v4.app.Fragment.onCreate(..))"
}

@Aspect
class AppAspect {

//    @Around(PointCuts.prebuildKodein)
//    @Throws(Throwable::class)
//    fun aopBuildKodein0Advice(joinPoint: ProceedingJoinPoint) {
//        val builder = joinPoint.args[0] as Kodein.MainBuilder
//        builder.import(core)
//        joinPoint.proceed()
//    }

    @Around(PointCuts.buildKodein)
    @Throws(Throwable::class)
    fun aopBuildKodeinAdvice(joinPoint: ProceedingJoinPoint) {
        val builder = joinPoint.args[0] as Kodein.MainBuilder
        builder.import(core)
        Log.i("DI", "---->> buildKodein $this")
        joinPoint.proceed()
        builder.import(other)
        Log.i("DI", "<<---- buildKodein $this")
    }

    @Around(PointCuts.appAttachBaseContext)
    @Throws(Throwable::class)
    fun appAttachBaseContext(joinPoint: ProceedingJoinPoint) {
        val app = joinPoint.`this` as Application
        val base = joinPoint.args[0] as Context
        val kodein = (app as KodeinAware).kodein
        Log.i("DI", kodein.container.tree.bindings.description(withOverrides = true))
        joinPoint.proceed()
        val apps: Set<AppLike> by kodein.instance()
        apps.forEach {
            Log.i("DI", "<<---- attachBaseContext: $it")
            it.attachBaseContext(app, base)
        }
    }

    @Around(PointCuts.appOnCreate)
    @Throws(Throwable::class)
    fun aopAppCoreOnCreateAdvice(joinPoint: ProceedingJoinPoint) {
        val app = joinPoint.`this` as Application
        val kodein = (app as KodeinAware).kodein
        joinPoint.proceed()
        val apps: Set<AppLike> by kodein.instance()
        apps.forEach {
            Log.i("DI", "<<---- onCreate: $it")
            it.onCreate(app)
        }
    }

    @Around(PointCuts.activityOnCreate)
    @Throws(Throwable::class)
    fun aopActivityOnCreateAdvice(joinPoint: ProceedingJoinPoint) {
        joinPoint.proceed()
        Log.i("DI", "<<---- onCreate: ${joinPoint.`this`}")
    }

    @Around(PointCuts.FragmentOnCreate)
    @Throws(Throwable::class)
    fun aopFragmentOnCreateAdvice(joinPoint: ProceedingJoinPoint) {
        joinPoint.proceed()
        Log.i("DI", "<<---- onCreate: ${joinPoint.`this`}")
    }
}

fun Context.appKodein() = (this.applicationContext as KodeinAware).kodein

val core = Kodein.Module(name = "Core") {
    bind() from setBinding<AppLike>()
}

val other = Kodein.Module(name = "Other") {
    bind<AppLike>().inSet() with singleton { App() }
}

class App : AppLike {

    override fun attachBaseContext(app: Application, base: Context?) {
        val kodein = (app as KodeinAware).kodein
        val user1 by kodein.instance<User>()
        val user2 by kodein.instance<User>()
        Log.i("app", "onCreate: ${user1 === user2}")
    }

    override fun onCreate(app: Application) {

    }

}