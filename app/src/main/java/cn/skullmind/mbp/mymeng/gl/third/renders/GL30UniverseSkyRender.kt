package cn.skullmind.mbp.mymeng.gl.third.renders

import android.opengl.GLES30
import android.opengl.GLSurfaceView
import cn.skullmind.mbp.mymeng.gl.GLShape
import cn.skullmind.mbp.tools.MatrixState
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class GL30UniverseSkyRender(val shape:GLShape):GLSurfaceView.Renderer {
    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES30.glClearColor(0.8f,0.8f,0.8f,1.0f)
        GLES30.glEnable(GLES30.GL_DEPTH_TEST)
        GLES30.glEnable(GLES30.GL_CULL_FACE)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES30.glViewport(0,0,width,height)
        MatrixState.setCamera()
        MatrixState.setProjectfrustumM()
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT or GLES30.GL_COLOR_BUFFER_BIT )
        shape.draw()
    }
}