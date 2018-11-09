package com.afirez.lib

import com.afirez.knight.core.PointCuts
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton
import timber.log.Timber

val lib = Kodein.Module(":lib") {
    bind<User>(tag = "user") with singleton { User() }
}

@Aspect
class AppAspect {

    @Around(PointCuts.buildKodin)
    @Throws(Throwable::class)
    fun aopBuildKodein(joinPoint: ProceedingJoinPoint) {
        val builder = joinPoint.args[0] as Kodein.MainBuilder
        joinPoint.proceed()
        builder.import(lib)
        Timber.i("<<---- buildKodin")
    }
}