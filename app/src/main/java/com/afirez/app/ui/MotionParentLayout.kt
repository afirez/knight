package com.afirez.app.ui

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.FrameLayout

class MotionParentLayout : FrameLayout {

    var intercept = false

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        Log.w("MotionParentLayout", "intercepted = $intercept")

        Log.w("MotionParentLayout", "dispatchTouchEvent")

        val consumed = super.dispatchTouchEvent(ev)

        Log.w("MotionParentLayout", "dispatchTouchEvent consumed = $consumed")

        return consumed
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        if (intercept) {
            return true
        }

        Log.w("MotionParentLayout", "onInterceptTouchEvent")
        val intercepted = super.onInterceptTouchEvent(ev)
        Log.w("MotionParentLayout", "onInterceptTouchEvent intercepted = $intercepted")
        return intercepted
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.w("MotionParentLayout", "onTouchEvent")
        val consumed = super.onTouchEvent(event)
        Log.w("MotionParentLayout", "onTouchEvent consumed = $consumed")
        return consumed
    }
}