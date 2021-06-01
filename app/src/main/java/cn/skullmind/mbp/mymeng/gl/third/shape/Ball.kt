package cn.skullmind.mbp.mymeng.gl.third.shape

import android.content.res.Resources
import android.opengl.GLES30
import cn.skullmind.mbp.mymeng.gl.GLShape
import cn.skullmind.mbp.tools.MatrixState
import cn.skullmind.mbp.tools.ShaderUtil
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import kotlin.math.cos
import kotlin.math.sin

class Ball(resource: Resources) : GLShape {
    private val pointCoords: FloatArray
    private val pointSize: Int

    private val vertexShader: Int
    private val fragShader: Int
    private val program: Int

    private val positionHandle: Int
    private val uLightPositionHandle: Int
    private val uMVPMatrixHandle: Int
    private val uMMatrixHandle: Int
    private val uRHandle: Int
    private val aNormalHandle:Int

    private val vertexBuffer: FloatBuffer
    private val normalBuffer: FloatBuffer

    private var angleX = 0f
    private var angleY = 0f
    private var angleZ = 0f

    val r = 0.8f

    init {
        pointCoords = initPoints()
        pointSize = pointCoords.size / COORD_SIZE_PER_VERTEX

        vertexBuffer = ByteBuffer.allocateDirect(pointCoords.size * 4).run {
            order(ByteOrder.nativeOrder())
            asFloatBuffer().apply {
                put(pointCoords)
                position(0)
            }
        }

        normalBuffer = ByteBuffer.allocateDirect(pointCoords.size * 4).run {
            order(ByteOrder.nativeOrder())
            asFloatBuffer().apply {
                put(pointCoords)
                position(0)
            }
        }


        vertexShader = ShaderUtil.initShader(GLES30.GL_VERTEX_SHADER, "vertex_ball.sh", resource)
        fragShader = ShaderUtil.initShader(GLES30.GL_FRAGMENT_SHADER, "frag_ball.sh", resource)
        program = initProgram()
        positionHandle = GLES30.glGetAttribLocation(program, "vPosition")
        aNormalHandle = GLES30.glGetAttribLocation(program, "aNormal")
        uLightPositionHandle = GLES30.glGetUniformLocation(program, "uLightPosition")
        uMVPMatrixHandle = GLES30.glGetUniformLocation(program, "uMVPMatrix")
        uMMatrixHandle = GLES30.glGetUniformLocation(program, "uMMatrix")
        uRHandle = GLES30.glGetUniformLocation(program, "uR")
    }

    private fun initProgram(): Int = GLES30.glCreateProgram().also {
        GLES30.glAttachShader(it, vertexShader)
        GLES30.glAttachShader(it, fragShader)
        GLES30.glLinkProgram(it)
    }


    private fun initPoints(): FloatArray {
        val pointCoords = ArrayList<Float>()
        val angleSpan = 10 // ������е�λ�зֵĽǶ�
        var vAngle = -90
        while (vAngle < 90) {
            var hAngle = 0
            while (hAngle <= 360) {
                val x0 = (r * UNIT_SIZE
                        * cos(Math.toRadians(vAngle.toDouble())) * cos(
                    Math
                        .toRadians(hAngle.toDouble())
                )).toFloat()
                val y0 = (r * UNIT_SIZE
                        * cos(Math.toRadians(vAngle.toDouble())) * sin(
                    Math
                        .toRadians(hAngle.toDouble())
                )).toFloat()
                val z0 = (r * UNIT_SIZE * sin(
                    Math
                        .toRadians(vAngle.toDouble())
                )).toFloat()
                val x1 = (r * UNIT_SIZE
                        * cos(Math.toRadians(vAngle.toDouble())) * cos(
                    Math
                        .toRadians((hAngle + angleSpan).toDouble())
                )).toFloat()
                val y1 = (r * UNIT_SIZE
                        * cos(Math.toRadians(vAngle.toDouble())) * sin(
                    Math
                        .toRadians((hAngle + angleSpan).toDouble())
                )).toFloat()
                val z1 = (r * UNIT_SIZE * sin(
                    Math
                        .toRadians(vAngle.toDouble())
                )).toFloat()
                val x2 = (r * UNIT_SIZE
                        * cos(Math.toRadians((vAngle + angleSpan).toDouble())) * cos(
                    Math.toRadians(
                        (hAngle + angleSpan).toDouble()
                    )
                )).toFloat()
                val y2 = (r * UNIT_SIZE
                        * cos(Math.toRadians((vAngle + angleSpan).toDouble())) * sin(
                    Math.toRadians(
                        (hAngle + angleSpan).toDouble()
                    )
                )).toFloat()
                val z2 = (r * UNIT_SIZE * sin(
                    Math
                        .toRadians((vAngle + angleSpan).toDouble())
                )).toFloat()
                val x3 = (r * UNIT_SIZE
                        * cos(Math.toRadians((vAngle + angleSpan).toDouble())) * cos(
                    Math.toRadians(
                        hAngle.toDouble()
                    )
                )).toFloat()
                val y3 = (r * UNIT_SIZE
                        * cos(Math.toRadians((vAngle + angleSpan).toDouble())) * sin(
                    Math.toRadians(
                        hAngle.toDouble()
                    )
                )).toFloat()
                val z3 = (r * UNIT_SIZE * sin(
                    Math
                        .toRadians((vAngle + angleSpan).toDouble())
                )).toFloat()

                pointCoords.add(x1)
                pointCoords.add(y1)
                pointCoords.add(z1)
                pointCoords.add(x3)
                pointCoords.add(y3)
                pointCoords.add(z3)
                pointCoords.add(x0)
                pointCoords.add(y0)
                pointCoords.add(z0)
                pointCoords.add(x1)
                pointCoords.add(y1)
                pointCoords.add(z1)
                pointCoords.add(x2)
                pointCoords.add(y2)
                pointCoords.add(z2)
                pointCoords.add(x3)
                pointCoords.add(y3)
                pointCoords.add(z3)
                hAngle += angleSpan
            }
            vAngle += angleSpan
        }

        return pointCoords.toFloatArray()
    }

    override fun draw() {
        GLES30.glUseProgram(program)
        MatrixState.rotate(angleX, 1f, 0f, 0f)
        MatrixState.rotate(0f, 0f, 1f, 0f)
        MatrixState.rotate(angleZ, 0f, 0f, 1f)
        GLES30.glUniformMatrix4fv(
            uMVPMatrixHandle, 1, false,
            MatrixState.getFinalMatrix(), 0
        )

        GLES30.glUniformMatrix4fv(
            uMMatrixHandle, 1, false,
            MatrixState.getCurrentModelMatrix(), 0
        )

        GLES30.glUniform3fv(
            uLightPositionHandle,
            1, MatrixState.lightBuffer
        )

        GLES30.glUniform1f(uRHandle, r * UNIT_SIZE)

        GLES30.glVertexAttribPointer(
            aNormalHandle, COORD_SIZE_PER_VERTEX,
            GLES30.GL_FLOAT, false, COORD_SIZE_PER_VERTEX * 4,
            normalBuffer)
        GLES30.glVertexAttribPointer(
            positionHandle, COORD_SIZE_PER_VERTEX, GLES30.GL_FLOAT,
            false, COORD_SIZE_PER_VERTEX * 4, vertexBuffer
        )
        GLES30.glEnableVertexAttribArray(positionHandle)
        GLES30.glEnableVertexAttribArray(aNormalHandle)
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, pointSize)
    }

    fun refreshAngles(angleX: Float, angleY: Float, angleZ: Float){
        this.angleX += angleX
        this.angleX = this.angleX.coerceAtMost(MAX_ANGLE)
        this.angleX = this.angleX.coerceAtLeast(-MAX_ANGLE)
        this.angleY += angleY
        this.angleY = this.angleY.coerceAtMost(MAX_ANGLE)
        this.angleY = this.angleY.coerceAtLeast(-MAX_ANGLE)
        this.angleZ += angleZ
    }

    companion object {
        const val UNIT_SIZE = 1
        const val COORD_SIZE_PER_VERTEX = 3
        const val MAX_ANGLE = 3.5f

    }
}