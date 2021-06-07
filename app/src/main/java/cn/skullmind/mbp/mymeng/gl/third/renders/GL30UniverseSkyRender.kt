package cn.skullmind.mbp.mymeng.gl.third.renders
import android.content.res.Resources
import android.opengl.GLES30
import android.opengl.GLSurfaceView
import cn.skullmind.mbp.mymeng.gl.third.shape.UniverseSky
import cn.skullmind.mbp.tools.MatrixState
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class GL30UniverseSkyRender(private val resource: Resources):GLSurfaceView.Renderer {
    private lateinit var smallSky:UniverseSky
    private lateinit var bigSky:UniverseSky
    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES30.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        GLES30.glEnable(GLES30.GL_DEPTH_TEST)
        GLES30.glEnable(GLES30.GL_CULL_FACE)
        smallSky = UniverseSky(1000, resource, 1f)
        bigSky = UniverseSky(500, resource, 2f)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES30.glViewport(0, 0, width, height)
        val ratio = width.toFloat() / height.toFloat()
        MatrixState.setProjectfrustumM(-ratio, ratio, -1f, 1f, 4f, 100f)

        MatrixState.setCamera(0f, 0f, 7.2f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)
        MatrixState.setLightPosition(100f, 5f, 0f)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT or GLES30.GL_COLOR_BUFFER_BIT)
        smallSky.draw()
        bigSky.draw()
    }
}