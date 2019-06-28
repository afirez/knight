package com.afirez.knight.core.internal

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import android.view.View
import timber.log.Timber

internal class FragmentLike: androidx.fragment.app.FragmentManager.FragmentLifecycleCallbacks() {
    override fun onFragmentPreAttached(fm: androidx.fragment.app.FragmentManager, f: androidx.fragment.app.Fragment, context: Context) {
        Timber.i("onFragmentPreAttached: %s %s", f, fm)
    }

    override fun onFragmentAttached(fm: androidx.fragment.app.FragmentManager, f: androidx.fragment.app.Fragment, context: Context) {
        Timber.i("onFragmentAttached: %s %s", f, fm)
    }

    override fun onFragmentPreCreated(fm: androidx.fragment.app.FragmentManager, f: androidx.fragment.app.Fragment, savedInstanceState: Bundle?) {
        Timber.i("onFragmentPreCreated: %s %s", f, fm)
    }

    override fun onFragmentCreated(fm: androidx.fragment.app.FragmentManager, f: androidx.fragment.app.Fragment, savedInstanceState: Bundle?) {
        Timber.i("onFragmentCreated: %s %s", f, fm)
    }

    override fun onFragmentActivityCreated(fm: androidx.fragment.app.FragmentManager, f: androidx.fragment.app.Fragment, savedInstanceState: Bundle?) {
        Timber.i("onFragmentActivityCreated: %s %s", f, fm)
    }

    override fun onFragmentViewCreated(fm: androidx.fragment.app.FragmentManager, f: androidx.fragment.app.Fragment, v: View, savedInstanceState: Bundle?) {
        Timber.i("onFragmentViewCreated: %s %s", f, fm)
    }

    override fun onFragmentStarted(fm: androidx.fragment.app.FragmentManager, f: androidx.fragment.app.Fragment) {
        Timber.i("onFragmentStarted: %s %s", f, fm)
    }

    override fun onFragmentResumed(fm: androidx.fragment.app.FragmentManager, f: androidx.fragment.app.Fragment) {
        Timber.i("onFragmentResumed: %s %s", f, fm)
    }

    override fun onFragmentPaused(fm: androidx.fragment.app.FragmentManager, f: androidx.fragment.app.Fragment) {
        Timber.i("onFragmentPaused: %s %s", f, fm)
    }

    override fun onFragmentStopped(fm: androidx.fragment.app.FragmentManager, f: androidx.fragment.app.Fragment) {
        Timber.i("onFragmentStopped: %s %s", f, fm)
    }

    override fun onFragmentSaveInstanceState(fm: androidx.fragment.app.FragmentManager, f: androidx.fragment.app.Fragment, outState: Bundle) {
        Timber.i("onFragmentSaveInstanceState: %s %s %s", f, fm, outState)
    }

    override fun onFragmentViewDestroyed(fm: androidx.fragment.app.FragmentManager, f: androidx.fragment.app.Fragment) {
        Timber.i("onFragmentViewDestroyed: %s %s", f, fm)
    }

    override fun onFragmentDestroyed(fm: androidx.fragment.app.FragmentManager, f: androidx.fragment.app.Fragment) {
        Timber.i("onFragmentDestroyed: %s %s", f, fm)
    }

    override fun onFragmentDetached(fm: androidx.fragment.app.FragmentManager, f: androidx.fragment.app.Fragment) {
        Timber.i("onFragmentDetached: %s %s", f, fm)
        LeakCanaryHelper.watcher.watch(f)
    }
}