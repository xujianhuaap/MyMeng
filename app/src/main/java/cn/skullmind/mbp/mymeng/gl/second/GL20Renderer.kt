package cn.skullmind.mbp.mymeng.gl.second

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import cn.skullmind.mbp.mymeng.gl.second.shape.Triangle
import cn.skullmind.mbp.tools.MatrixState
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class GL20Renderer : GLSurfaceView.Renderer {
    private lateinit var triangle: Triangle


    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glClearColor(0F, 0F, 0F, 1F)
        triangle = Triangle()
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        val ratio: Float = width.toFloat() / height.toFloat()
        //正交转换
        MatrixState.setProjectOrtho(-ratio, ratio, -1f, 1f, 1f, 10f)
        MatrixState.setCamera(0F, 0f, 0F, -3F,
            0F, 0F, 0F, 0F, 1F)
        //透视转换
        MatrixState.setProjectfrustumM(-ratio, ratio, -1F, 1F, 3F, 7F)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        triangle.draw()
    }

}