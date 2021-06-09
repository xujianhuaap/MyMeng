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
import kotlin.math.cos
import kotlin.math.sin

class UniverseSky(private var pointCount: Int,
                  resource:Resources, private val pointSize:Float):GLShape {
    private  var pointBuffer: FloatBuffer

    private var program:Int = 0
    private var aPositionHandle  = 0
    private var uPointSizeHandle = 0
    private var uMVPMatrixHandle = 0

    var cAngle = 0f
    init {
        if(pointCount < 100){
            pointCount = 100
        }

        val points = FloatArray(pointCount * COORD_COUNT_PER_VERTEX)
        for(i in 0 until pointCount){
            val angleTempJD = Math.PI * 2 * Math.random()
            val angleTempWD = Math.PI * (Math.random() - 0.5f)
            points[i * 3] = (UNIT_SIZE * cos(angleTempWD) * sin(angleTempJD)).toFloat()
            points[i * 3 + 1] = (UNIT_SIZE * sin(angleTempWD)).toFloat()
            points[i * 3 + 2] = (UNIT_SIZE * cos(angleTempWD) * cos(angleTempJD)).toFloat()
        }

        pointBuffer = ByteBuffer.allocateDirect(points.size*4).run {

            order(ByteOrder.nativeOrder())
            asFloatBuffer().apply {
                put(points)
                position(0)
            }
        }

        val vertexShader = ShaderUtil.initShader(GLES30.GL_VERTEX_SHADER,"vertex_universe_sky.sh",resource)
        val fragShader = ShaderUtil.initShader(GLES30.GL_FRAGMENT_SHADER,"frag_universe_sky.sh",resource)
        program = initProgram(vertexShader, fragShader)

        uPointSizeHandle = GLES30.glGetUniformLocation(program,"uPointSize")
        uMVPMatrixHandle = GLES30.glGetUniformLocation(program,"uMVPMatrix")
        aPositionHandle = GLES30.glGetAttribLocation(program,"aPosition")

    }

    private fun initProgram(vertexShader:Int, fragShader:Int): Int = GLES30.glCreateProgram().let {
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
        MatrixState.rotate(cAngle, 0f, 1f, 0f)
        GLES30.glUseProgram(program)
        GLES30.glEnableVertexAttribArray(aPositionHandle)
        GLES30.glUniformMatrix4fv(uMVPMatrixHandle,1,
            false,MatrixState.getFinalMatrix(),0)
        GLES30.glUniform1f(uPointSizeHandle,pointSize)
        GLES30.glVertexAttribPointer(aPositionHandle, COORD_COUNT_PER_VERTEX,GLES30.GL_FLOAT,
        false, COORD_COUNT_PER_VERTEX*4,pointBuffer)
        GLES30.glDrawArrays(GLES30.GL_POINTS,0,pointCount)

    }

    companion object {
        const val UNIT_SIZE = 8f
        const val COORD_COUNT_PER_VERTEX = 3
    }
}