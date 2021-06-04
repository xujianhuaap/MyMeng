package cn.skullmind.mbp.mymeng.gl.third.renders

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.GLES30
import android.opengl.GLSurfaceView
import android.opengl.GLUtils
import cn.skullmind.mbp.mymeng.R
import cn.skullmind.mbp.mymeng.gl.GLShape
import cn.skullmind.mbp.mymeng.gl.third.shape.Wall
import cn.skullmind.mbp.tools.MatrixState
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class GL30WallRender(val resource: Resources) : GLSurfaceView.Renderer {
    private lateinit var shape: GLShape
    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {


        GLES30.glClearColor(0.5f, 0.5f, 0.5f, 1.0f)
        GLES30.glEnable(GLES30.GL_DEPTH_TEST)
        shape = Wall(resource, initTexture())
        GLES30.glDisable(GLES30.GL_CULL_FACE)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        val viewWidth = width*0.8
        val viewHeight = height*0.6
        val xStart = (width - viewWidth) /2
        val yStart = (height - viewHeight)/2
        GLES30.glViewport(xStart.toInt(), yStart.toInt(), viewWidth.toInt(), viewHeight.toInt())
        val ratio = (viewWidth.toFloat() / viewHeight.toFloat())
        MatrixState.setProjectfrustumM(-ratio, ratio, -1f, 1f, 1f, 10f)
        MatrixState.setCamera(0f, 0f, 3f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT or GLES30.GL_COLOR_BUFFER_BIT)
        shape.draw()
    }


    @SuppressLint("ResourceType")
    private fun initTexture(): Int {
        val textures = IntArray(1)
        GLES30.glGenTextures(textures.size, textures, 0)
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
            GLES30.GL_REPEAT.toFloat()
        )
        GLES30.glTexParameterf(
            GLES30.GL_TEXTURE_2D,
            GLES30.GL_TEXTURE_WRAP_T,
            GLES30.GL_REPEAT.toFloat()
        )

        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D,
            GLES30.GL_TEXTURE_SWIZZLE_R,//片元着色器的颜色红色通道
            GLES30.GL_GREEN.toFloat())//映射纹理的绿颜色通道
        val bitmap = resource.openRawResource(R.drawable.wall).let {
            var bmp: Bitmap? = null
            try {
                bmp = BitmapFactory.decodeStream(it)

            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                it.close()
            }
            bmp
        }
        GLUtils.texImage2D(GLES30.GL_TEXTURE_2D, 0, bitmap, 0)
        bitmap?.recycle()
        return textureId
    }
}