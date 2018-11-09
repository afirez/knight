package com.afirez.knight.core.internal

import android.util.Log
import timber.log.Timber

class TimberTreeImpl : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority == Log.VERBOSE || priority == Log.DEBUG) {
            return
        }

        log(priority, tag, message)

        if (t != null) {
            if (priority == Log.ERROR) {
                logError(t)
            } else if (priority == Log.WARN) {
               logWarning(t)
            }
        }
    }

    companion object {
        fun log(priority: Int, tag: String, message: String) {

        }

        fun logWarning(t: Throwable) {

        }

        fun logError(t: Throwable) {

        }
    }
}