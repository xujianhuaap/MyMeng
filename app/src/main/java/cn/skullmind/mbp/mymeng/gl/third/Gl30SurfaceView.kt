package cn.skullmind.mbp.mymeng.gl.third

import android.content.Context
import android.opengl.GLSurfaceView
import cn.skullmind.mbp.mymeng.gl.third.shape.SixPointStar

class Gl30SurfaceView(context: Context?) : GLSurfaceView(context) {
    private val render: GL30Renderer

    init {
        setEGLContextClientVersion(3)
        render = GL30Renderer(this.context)
        setRenderer(render)
        renderMode = RENDERMODE_CONTINUOUSLY
    }

}


