package cn.skullmind.mbp.mymeng.gl.third

import android.content.res.Resources
import android.opengl.GLES30
import android.opengl.GLSurfaceView
import cn.skullmind.mbp.mymeng.gl.GLShape
import cn.skullmind.mbp.mymeng.gl.Shape
import cn.skullmind.mbp.tools.MatrixState
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class GL30Renderer: GLSurfaceView.Renderer {
    lateinit var shape: GLShape
    lateinit var resources:Resources

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES30.glClearColor(0.5f, 0.5f, 0.5f, 1.0f)
        shape = Shape.getSixPointStar(0.5f, 0.2f, -0.3F, resources)
        GLES30.glEnable(GLES30.GL_DEPTH_TEST)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES30.glViewport(0, 0, width, height)
        val ratio = width.toFloat() / height
        MatrixState.setProjectOrtho(-ratio, ratio, -1f, 1f, 1f, 10f)
        MatrixState.setCamera(
            0f, 0f, 3f, 0f, 0f, 0f,
            0f, 1.0f, 0.0f
        )
    }

    override fun onDrawFrame(gl: GL10?) {

        GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT or GLES30.GL_COLOR_BUFFER_BIT)
        shape!!.draw()
    }
}