package com.afirez.app.weex


import android.view.Gravity
import com.afirez.knight.core.PointCuts
import com.afirez.knight.core.api.AppLike

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.kodein.di.Kodein

import org.kodein.di.generic.bind
import org.kodein.di.generic.inSet
import org.kodein.di.generic.singleton
import timber.log.Timber

val weex = Kodein.Module(":weex") {
    bind<AppLike>().inSet() with singleton { AppWeex() }
}

@Aspect
class AppAspect {

    @Around(PointCuts.buildKodin)
    @Throws(Throwable::class)
    fun aopBuildKodein(joinPoint: ProceedingJoinPoint) {
        val builder = joinPoint.args[0] as Kodein.MainBuilder
        joinPoint.proceed()
        builder.import(weex)
        Timber.i("<<---- buildKodin")
    }
}