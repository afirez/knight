package com.afirez.app.ui

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.FrameLayout

class MotionChildLayout : FrameLayout {

    var disallowIntercept = false

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {

        Log.w("MotionChildLayout@$tag", "dispatchTouchEvent")

        val consumed = super.dispatchTouchEvent(ev)

        Log.w("MotionChildLayout@$tag", "dispatchTouchEvent consumed = $consumed")

        return true
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        if (disallowIntercept) {
            return true
        }

        Log.w("MotionChildLayout@$tag", "onInterceptTouchEvent")
        val intercepted = super.onInterceptTouchEvent(ev)
        Log.w("MotionChildLayout@$tag", "onInterceptTouchEvent intercepted = $intercepted")
        return intercepted
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.w("MotionChildLayout@$tag", "disallowIntercept = $disallowIntercept")
        requestDisallowInterceptTouchEvent(disallowIntercept)

        Log.w("MotionChildLayout@$tag", "onTouchEvent")
        val consumed = super.onTouchEvent(event)
        Log.w("MotionChildLayout@$tag", "onTouchEvent consumed = $consumed")
        return consumed
    }
}