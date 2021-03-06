package com.afirez.app.opengles

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnOpenGlES.setOnClickListener {
            startActivity(Intent(this@MainActivity, OpenGlESActivity::class.java))
        }

        btnTriangle.setOnClickListener {
            startActivity(Intent(this@MainActivity, TriangleActivity::class.java))
        }
    }
}
