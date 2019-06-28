package com.afirez.app.audio

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.afirez.lib.player.AudioPlayer
import com.afirez.lib.player.One
import kotlinx.android.synthetic.main.audio_activity_main.*
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    val audioPlayer = AudioPlayer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.audio_activity_main)
        btnHelloWorld.setOnClickListener {
            Thread { One().helloWorld() }.run()
        }
        btnProducerCustomer.setOnClickListener {
            One().producerCustomer()
        }

        btnCppCallbackJava.setOnClickListener {
            val one = One()
            one.setOnErrorListener{ code, msg ->
                Log.i("CallbackFromC",  "" + code + " "+ msg)
            }
            one.callbackFromC()
        }

        btnAudioPlayerPrepare.setOnClickListener {
            audioPlayer.setDataSource("http://mpge.5nd.com/2015/2015-11-26/69708/1.mp3")
            audioPlayer.setOnPreparedListener {
                Timber.i("Player Prepared")
            }
            audioPlayer.prepare()
        }

        btnAudioPlayerStart.setOnClickListener {
            audioPlayer.start()
        }

        btnAudioPlayerPlayPcm.setOnClickListener {
          verifyPermission()
        }
    }


    val REQUEST_EXTERNAL_STORAGE = 1

    val PERMISSIONS_STORAGE = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    fun verifyPermission() {
        val permission = ActivityCompat.checkSelfPermission (this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                this,
                PERMISSIONS_STORAGE,
                REQUEST_EXTERNAL_STORAGE
            )
        } else {
            audioPlayer.playPcm("/storage/emulated/0/mydream.pcm")
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_EXTERNAL_STORAGE -> {
                var grant: Boolean = true
                permissions.forEachIndexed{ i, p ->
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        grant = false
                    }
                }
                if (grant) {
                    audioPlayer.playPcm("/storage/emulated/0/mydream.pcm")
                } else {
                    Timber.i("permission needed")
                }
            }
            else -> {
            }
        }
    }
}
