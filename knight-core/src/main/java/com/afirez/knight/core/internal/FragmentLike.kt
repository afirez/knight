package com.afirez.knight.core.internal

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.View
import timber.log.Timber

internal class FragmentLike: FragmentManager.FragmentLifecycleCallbacks() {
    override fun onFragmentPreAttached(fm: FragmentManager, f: Fragment, context: Context) {
        Timber.i("onFragmentPreAttached: %s %s", f, fm)
    }

    override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) {
        Timber.i("onFragmentAttached: %s %s", f, fm)
    }

    override fun onFragmentPreCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        Timber.i("onFragmentPreCreated: %s %s", f, fm)
    }

    override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        Timber.i("onFragmentCreated: %s %s", f, fm)
    }

    override fun onFragmentActivityCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        Timber.i("onFragmentActivityCreated: %s %s", f, fm)
    }

    override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
        Timber.i("onFragmentViewCreated: %s %s", f, fm)
    }

    override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
        Timber.i("onFragmentStarted: %s %s", f, fm)
    }

    override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
        Timber.i("onFragmentResumed: %s %s", f, fm)
    }

    override fun onFragmentPaused(fm: FragmentManager, f: Fragment) {
        Timber.i("onFragmentPaused: %s %s", f, fm)
    }

    override fun onFragmentStopped(fm: FragmentManager, f: Fragment) {
        Timber.i("onFragmentStopped: %s %s", f, fm)
    }

    override fun onFragmentSaveInstanceState(fm: FragmentManager, f: Fragment, outState: Bundle) {
        Timber.i("onFragmentSaveInstanceState: %s %s %s", f, fm, outState)
    }

    override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
        Timber.i("onFragmentViewDestroyed: %s %s", f, fm)
    }

    override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
        Timber.i("onFragmentDestroyed: %s %s", f, fm)
    }

    override fun onFragmentDetached(fm: FragmentManager, f: Fragment) {
        Timber.i("onFragmentDetached: %s %s", f, fm)
        LeakCanaryHelper.watcher.watch(f)
    }
}