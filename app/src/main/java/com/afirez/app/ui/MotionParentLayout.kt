package com.afirez.app.ui

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import java.util.ArrayList

class MotionParentLayout : FrameLayout {

    val touchTarget = Class.forName("android.view.ViewGroup\$TouchTarget")

    var intercept = false

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        Log.w("MotionParentLayout", "intercepted = $intercept")

        Log.w("MotionParentLayout", "dispatchTouchEvent")

        val consumed = super.dispatchTouchEvent(ev)

        Log.w("MotionParentLayout", "dispatchTouchEvent consumed = $consumed")

        try {

            val field = ViewGroup::class.java.getDeclaredField("mFirstTouchTarget")
            val childField = touchTarget.getDeclaredField("child") ?: null
            val nextField = touchTarget.getDeclaredField("next") ?: null

            field.isAccessible = true
            childField?.isAccessible = true
            nextField?.isAccessible = true

            val mFirstTouchTarget = field.get(this)

            var p = mFirstTouchTarget

            while (p != null) {
                var child = childField?.get(p)
                Log.w("MotionParentLayout", "child = $child")
                p = nextField?.get(p)
            }


            val m = ViewGroup::class.java.getDeclaredMethod("buildOrderedChildList")
            m.isAccessible = true
            val preorderedList: ArrayList<View> = m.invoke(this) as ArrayList<View>
            Log.w("MotionParentLayout", "$preorderedList")


        } catch (e: Throwable) {
            e.printStackTrace()
        }

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