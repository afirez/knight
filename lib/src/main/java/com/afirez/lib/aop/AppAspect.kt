package com.afirez.lib.aop

import android.util.Log
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

@Aspect
class AppAspect : KodeinAware {
    override val kodein: Kodein = Kodein.lazy { bind<User>(tag = "user") with singleton { User() } }

    val user: User by instance(tag = "user")

    @Around("execution( * android.app.Application.onCreate(..))")
    @Throws(Throwable::class)
    fun aopAppOnCreateAdvice(joinPoint: ProceedingJoinPoint) {
        Log.i("aspectk", "------------>> Application.onCreate: \n${joinPoint.signature} \n${joinPoint.`this`}")
        joinPoint.proceed()
        Log.i("aspectk", "<<------------ Application.onCreate ${user.name}")
    }

    @Around("execution( * android.support.v4.app.FragmentActivity.onCreate(..))")
    @Throws(Throwable::class)
    fun aopActivityOnCreateAdvice(joinPoint: ProceedingJoinPoint) {
        Log.i("aspectk", "------------>> Activity.onCreate: \n${joinPoint.signature} \n${joinPoint.`this`} \n${joinPoint.args[0]}")
        joinPoint.proceed()
        Log.i("aspectk", "<<------------ Activity.onCreate")
    }

    @Around("execution( * android.support.v4.app.Fragment.onCreate(..))")
    @Throws(Throwable::class)
    fun aopFragmentOnCreateAdvice(joinPoint: ProceedingJoinPoint) {
        Log.i("aspectk", "------------>> Fragment.onCreate: \n${joinPoint.signature} \n${joinPoint.`this`} \n${joinPoint.args[0]}")
        joinPoint.proceed()
        Log.i("aspectk", "<<------------ Fragment.onCreate")
    }

}