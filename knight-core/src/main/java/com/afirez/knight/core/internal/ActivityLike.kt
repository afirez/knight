package com.afirez.knight.core.internal

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.afirez.knight.core.appKodein
import org.kodein.di.generic.instance
import timber.log.Timber

internal class ActivityLike: Application.ActivityLifecycleCallbacks {
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle) {
        Timber.i("onActivityCreated: %s %s", activity, savedInstanceState)
        if (activity is androidx.fragment.app.FragmentActivity) {
            val fm = activity.supportFragmentManager
            val fragmentLiKe: androidx.fragment.app.FragmentManager.FragmentLifecycleCallbacks by activity.appKodein().instance()
            val fragmentLiKes: Set<androidx.fragment.app.FragmentManager.FragmentLifecycleCallbacks> by activity.appKodein().instance()
            fm.registerFragmentLifecycleCallbacks(fragmentLiKe, true)
            fragmentLiKes.forEach {
                fm.registerFragmentLifecycleCallbacks(it, true)
            }
        }
    }

    override fun onActivityStarted(activity: Activity) {
        Timber.i("onActivityStarted: %s", activity)
    }

    override fun onActivityResumed(activity: Activity) {
        Timber.i("onActivityResumed: %s", activity)
    }

    override fun onActivityPaused(activity: Activity) {
        Timber.i("onActivityPaused: %s", activity)
    }

    override fun onActivityStopped(activity: Activity) {
        Timber.i("onActivityStopped: %s", activity)
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        Timber.i("onActivitySaveInstanceState: %s %s", activity, outState)
    }

    override fun onActivityDestroyed(activity: Activity) {
        Timber.i("onActivityDestroyed: %s", activity)
        LeakCanaryHelper.watcher.watch(activity)
    }

}