package cn.skullmind.mbp.mymeng.gl.third

import android.content.Context
import android.opengl.GLSurfaceView

class Gl30SurfaceView(context: Context?) : GLSurfaceView(context) {
    private val render: GL30Renderer

    init {
        setEGLContextClientVersion(3)
        render = GL30Renderer()
        setRenderer(render)
    }

}


