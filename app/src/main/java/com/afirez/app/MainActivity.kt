package com.afirez.app

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.afirez.app.ui.CustomRecyclerViewActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnCumtomLayoutManager.setOnClickListener {
            startActivity(Intent(this@MainActivity, CustomRecyclerViewActivity::class.java))
        }
    }
}
