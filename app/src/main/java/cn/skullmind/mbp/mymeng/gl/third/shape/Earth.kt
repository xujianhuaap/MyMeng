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

class Earth(
    private val r: Float,
    resource: Resources
) : GLShape {
    private var pointSize = 0
    private var pointBuffer: FloatBuffer
    private var texCoordsBuffer: FloatBuffer

    private val program: Int

    private val uMVPMatrixHandle: Int
    private val uMMatrixHandle: Int
    private val uCameraHandle: Int
    private val uLightLocationHandle: Int
    private val aPositionHandle: Int
    private val aTexCoorHandle: Int
    private val aNormalHandle: Int
    private val sTexDayHandle: Int
    private val sTexNightHandle: Int

    var texDayId = 0
    var texNightId = 0
    var eAngle = 0f

    init {
        val pointCoords = initPoints()
        pointSize = pointCoords.size / COORS_SIZE_PER_VERTEX
        pointBuffer = ByteBuffer.allocateDirect(pointCoords.size * 4).run {
            order(ByteOrder.nativeOrder())
            asFloatBuffer().apply {
                put(pointCoords)
                position(0)
            }
        }

        val texCoors = generateTexCoor(360 / ANGLE_SPAN, 180 / ANGLE_SPAN)
        texCoordsBuffer = ByteBuffer.allocateDirect(texCoors.size * 4).run {
            order(ByteOrder.nativeOrder())
            asFloatBuffer().apply {
                put(texCoors)
                position(0)
            }
        }
        val vertexShader =
            ShaderUtil.initShader(GLES30.GL_VERTEX_SHADER, "vertex_universe_earth.sh", resource)
        val fragShader =
            ShaderUtil.initShader(GLES30.GL_FRAGMENT_SHADER, "frag_universe_earth.sh", resource)
        program = initProgram(vertexShader,fragShader)

        uMVPMatrixHandle = GLES30.glGetUniformLocation(program, "uMVPMatrix")
        uMMatrixHandle = GLES30.glGetUniformLocation(program, "uMMatrix")
        uCameraHandle = GLES30.glGetUniformLocation(program, "uCamera")
        uLightLocationHandle = GLES30.glGetUniformLocation(program, "uLightLocation")
        aPositionHandle = GLES30.glGetAttribLocation(program, "aPosition")
        aTexCoorHandle = GLES30.glGetAttribLocation(program, "aTexCoor")
        aNormalHandle = GLES30.glGetAttribLocation(program, "aNormal")
        sTexDayHandle = GLES30.glGetUniformLocation(program, "sTexDay")
        sTexNightHandle = GLES30.glGetUniformLocation(program, "sTexNight")
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

    private fun initPoints(): FloatArray {
        val pointCoors = ArrayList<Float>()
        var vAngle = 90f
        while (vAngle > -90) {
            var hAngle = 360f
            while (hAngle > 0) {
                var xozLength = r * UNIT_SIZE * Math.cos(Math.toRadians(vAngle.toDouble()))
                val x1 = (xozLength * Math.cos(Math.toRadians(hAngle.toDouble()))).toFloat()
                val z1 = (xozLength * Math.sin(Math.toRadians(hAngle.toDouble()))).toFloat()
                val y1 = (r * UNIT_SIZE * Math.sin(Math.toRadians(vAngle.toDouble()))).toFloat()
                xozLength =
                    r * UNIT_SIZE * Math.cos(Math.toRadians((vAngle - ANGLE_SPAN).toDouble()))
                val x2 = (xozLength * Math.cos(Math.toRadians(hAngle.toDouble()))).toFloat()
                val z2 = (xozLength * Math.sin(Math.toRadians(hAngle.toDouble()))).toFloat()
                val y2 =
                    (r * UNIT_SIZE * Math.sin(Math.toRadians((vAngle - ANGLE_SPAN).toDouble()))).toFloat()
                xozLength =
                    r * UNIT_SIZE * Math.cos(Math.toRadians((vAngle - ANGLE_SPAN).toDouble()))
                val x3 =
                    (xozLength * Math.cos(Math.toRadians((hAngle - ANGLE_SPAN).toDouble()))).toFloat()
                val z3 =
                    (xozLength * Math.sin(Math.toRadians((hAngle - ANGLE_SPAN).toDouble()))).toFloat()
                val y3 =
                    (r * UNIT_SIZE * Math.sin(Math.toRadians((vAngle - ANGLE_SPAN).toDouble()))).toFloat()
                xozLength = r * UNIT_SIZE * Math.cos(Math.toRadians(vAngle.toDouble()))
                val x4 =
                    (xozLength * Math.cos(Math.toRadians((hAngle - ANGLE_SPAN).toDouble()))).toFloat()
                val z4 =
                    (xozLength * Math.sin(Math.toRadians((hAngle - ANGLE_SPAN).toDouble()))).toFloat()
                val y4 = (r * UNIT_SIZE * Math.sin(Math.toRadians(vAngle.toDouble()))).toFloat()

                pointCoors.add(x1)
                pointCoors.add(y1)
                pointCoors.add(z1)
                pointCoors.add(x2)
                pointCoors.add(y2)
                pointCoors.add(z2)
                pointCoors.add(x4)
                pointCoors.add(y4)
                pointCoors.add(z4)

                pointCoors.add(x4)
                pointCoors.add(y4)
                pointCoors.add(z4)
                pointCoors.add(x2)
                pointCoors.add(y2)
                pointCoors.add(z2)
                pointCoors.add(x3)
                pointCoors.add(y3)
                pointCoors.add(z3)
                hAngle -= ANGLE_SPAN
            }
            vAngle -= ANGLE_SPAN
        }

        return pointCoors.toFloatArray()
    }

    override fun draw() {
        MatrixState.setInitModelMatrix()
        MatrixState.rotate(eAngle, 0f, 1f, 0f)
        GLES30.glUseProgram(program)
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0)
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D,texDayId)

        GLES30.glActiveTexture(GLES30.GL_TEXTURE1)
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D,texNightId)

        GLES30.glUniform1i(sTexDayHandle, 0)
        GLES30.glUniform1i(sTexNightHandle, 1)

        GLES30.glUniformMatrix4fv(uMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0)
        GLES30.glUniformMatrix4fv(uMMatrixHandle, 1, false, MatrixState.getCurrentModelMatrix(), 0)
        GLES30.glUniform3fv(uCameraHandle, 1, MatrixState.cameraBuffer)
        GLES30.glUniform3fv(uLightLocationHandle, 1, MatrixState.lightBuffer)
        GLES30.glVertexAttribPointer(
            aTexCoorHandle, TEX_COORS_SIZE_PER_VERTEX, GLES30.GL_FLOAT,
            false, TEX_COORS_SIZE_PER_VERTEX * 4, texCoordsBuffer
        )
        GLES30.glEnableVertexAttribArray(aTexCoorHandle)

        GLES30.glVertexAttribPointer(
            aPositionHandle, COORS_SIZE_PER_VERTEX, GLES30.GL_FLOAT,
            false, COORS_SIZE_PER_VERTEX * 4, pointBuffer
        )
        GLES30.glEnableVertexAttribArray(aPositionHandle)

        GLES30.glVertexAttribPointer(
            aNormalHandle, NORML_COORS_SIZE_PER_VERTEX, GLES30.GL_FLOAT,
            false, COORS_SIZE_PER_VERTEX * 4, pointBuffer
        )
        GLES30.glEnableVertexAttribArray(aNormalHandle)

        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, pointSize)
    }


    /***
     * @param bw 每行个数
     * @param bh 每列个数
     *
     */
    fun generateTexCoor(bw: Int, bh: Int): FloatArray {
        //每个矩形有 两个三角形,6个顶点,12个坐标组成
        val rectCount = bw * bh //矩形个数
        val result = FloatArray(rectCount * 6 * 2)
        val sizew = 1.0f / bw //单个 行方向上大小
        val sizeh = 1.0f / bh //单个 列方向上大小
        var c = 0
        for (i in 0 until bh) {
            for (j in 0 until bw) {
                val s = j * sizew
                val t = i * sizeh
                //第一个三角形
                result[c++] = s
                result[c++] = t

                result[c++] = s
                result[c++] = t + sizeh

                result[c++] = s + sizew
                result[c++] = t

                //第二个三角形
                result[c++] = s + sizew
                result[c++] = t

                result[c++] = s
                result[c++] = t + sizeh

                result[c++] = s + sizew
                result[c++] = t + sizeh
            }
        }
        return result
    }

    companion object {
        const val UNIT_SIZE = 0.3f
        const val COORS_SIZE_PER_VERTEX = 3
        const val TEX_COORS_SIZE_PER_VERTEX = 2
        const val NORML_COORS_SIZE_PER_VERTEX = 4 //法向量
        const val ANGLE_SPAN = 10
    }
}