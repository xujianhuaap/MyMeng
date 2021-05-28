package cn.skullmind.mbp.mymeng.gl.third

import android.content.Context
import android.opengl.GLSurfaceView
import cn.skullmind.mbp.mymeng.gl.third.renders.GL30BallRenderer
import cn.skullmind.mbp.mymeng.gl.third.renders.GL30SixPointStarRenderer

class Gl30SurfaceView(context: Context?) : GLSurfaceView(context) {
    private val render: GL30SixPointStarRenderer

    init {
        setEGLContextClientVersion(3)
        render = GL30SixPointStarRenderer()
        render.resources = resources
        setRenderer(render)
        renderMode = RENDERMODE_CONTINUOUSLY
    }

}


