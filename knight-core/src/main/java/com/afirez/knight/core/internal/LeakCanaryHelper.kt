package com.afirez.knight.core.internal

import android.app.Application
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher

object LeakCanaryHelper {

    lateinit var watcher: RefWatcher

    fun install(app: Application) {
        watcher = if (LeakCanary.isInAnalyzerProcess(app)) {
            RefWatcher.DISABLED
        } else {
            LeakCanary.install(app)
        }
    }
}