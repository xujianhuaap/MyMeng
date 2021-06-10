package cn.skullmind.mbp.mymeng.gl.third.shape

import android.content.res.Resources
import android.opengl.GLES30
import cn.skullmind.mbp.mymeng.gl.GLShape
import cn.skullmind.mbp.tools.MatrixState
import cn.skullmind.mbp.tools.ShaderUtil
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class Moon(private val radius: Float, private val resource: Resources) :GLShape {
    private val pointCount:Int
    private val pointBuffer:FloatBuffer
    private val normalBuffer:FloatBuffer
    private val texBuffer:FloatBuffer


    private val program:Int
    private val uMVPMatrixHandle:Int
    private val uMMatrixHandle:Int
    private val uCameraHandle:Int
    private val uLightLocationHandle:Int
    private val aPositionHandle:Int
    private val aTexCoorHandle:Int
    private val aNormalHandle:Int
    private val sTextureHandle:Int

    var texId:Int = 0
    var  eAngle = 0f
    init {
        val pointCoors = initPoints()
        pointCount = pointCoors.size / COOR_SIZE_PER_VERTEX
        pointBuffer = ByteBuffer.allocateDirect(pointCoors.size * 4).run {
            order(ByteOrder.nativeOrder())
            asFloatBuffer().apply {
                put(pointCoors)
                position(0)
            }
        }

        normalBuffer = ByteBuffer.allocateDirect(pointCoors.size * 4).run {
            order(ByteOrder.nativeOrder())
            asFloatBuffer().apply {
                put(pointCoors)
                position(0)
            }
        }

        val texCoors = initTexCoor()
        texBuffer = ByteBuffer.allocateDirect(texCoors.size * 4).run {
            order(ByteOrder.nativeOrder())
            asFloatBuffer().apply {
                put(texCoors)
                position(0)

            }
        }

        val vertexShader = ShaderUtil.initShader(
            GLES30.GL_VERTEX_SHADER,
            "vertex_universe_moon.sh", resource
        )
        val fragShader = ShaderUtil.initShader(
            GLES30.GL_FRAGMENT_SHADER,
            "frag_universe_moon.sh", resource
        )
        program = GLES30.glCreateProgram().also {
            GLES30.glAttachShader(it, vertexShader)
            GLES30.glAttachShader(it, fragShader)
            GLES30.glLinkProgram(it)
        }

        uMVPMatrixHandle = GLES30.glGetUniformLocation(program, "uMVPMatrix")
        uMMatrixHandle = GLES30.glGetUniformLocation(program, "uMMatrix")
        uCameraHandle = GLES30.glGetUniformLocation(program, "uCamera")
        uLightLocationHandle = GLES30.glGetUniformLocation(program, "uLightLocation")
        sTextureHandle = GLES30.glGetUniformLocation(program, "sTexture")

        aPositionHandle = GLES30.glGetAttribLocation(program, "aPosition")
        aTexCoorHandle = GLES30.glGetAttribLocation(program, "aTexCoor")
        aNormalHandle = GLES30.glGetAttribLocation(program, "aNormal")
    }

    private fun initTexCoor():FloatArray{
        val wCount = 360/ ANGLE_SPAN
        val hCount = 180/ ANGLE_SPAN

        val texCoors = FloatArray(wCount * hCount * 6 * 2)
        val wSize = 1f / wCount
        val hSize = 1f /hCount
        var c = 0

        for(i in 0 until wCount){
            for (j in 0 until  hCount){
                val s = j * wSize
                val t = i * hSize
                //第一个三角形
                texCoors[c++] = s
                texCoors[c++] = t

                texCoors[c++] = s
                texCoors[c++] = t + hSize

                texCoors[c++] = s + wSize
                texCoors[c++] = t

                //第二个三角形
                texCoors[c++] = s + wSize
                texCoors[c++] = t

                texCoors[c++] = s
                texCoors[c++] = t + hSize

                texCoors[c++] = s + wSize
                texCoors[c++] = t + hSize
            }
        }


        return texCoors
    }
    private fun initPoints(): FloatArray {
        val pointCoors = ArrayList<Float>()
        var vAngle = 90f
        while (vAngle > -90) {
            var hAngle = 360f
            while (hAngle > 0) {
                var xozLength = radius * UNIT_SIZE * Math.cos(Math.toRadians(vAngle.toDouble()))
                val x1 = (xozLength * Math.cos(Math.toRadians(hAngle.toDouble()))).toFloat()
                val z1 = (xozLength * Math.sin(Math.toRadians(hAngle.toDouble()))).toFloat()
                val y1 = (radius * UNIT_SIZE * Math.sin(Math.toRadians(vAngle.toDouble()))).toFloat()
                xozLength =
                    radius * UNIT_SIZE * Math.cos(Math.toRadians((vAngle - ANGLE_SPAN).toDouble()))
                val x2 = (xozLength * Math.cos(Math.toRadians(hAngle.toDouble()))).toFloat()
                val z2 = (xozLength * Math.sin(Math.toRadians(hAngle.toDouble()))).toFloat()
                val y2 =
                    (radius * UNIT_SIZE * Math.sin(Math.toRadians((vAngle - ANGLE_SPAN).toDouble()))).toFloat()
                xozLength =
                    radius * UNIT_SIZE * Math.cos(Math.toRadians((vAngle - ANGLE_SPAN).toDouble()))
                val x3 =
                    (xozLength * Math.cos(Math.toRadians((hAngle - ANGLE_SPAN).toDouble()))).toFloat()
                val z3 =
                    (xozLength * Math.sin(Math.toRadians((hAngle - ANGLE_SPAN).toDouble()))).toFloat()
                val y3 =
                    (radius * UNIT_SIZE * Math.sin(Math.toRadians((vAngle - ANGLE_SPAN).toDouble()))).toFloat()
                xozLength = radius * UNIT_SIZE * Math.cos(Math.toRadians(vAngle.toDouble()))
                val x4 =
                    (xozLength * Math.cos(Math.toRadians((hAngle - ANGLE_SPAN).toDouble()))).toFloat()
                val z4 =
                    (xozLength * Math.sin(Math.toRadians((hAngle - ANGLE_SPAN).toDouble()))).toFloat()
                val y4 = (radius * UNIT_SIZE * Math.sin(Math.toRadians(vAngle.toDouble()))).toFloat()

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
        MatrixState.translate(2f, 0f, 0f)
        MatrixState.rotate(eAngle/10, 0f, 1f, 0f)
        GLES30.glUseProgram(program)
        GLES30.glUniform1i(sTextureHandle, 0)
        GLES30.glUniformMatrix4fv(
            uMVPMatrixHandle, 1,
            false, MatrixState.getFinalMatrix(), 0
        )
        GLES30.glUniformMatrix4fv(
            uMMatrixHandle, 1,
            false, MatrixState.getCurrentModelMatrix(), 0
        )
        GLES30.glUniform3fv(uCameraHandle, 1, MatrixState.cameraBuffer)
        GLES30.glUniform3fv(uLightLocationHandle, 1, MatrixState.lightBuffer)


        GLES30.glVertexAttribPointer(
            aTexCoorHandle, TEX_COOR_SIZE_PER_VERTEX, GLES30.GL_FLOAT, false,
            TEX_COOR_SIZE_PER_VERTEX * 4, texBuffer
        )
        GLES30.glEnableVertexAttribArray(aTexCoorHandle)

        GLES30.glVertexAttribPointer(
            aNormalHandle, 4, GLES30.GL_FLOAT, false,
            COOR_SIZE_PER_VERTEX * 4, pointBuffer
        )
        GLES30.glEnableVertexAttribArray(aNormalHandle)

        GLES30.glVertexAttribPointer(
            aPositionHandle, COOR_SIZE_PER_VERTEX,
            GLES30.GL_FLOAT, false, COOR_SIZE_PER_VERTEX * 4, pointBuffer
        )
        GLES30.glEnableVertexAttribArray(aPositionHandle)


        GLES30.glActiveTexture(GLES30.GL_TEXTURE0)
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texId)
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, pointCount)

    }

    companion object {
        const val ANGLE_SPAN = 10
        const val UNIT_SIZE = 0.5f
        const val COOR_SIZE_PER_VERTEX = 3
        const val TEX_COOR_SIZE_PER_VERTEX = 2
    }
}