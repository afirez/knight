package com.afirez.app.audio

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.afirez.lib.player.One
import kotlinx.android.synthetic.main.audio_activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.audio_activity_main)
        btnHelloWorld.setOnClickListener {
            Thread { One.apply() }.run()
        }

    }
}
