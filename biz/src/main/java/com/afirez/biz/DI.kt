package com.afirez.biz

import android.app.Application
import android.content.Context
import android.util.Log
import com.afirez.lib.PointCuts
import com.afirez.lib.User
import com.afirez.lib.api.AppLike
import com.afirez.lib.appKodein
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.inSet
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

@Aspect
class AppAspect  {

    @Around(PointCuts.buildKodein)
    @Throws(Throwable::class)
    fun aopBuildKodeinAdvice(joinPoint: ProceedingJoinPoint) {
        val builder = joinPoint.args[0] as Kodein.MainBuilder
        Log.i("DI", "---->> buildKodein $this")
        joinPoint.proceed()
        builder.import(biz)
        Log.i("DI", "<<---- buildKodein $this")
    }
}

class App : AppLike {
    override fun attachBaseContext(app: Application, base: Context?) {

    }

    override fun onCreate(app: Application) {
        val user1 by app.appKodein().instance<User>()
        val user2 by app.appKodein().instance<User>()
        Log.i("DI", "onCreate: ${user1 === user2}")
    }
}

val biz = Kodein.Module(name = "Biz") {
    bind<AppLike>().inSet() with singleton { App() }
    bind<User>() with singleton { User() }
}

