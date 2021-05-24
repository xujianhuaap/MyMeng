package cn.skullmind.mbp.mymeng.gl

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import cn.skullmind.mbp.mymeng.gl.shape.Triangle
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class MyGlSurfaceView(context: Context?) : GLSurfaceView(context) {
    private val render:MyGLRenderer

    init {
        setEGLContextClientVersion(2)
        render = MyGLRenderer()
        setRenderer(render)
    }

}

class MyGLRenderer : GLSurfaceView.Renderer{
    private lateinit var triangle: Triangle
    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {

        GLES20.glClearColor(0F,0F,0F,1F)
        triangle = Triangle()
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0,0,width,height)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        triangle.draw()
    }

}