package com.afirez.app

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.afirez.knight.core.appKodein
import com.afirez.lib.User
import org.kodein.di.generic.instance

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val kodein = appKodein()
        val user by kodein.instance<User>()
        Toast.makeText(this, user.name, Toast.LENGTH_SHORT).show()
    }
}
