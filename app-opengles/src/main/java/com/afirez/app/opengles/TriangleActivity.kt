package com.afirez.app.opengles

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class TriangleActivity : AppCompatActivity(), GLSurfaceView.Renderer {


    private lateinit var glSurfaceView: GLSurfaceView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        glSurfaceView = GLSurfaceView(this)
        glSurfaceView.apply {
            setEGLConfigChooser(8, 8, 8, 0, 16, 0)
            setEGLContextClientVersion(3)
            setRenderer(this@TriangleActivity)
        }
        setContentView(glSurfaceView)
    }

    override fun onResume() {
        super.onResume()
        glSurfaceView.onResume()
    }

    override fun onPause() {
        super.onPause()
        glSurfaceView.onPause()
    }

    override fun onDrawFrame(gl10: GL10?) {
        Triangle.draw()
    }

    override fun onSurfaceChanged(gl10: GL10?, width: Int, height: Int) {
        Triangle.resize(width, height)
    }

    override fun onSurfaceCreated(gl10: GL10?, config: EGLConfig?) {
        Triangle.init()
    }
}
