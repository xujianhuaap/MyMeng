package cn.skullmind.mbp.mymeng.gl.second

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import cn.skullmind.mbp.mymeng.gl.second.shape.Triangle
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class GL20Renderer : GLSurfaceView.Renderer {
    private lateinit var triangle: Triangle
    private val viewMatrix = FloatArray(16)
    private val vpMatrix = FloatArray(16)
    private val projectMatrix = FloatArray(16)

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glClearColor(0F, 0F, 0F, 1F)
        triangle = Triangle()
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        val ratio: Float = width.toFloat() / height.toFloat()
        Matrix.frustumM(projectMatrix, 0, -ratio, ratio, -1F, 1F, 3F, 7F)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        Matrix.setLookAtM(
            viewMatrix, 0, 0f, 0F, -3F,
            0F, 0F, 0F, 0F, 1F, 0F
        )
        Matrix.multiplyMM(vpMatrix, 0, projectMatrix, 0, viewMatrix, 0)
        triangle.draw(vpMatrix)
    }

}