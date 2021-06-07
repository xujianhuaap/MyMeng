package cn.skullmind.mbp.mymeng.gl.third

import android.annotation.SuppressLint
import android.content.Context
import android.opengl.GLSurfaceView
import android.view.MotionEvent
import cn.skullmind.mbp.mymeng.gl.third.renders.GL30BallRenderer
import cn.skullmind.mbp.mymeng.gl.third.renders.GL30UniverseSkyRender
import cn.skullmind.mbp.mymeng.gl.third.renders.GL30WallRender

class Gl30SurfaceView(context: Context?) : GLSurfaceView(context) {
    private val render: GL30UniverseSkyRender
    private var touchX = 0f
    private var touchY = 0f

    init {
        setEGLContextClientVersion(3)
        render = GL30UniverseSkyRender(resources)
        setRenderer(render)
        renderMode = RENDERMODE_CONTINUOUSLY
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val handled = super.onTouchEvent(event)

        event?.also {
            if (MotionEvent.ACTION_MOVE.equals(it.action)) {
                val xRangle = (it.x - touchX) * SCALE_FACTOR
                val yRangle = (it.y - touchY) * SCALE_FACTOR
//                render.refreshBallAngles(xRangle, yRangle, 0f)
            }

            touchX = it.x
            touchY = it.y
        }
        return true

    }


    companion object {
        const val SCALE_FACTOR = 0.8f
    }
}


