package com.afirez.app.audio

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
    }
}
