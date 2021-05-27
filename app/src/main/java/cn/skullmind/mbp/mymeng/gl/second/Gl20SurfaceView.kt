package cn.skullmind.mbp.mymeng.gl.second

import android.content.Context
import android.opengl.GLSurfaceView

class Gl20SurfaceView(context: Context?) : GLSurfaceView(context) {
    private val render: GL20Renderer

    init {
        setEGLContextClientVersion(2)
        render = GL20Renderer()
        setRenderer(render)
    }

}


