package com.afirez.knight.aop;

import android.util.Log;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class AppAspect {

    @Around("execution( * android.app.Application.onCreate(..))")
    public void aopAppOnCreateAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        Log.i("aspectj", String.format("------------>> Application.onCreate: \n%s \n%s",
                joinPoint.getSignature(), joinPoint.getThis()));
        joinPoint.proceed();
        Log.i("aspectj", "<<------------ Application.onCreate");
    }

    @Around("execution( * android.support.v4.app.FragmentActivity.onCreate(..))")
    public void aopActivityOnCreateAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        Log.i("aspectj", String.format("------------>> Activity.onCreate: \n%s \n%s \n%s",
                joinPoint.getSignature(), joinPoint.getThis(), joinPoint.getArgs()[0]));
        joinPoint.proceed();
        Log.i("aspectj", "<<------------ Activity.onCreate");
    }

    @Around("execution( * android.support.v4.app.Fragment.onCreate(..))")
    public void aopFragmentOnCreateAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        Log.i("aspectj", String.format("------------>> Fragment.onCreate: \n%s \n%s \n%s",
                joinPoint.getSignature(), joinPoint.getThis(), joinPoint.getArgs()[0]));
        joinPoint.proceed();
        Log.i("aspectj", "<<------------ Fragment.onCreate");
    }

}
