package cn.skullmind.mbp.mymeng.gl.third

import android.opengl.GLES30
import android.opengl.GLSurfaceView
import cn.skullmind.mbp.mymeng.gl.third.shape.SixPointStar
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class GL30Renderer : GLSurfaceView.Renderer {
    val view:SixPointStar = SixPointStar(0.5f,0.2f,0f)
    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES30.glClearColor(0f,0f,0f,1.0f)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES30.glViewport(0,0,width,height)
    }

    override fun onDrawFrame(gl: GL10?) {
        view.draw()
    }
}