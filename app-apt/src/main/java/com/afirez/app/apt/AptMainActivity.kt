package com.afirez.app.apt

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.afirez.apt.api.HelloWorld

class AptMainActivity : AppCompatActivity() {

    @HelloWorld
    var name: String = "afirez"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apt_main)
        var helloWorld = AptMainActivityApi().helloWorld(this)
        Log.i("AptMainActivity", helloWorld)
    }
}
