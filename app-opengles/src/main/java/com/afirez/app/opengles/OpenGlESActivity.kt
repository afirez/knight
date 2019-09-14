package com.afirez.app.opengles

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.GLUtils
import android.opengl.Matrix
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import org.intellij.lang.annotations.Language
import java.io.IOException
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class OpenGlESActivity : AppCompatActivity() {

    lateinit var glSurfaceView: GLSurfaceView

    lateinit var scaleGestureDetector: ScaleGestureDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_gl_es)
        glSurfaceView = findViewById(R.id.glSurfaceView)

        glSurfaceView.apply {
            setEGLContextClientVersion(2)
            setPreserveEGLContextOnPause(true)
            setRenderer(renderer)
            setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY)
            setOnTouchListener(onTouchListener)
        }

        scaleGestureDetector = ScaleGestureDetector(this, onScaleGestureListener)
    }

    override fun onResume() {
        super.onResume()
        glSurfaceView.onResume()
    }

    override fun onPause() {
        super.onPause()
        glSurfaceView.onPause()
    }

    val renderer = object : GLSurfaceView.Renderer {
        override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
            //init
            GLES20.glClearColor(0f, 0f, 0f, 0f)
            Matrix.setRotateM(rotationMatrix, 0, 0f, 0f, 0f, 1.0f)

            //load the picture into a texture that OpenGL will be able to use
            val bitmap = loadBitmapFromAssets("logo.png")

            val fboId = createFboTexture(bitmap.width, bitmap.height)

            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, fboId)
            GLUtils.texSubImage2D(GLES20.GL_TEXTURE_2D, 0, 0, 0, bitmap)

            // load and compile shader
            // link program
            val vShaderSrc = VERTEX_SHADER
            val fShaderSrc = FRAGMENT_SHADER

            val vShaderId: Int
            val fShaderId: Int
            val programId: Int
            val linkStatus = IntArray(1)

            vShaderId = loadShader(vShaderSrc, GLES20.GL_VERTEX_SHADER)
            fShaderId = loadShader(fShaderSrc, GLES20.GL_FRAGMENT_SHADER)


            programId = GLES20.glCreateProgram()
            GLES20.glAttachShader(programId, vShaderId)
            GLES20.glAttachShader(programId, fShaderId)
            GLES20.glLinkProgram(programId)

            GLES20.glGetProgramiv(programId, GLES20.GL_LINK_STATUS, linkStatus, 0)
            if (linkStatus[0] <= 0) {
                throw RuntimeException("gl program link error")
            }

            GLES20.glDeleteShader(vShaderId)
            GLES20.glDeleteShader(fShaderId)

            GLES20.glUseProgram(programId)

            // Now that our program is loaded and in use,
            // we'll retrieve the handles of the parameters we pass to our shaders

            vPosition = GLES20.glGetAttribLocation(programId, POSITION)
            vTexturePosition = GLES20.glGetAttribLocation(programId, TEXTURE_COORDINATE)
            uMVPMatrix = GLES20.glGetUniformLocation(programId, MVP_MATRIX)


        }

        override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
            GLES20.glViewport(0, 0, width, height)

            // OpenGL will stretch what we give it into a square. To avoid this, we have to send the ratio
            // information to the VERTEX_SHADER. In our case, we pass this information (with other) in the
            // MVP Matrix as can be seen in the onDrawFrame method.
            val ratio = width.toFloat() / height
            Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 7f)

            glSurfaceView.requestRender()
        }

        override fun onDrawFrame(gl: GL10?) {
            // We have setup that the background color will be black with GLES20.glClearColor in
            // onSurfaceCreated, now is the time to ask OpenGL to clear the screen with this color
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

            // Using matrices, we set the camera at the center, advanced of 7 looking to the center back
            // of -1
            Matrix.setLookAtM(
                viewMatrix,
                0,
                0f,
                0f,
                7f,
                0f,
                0f,
                -1f,
                0f,
                1f,
                0f
            )

            // We combine the scene setup we have done in onSurfaceChanged with the camera setup
            Matrix.multiplyMM(
                mvpMatrix,
                0,
                projectionMatrix,
                0,
                viewMatrix,
                0
            )

            // We combile that with the applied rotation
            Matrix.multiplyMM(
                mvpMatrix
                ,
                0,
                mvpMatrix,
                0,
                rotationMatrix,
                0
            )

            // Finally, we apply the scale to our Matrix
            Matrix.scaleM(mvpMatrix, 0, scale, scale, scale)

            // We attach the float array containing our Matrix to the correct handle
            GLES20.glUniformMatrix4fv(uMVPMatrix, 1, false, mvpMatrix, 0)

            // We pass the buffer for the position
            positionBuffer.position(0)
            GLES20.glVertexAttribPointer(vPosition, 3, GLES20.GL_FLOAT, false, 0, positionBuffer)
            GLES20.glEnableVertexAttribArray(vPosition)

            // We pass the buffer for the texture position
            textureCoordsBuffer.position(0)
            GLES20.glVertexAttribPointer(vTexturePosition, 2, GLES20.GL_FLOAT, false, 0, textureCoordsBuffer)
            GLES20.glEnableVertexAttribArray(vTexturePosition)

            // We draw our square which will represent our logo
            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)

            GLES20.glDisableVertexAttribArray(vPosition)
            GLES20.glDisableVertexAttribArray(vTexturePosition)
        }
    }


    val onTouchListener = object : View.OnTouchListener {
        private var previousX: Float = 0f
        private var previousY: Float = 0f

        override fun onTouch(v: View?, event: MotionEvent): Boolean {
            scaleGestureDetector.onTouchEvent(event)
            if (event.getPointerCount() == 1) {
                when (event.getAction()) {
                    MotionEvent.ACTION_DOWN -> {
                        previousX = event.getX()
                        previousY = event.getY()
                    }
                    MotionEvent.ACTION_MOVE -> {
                        if (previousX != event.getX()) {
                            Matrix.rotateM(rotationMatrix, 0, event.getX() - previousX, 0f, 1f, 0f)
                        }
                        if (previousY != event.getY()) {
                            Matrix.rotateM(rotationMatrix, 0, event.getY() - previousY, 1f, 0f, 0f)
                        }
                        glSurfaceView.requestRender()
                        previousX = event.getX()
                        previousY = event.getY()
                    }
                }
            }

            return true
        }
    }

    val onScaleGestureListener = object : ScaleGestureDetector.OnScaleGestureListener {
        override fun onScaleBegin(detector: ScaleGestureDetector?): Boolean {
            return true
        }

        override fun onScaleEnd(detector: ScaleGestureDetector?) {

        }

        override fun onScale(detector: ScaleGestureDetector?): Boolean {
            if (scaleGestureDetector.scaleFactor != 0f) {
                scale *= scaleGestureDetector.scaleFactor
                glSurfaceView.requestRender()
            }

            return true
        }

    }


    private var vPosition: Int = 0
    private var vTexturePosition: Int = 0
    private var uMVPMatrix: Int = 0

    private var scale: Float = 1f

    private val mvpMatrix = FloatArray(16)
    private val projectionMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)
    private val rotationMatrix = FloatArray(16)

    companion object {

        private val MVP_MATRIX = "uMVPMatrix"
        private val POSITION = "vPosition"
        private val TEXTURE_COORDINATE = "vTextureCoordinate"

        @Language("GLSL")
        private val VERTEX_SHADER = "" +
                "precision mediump float;" +
                "uniform mat4 " + MVP_MATRIX + ";" +
                "attribute vec4 " + POSITION + ";" +
                "attribute vec4 " + TEXTURE_COORDINATE + ";" +
                "varying vec2 position;" +
                "void main(){" +
                " gl_Position = " + MVP_MATRIX + " * " + POSITION + ";" +
                " position = " + TEXTURE_COORDINATE + ".xy;" +
                "}"

        @Language("GLSL")
        private val FRAGMENT_SHADER = "" +
                "precision mediump float;" +
                "uniform sampler2D uTexture;" +
                "varying vec2 position;" +
                "void main() {" +
                "    gl_FragColor = texture2D(uTexture, position);" +
                "}"

        private val POSITION_MATRIX = floatArrayOf(
            -1f, -1f, 1f, // X1,Y1,Z1
            1f, -1f, 1f, // X2,Y2,Z2
            -1f, 1f, 1f, // X3,Y3,Z3
            1f, 1f, 1f
        )// X4,Y4,Z4

        private val TEXTURE_COORDS = floatArrayOf(
            0f, 1f, // X1,Y1
            1f, 1f, // X2,Y2
            0f, 0f, // X3,Y3
            1f, 0f
        )// X4,Y4
    }


    private val positionBuffer = ByteBuffer
        .allocateDirect(POSITION_MATRIX.size * 4)
        .order(ByteOrder.nativeOrder())
        .asFloatBuffer()
        .put(POSITION_MATRIX)

    private val textureCoordsBuffer = ByteBuffer
        .allocateDirect(TEXTURE_COORDS.size * 4)
        .order(ByteOrder.nativeOrder())
        .asFloatBuffer()
        .put(TEXTURE_COORDS)


    private fun loadShader(shaderSrc: String, type: Int): Int {
        val shaderId = GLES20.glCreateShader(type)
        GLES20.glShaderSource(shaderId, shaderSrc)
        GLES20.glCompileShader(shaderId)
        val compileStatus = IntArray(1)
        GLES20.glGetShaderiv(shaderId, GLES20.GL_COMPILE_STATUS, compileStatus, 0)
        if (compileStatus[0] == 0) {
            throw RuntimeException("Compilation failed : " + GLES20.glGetShaderInfoLog(shaderId))
        }
        return shaderId
    }

    private fun createFboTexture(width: Int, height: Int): Int {
        val temp = IntArray(1)
        GLES20.glGenFramebuffers(1, temp, 0)
        val fboId = temp[0]
        GLES20.glBindBuffer(GLES20.GL_FRAMEBUFFER, fboId)

        val textureId = createTexture(width, height)

        GLES20.glFramebufferTexture2D(
            GLES20.GL_FRAMEBUFFER,
            GLES20.GL_COLOR_ATTACHMENT0,
            GLES20.GL_TEXTURE_2D,
            textureId,
            0
        )

        if (GLES20.glCheckFramebufferStatus(GLES20.GL_FRAMEBUFFER) != GLES20.GL_FRAMEBUFFER_COMPLETE) {
            throw IllegalStateException("GL_FRAMEBUFFER status incomplete")
        }

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0)
        GLES20.glBindBuffer(GLES20.GL_FRAMEBUFFER, 0)

        return fboId
    }

    private fun createTexture(width: Int, height: Int): Int {
        val handleIds = IntArray(1)
        GLES20.glGenTextures(1, handleIds, 0)
        val textureId = handleIds[0]
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId)
        GLES20.glTexImage2D(
            GLES20.GL_TEXTURE_2D,
            0,
            GLES20.GL_RGBA,
            width,
            height,
            0,
            GLES20.GL_RGBA,
            GLES20.GL_UNSIGNED_BYTE,
            null
        )
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR)
        return textureId
    }

    private fun loadBitmapFromAssets(fileName: String): Bitmap {
        var input: InputStream? = null
        try {
            input = assets.open(fileName)
            return BitmapFactory.decodeStream(input)
        } catch (ex: IOException) {
            throw RuntimeException()
        } finally {
            if (input != null) {
                try {
                    input.close()
                } catch (ignored: IOException) {
                    //
                }

            }
        }
    }


}
