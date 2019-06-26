package com.afirez.app.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MotionEvent
import android.widget.Button
import com.afirez.app.R

class MotionEventActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_motion_event)
        findViewById<Button>(R.id.btnParentIntercept).setOnClickListener {
            val parent = findViewById<MotionParentLayout>(R.id.parent)
            parent.intercept = !parent.intercept
        }

        findViewById<Button>(R.id.btnChildDisallowIntercept).setOnClickListener {
            val child = findViewById<MotionChildLayout>(R.id.child)
            child.disallowIntercept = !child.disallowIntercept
        }
    }


    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {

        Log.w("MotionEventActivity", "dispatchTouchEvent")

        val consumed = super.dispatchTouchEvent(ev)

        Log.w("MotionEventActivity", "dispatchTouchEvent consumed = $consumed")

        return consumed
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {

        Log.w("MotionEventActivity", "onTouchEvent")

        val consumed = super.onTouchEvent(event)

        Log.w("MotionEventActivity", "onTouchEvent consumed = $consumed")

        return consumed
    }


}
