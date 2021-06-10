package cn.skullmind.mbp.mymeng.gl.third.renders

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.opengl.GLES30
import android.opengl.GLSurfaceView
import android.opengl.GLUtils
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import cn.skullmind.mbp.mymeng.R
import cn.skullmind.mbp.mymeng.gl.third.shape.Earth
import cn.skullmind.mbp.mymeng.gl.third.shape.Moon
import cn.skullmind.mbp.mymeng.gl.third.shape.UniverseSky
import cn.skullmind.mbp.mymeng.gl.third.universe.UniverseAngles
import cn.skullmind.mbp.tools.MatrixState
import java.io.IOException
import java.io.InputStream
import java.time.Duration
import java.util.*
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.concurrent.timerTask

class GL30UniverseSkyRender( private val resource: Resources) : GLSurfaceView.Renderer {
    private var timer:Timer = Timer()
    private lateinit var smallSky: UniverseSky
    private lateinit var bigSky: UniverseSky
    private lateinit var earth: Earth
    private lateinit var moon:Moon
    private  val universeAngles: UniverseAngles = UniverseAngles(0f,0f)

     private lateinit var  task:TimerTask

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES30.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        GLES30.glEnable(GLES30.GL_DEPTH_TEST)
        GLES30.glEnable(GLES30.GL_CULL_FACE)
        smallSky = UniverseSky(1000, resource, 1f)
        bigSky = UniverseSky(500, resource, 2f)
        earth = Earth(2.0f, resource)
        moon = Moon(0.4f,resource)
        MatrixState.setInitModelMatrix()
    }

    @SuppressLint("RestrictedApi")
    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES30.glViewport(0, 0, width, height)
        val ratio = width.toFloat() / height.toFloat()
        MatrixState.setProjectfrustumM(-ratio, ratio, -1f, 1f, 4f, 100f)

        MatrixState.setCamera(0f, 0f, 7.2f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)
        MatrixState.setLightPosition(100f, 5f, -0f)
        earth.texDayId = initTexture(R.mipmap.earth,GLES30.GL_CLAMP_TO_EDGE)
        earth.texNightId = initTexture(R.mipmap.earthn,GLES30.GL_CLAMP_TO_EDGE)
        moon.texId = initTexture(R.mipmap.moon,GLES30.GL_REPEAT)
       resumeRotateTask()
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT or GLES30.GL_COLOR_BUFFER_BIT)
        smallSky.cAngle = universeAngles.starSkyRotateAngle
        bigSky.cAngle = universeAngles.starSkyRotateAngle
        earth.eAngle = universeAngles.earthRotateAngle
        moon.eAngle = universeAngles.earthRotateAngle

        MatrixState.pushMatrix()
        earth.draw()
        moon.draw()
        MatrixState.popMatrix()
        MatrixState.pushMatrix()
        smallSky.draw()
        bigSky.draw()
        MatrixState.popMatrix()
    }

    private fun initTexture(drawableId: Int, ss:Int): Int //textureId
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
            ss.toFloat()
        )
        GLES30.glTexParameterf(
            GLES30.GL_TEXTURE_2D,
            GLES30.GL_TEXTURE_WRAP_T,
            ss.toFloat()
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

    fun resumeRotateTask(){
        if(this::task.isInitialized){
            task.cancel()
        }
        task = object:TimerTask(){
            override fun run() {
                universeAngles.refreshAngles()
            }
        }
        timer.schedule(task,0L,100L)
    }
    fun cancelRotateTask(){
        if(this::task.isInitialized) task.cancel()
    }
    private fun UniverseAngles. refreshAngles() {
        this.earthRotateAngle = (this.earthRotateAngle + 2) % 360
        this.starSkyRotateAngle = (this.starSkyRotateAngle + 0.2f) % 360
    }
}