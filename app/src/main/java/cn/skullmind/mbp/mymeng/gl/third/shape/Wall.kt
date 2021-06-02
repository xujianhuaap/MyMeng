package cn.skullmind.mbp.mymeng.gl.third.shape

import android.content.res.Resources
import android.opengl.GLES30
import android.util.Log
import cn.skullmind.mbp.mymeng.gl.GLShape
import cn.skullmind.mbp.tools.MatrixState
import cn.skullmind.mbp.tools.ShaderUtil
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class Wall (val resources: Resources, val textureId: Int) : GLShape {
    private val pointCoords:FloatArray
    private var pointCount:Int = 0

    private var pointBuffer:FloatBuffer
    private var textureBuffer:FloatBuffer

    private var vertexShader = 0
    private var fragShader = 0
    private var program: Int = 0
    private var uMVPMatrixHandle:Int = 0
    private var aPositionHandle: Int = 0
    private var aTextureCoorHandle:Int = 0

    init {
        pointCoords = initPoints()
        pointCount = pointCoords.size / COORD_SIZE_PER_VERTEX

        pointBuffer = ByteBuffer.allocateDirect(pointCoords.size * 4).run {
            order(ByteOrder.nativeOrder())
            asFloatBuffer().apply {
                put(pointCoords)
                position(0)
            }
        }

        val textureCoor = floatArrayOf(
            0.5f, 0f,
            0f, 1f,
            1f, 0f
        )
        textureBuffer = ByteBuffer.allocateDirect(textureCoor.size * 4).run {
            order(ByteOrder.nativeOrder())
            asFloatBuffer().apply {
                put(textureCoor)
                position(0)
            }
        }

        vertexShader = ShaderUtil.initShader(GLES30.GL_VERTEX_SHADER, "vertex_wall.sh", resources)
        fragShader = ShaderUtil.initShader(GLES30.GL_FRAGMENT_SHADER, "frag_wall.sh", resources)
        program = initProgram()
        uMVPMatrixHandle = GLES30.glGetUniformLocation(program, "uMVPMatrix")
        aPositionHandle = GLES30.glGetAttribLocation(program, "aPosition")
        aTextureCoorHandle = GLES30.glGetAttribLocation(program, "aTexture")
    }

    private fun initPoints():FloatArray{
        val s = floatArrayOf(
            0 * UNIT_SIZE, 11 * UNIT_SIZE, 0f,
            -11 * UNIT_SIZE, -11 * UNIT_SIZE, 0f,
            11 * UNIT_SIZE, -11 * UNIT_SIZE, 0f
        )
        return s
    }

    private fun initProgram():Int = GLES30.glCreateProgram().let {
        GLES30.glAttachShader(it, vertexShader)
        GLES30.glAttachShader(it, fragShader)
        GLES30.glLinkProgram(it)
        val linkStatus = IntArray(1)
        GLES30.glGetProgramiv(it, GLES30.GL_LINK_STATUS, linkStatus, 0)
        var result = it
        if (linkStatus[0] != GLES30.GL_TRUE) {
            Log.e("ES30_ERROR", "Could not link program: ")
            Log.e("ES30_ERROR", GLES30.glGetProgramInfoLog(it))
            GLES30.glDeleteProgram(it)
            result = 0
        }
        result
    }
    override fun draw() {
        GLES30.glUseProgram(program)

        MatrixState.setInitModelMatrix()
        MatrixState.translate(0f, 0f, 1f)
        MatrixState.rotate(0f, 0f, 1f, 0f)
        MatrixState.rotate(0f, 0f, 0f, 1f)

        GLES30.glEnableVertexAttribArray(aPositionHandle)
        GLES30.glEnableVertexAttribArray(aTextureCoorHandle)

        GLES30.glUniformMatrix4fv(uMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0)
        GLES30.glVertexAttribPointer(
            aPositionHandle, COORD_SIZE_PER_VERTEX, GLES30.GL_FLOAT,
            false, COORD_SIZE_PER_VERTEX * 4, pointBuffer
        )
        GLES30.glVertexAttribPointer(
            aTextureCoorHandle, TEXTURE_SIZE_PER_VERTEX, GLES30.GL_FLOAT,
            false, TEXTURE_SIZE_PER_VERTEX * 4, textureBuffer
        )


        GLES30.glActiveTexture(GLES30.GL_TEXTURE0)
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureId)
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, pointCount)
    }

    companion object{
        const val COORD_SIZE_PER_VERTEX = 3
        const val TEXTURE_SIZE_PER_VERTEX = 2
        const val UNIT_SIZE = 0.15f
    }

}