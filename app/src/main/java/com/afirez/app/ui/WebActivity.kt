package com.afirez.app.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.afirez.app.R
import com.afirez.app.WebFragment

class WebActivity : AppCompatActivity() {

    val url = "http://192.168.200.25:8080/index"
    val url0 = "https://robot-lib-achieve.zuoshouyisheng.com/?app_id=5cc197e8b60c48171066f0e7"
    val url1 = "https://robot-lib-achieve.zuoshouyisheng.com/?app_id=5cd3d5cbb60c48343fafe493"

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
                        val type = intent.getIntExtra("type", 0)
                        val url = if(type == 0) {
                            url0
                        } else {
                            url1
                        }
                        putString("url", url)
                    }
                })
                .commit()
        }
    }
}
