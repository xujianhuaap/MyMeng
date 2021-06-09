package cn.skullmind.mbp.mymeng.gl.third.renders

import android.content.res.Resources
import android.graphics.BitmapFactory
import android.opengl.GLES30
import android.opengl.GLSurfaceView
import android.opengl.GLUtils
import cn.skullmind.mbp.mymeng.R
import cn.skullmind.mbp.mymeng.gl.third.shape.Earth
import cn.skullmind.mbp.mymeng.gl.third.shape.Moon
import cn.skullmind.mbp.mymeng.gl.third.shape.UniverseSky
import cn.skullmind.mbp.tools.MatrixState
import java.io.IOException
import java.io.InputStream
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class GL30UniverseSkyRender(private val resource: Resources) : GLSurfaceView.Renderer {
    private lateinit var smallSky: UniverseSky
    private lateinit var bigSky: UniverseSky
    private lateinit var earth: Earth
    private lateinit var moon:Moon
    private var cAngle: Float = 0f
    private var eAngle: Float = 0f
    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES30.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        GLES30.glEnable(GLES30.GL_DEPTH_TEST)
        GLES30.glEnable(GLES30.GL_CULL_FACE)
        smallSky = UniverseSky(1000, resource, 1f)
        bigSky = UniverseSky(500, resource, 2f)
        earth = Earth(2.0f, resource)
        moon = Moon(1.0f,resource)
        MatrixState.setInitModelMatrix()
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES30.glViewport(0, 0, width, height)
        val ratio = width.toFloat() / height.toFloat()
        MatrixState.setProjectfrustumM(-ratio, ratio, -1f, 1f, 4f, 100f)

        MatrixState.setCamera(0f, 0f, 7.2f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)
        MatrixState.setLightPosition(100f, 5f, -0f)
        earth.texDayId = initTexture(R.mipmap.earth)
        earth.texNightId = initTexture(R.mipmap.earthn)
        moon.texId = initTexture(R.mipmap.moon)
        object : Thread() {
            override fun run() {
                while (threadFlag) {
                    eAngle = (eAngle + 2) % 360
                    cAngle = (cAngle + 0.2f) % 360
                    try {
                        sleep(100)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }
            }
        }.start()
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT or GLES30.GL_COLOR_BUFFER_BIT)
        smallSky.cAngle = cAngle
        bigSky.cAngle = cAngle
        earth.eAngle = eAngle
        moon.eAngle = eAngle

        MatrixState.pushMatrix()
        earth.draw()
        moon.draw()
        MatrixState.popMatrix()
        MatrixState.pushMatrix()
        smallSky.draw()
        bigSky.draw()
        MatrixState.popMatrix()
    }

    private fun initTexture(drawableId: Int): Int //textureId
    {
        val textures = IntArray(1)
        GLES30.glGenTextures(
            1,
            textures,
            0
        )
        val textureId = textures[0]
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureId)
        GLES30.glTexParameterf(
            GLES30.GL_TEXTURE_2D,
            GLES30.GL_TEXTURE_MIN_FILTER,
            GLES30.GL_NEAREST.toFloat()
        )
        GLES30.glTexParameterf(
            GLES30.GL_TEXTURE_2D,
            GLES30.GL_TEXTURE_MAG_FILTER,
            GLES30.GL_LINEAR.toFloat()
        )
        GLES30.glTexParameterf(
            GLES30.GL_TEXTURE_2D,
            GLES30.GL_TEXTURE_WRAP_S,
            GLES30.GL_CLAMP_TO_EDGE.toFloat()
        )
        GLES30.glTexParameterf(
            GLES30.GL_TEXTURE_2D,
            GLES30.GL_TEXTURE_WRAP_T,
            GLES30.GL_CLAMP_TO_EDGE.toFloat()
        )

        val inputStream: InputStream = resource.openRawResource(drawableId)
        val bitmapTmp = try {
            BitmapFactory.decodeStream(inputStream)
        } finally {
            try {
                inputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        GLUtils.texImage2D(
            GLES30.GL_TEXTURE_2D,
            0,
            bitmapTmp,
            0
        )
        bitmapTmp.recycle()
        return textureId
    }

    companion object{
         var threadFlag = true
    }
}