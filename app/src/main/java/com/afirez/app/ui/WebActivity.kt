package com.afirez.app.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.afirez.app.R
import com.afirez.app.WebFragment

class WebActivity : AppCompatActivity() {

    val url = "https://robot-lib-achieve.zuoshouyisheng.com/?app_id=5cc197e8b60c48171066f0e7"
    val url1 = "http://192.168.200.25:8080/index"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
        if (supportActionBar != null) {
            supportActionBar?.hide()
        }
        val fm = supportFragmentManager
        val tag = WebFragment::class.java.name
        val fragment = fm.findFragmentByTag(tag)
        if (fragment == null) {
            fm
                .beginTransaction().add(android.R.id.content, WebFragment().apply {
                    arguments = Bundle().apply {
                        putString("url", url)
                    }
                })
                .commit()
        }
    }
}
