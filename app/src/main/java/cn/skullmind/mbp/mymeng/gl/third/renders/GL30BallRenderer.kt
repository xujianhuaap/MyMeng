package cn.skullmind.mbp.mymeng.gl.third.renders

import android.content.res.Resources
import android.opengl.GLES30
import android.opengl.GLSurfaceView
import cn.skullmind.mbp.mymeng.gl.third.shape.Ball
import cn.skullmind.mbp.tools.MatrixState
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class GL30BallRenderer: GLSurfaceView.Renderer {
    private lateinit var shape: Ball
    lateinit var resources:Resources

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES30.glClearColor(0.5f, 0.5f, 0.5f, 1.0f)
        shape = Ball(resources)
        GLES30.glEnable(GLES30.GL_DEPTH_TEST)
        GLES30.glEnable(GLES30.GL_CULL_FACE)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        val viewWidth = width * 0.6
        val viewHeight = height * 0.6
        val xStart = (width - viewWidth) /2
        val yStart = (height - viewHeight)/2
        GLES30.glViewport(xStart.toInt(), yStart.toInt(), viewWidth.toInt(), viewHeight.toInt())
        val ratio = width.toFloat() / height
        MatrixState.setProjectfrustumM(-ratio, ratio, -1F, 1F, 20F, 100F)
        MatrixState.setCamera(0F, 0F, 30F, 0f, 0f, 0f, 0f, 1.0f, 0.0f)
        MatrixState.setInitModelMatrix()
    }

    override fun onDrawFrame(gl: GL10?) {

        GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT or GLES30.GL_COLOR_BUFFER_BIT)
        MatrixState.setLightPosition(50f, 70f, 50f)
        shape.draw()
    }

    fun refreshBallAngles(angleX: Float, angleY: Float, angleZ: Float){
        shape.refreshAngles(angleX, angleY, angleZ)
    }


}