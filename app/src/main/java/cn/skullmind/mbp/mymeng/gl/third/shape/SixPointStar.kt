package cn.skullmind.mbp.mymeng.gl.third.shape

import android.content.res.Resources
import android.opengl.GLES30
import android.opengl.Matrix
import android.util.Log
import cn.skullmind.mbp.tools.MatrixState
import cn.skullmind.mbp.tools.ShaderUtil
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

class SixPointStar(outRadius: Float, innerRadius: Float, zAxisValue: Float, resource: Resources) {

    private var pointCount = 0

    private var vertexShader: Int
    private var fragShader: Int
    private var program: Int
    private var vertexBuffer: FloatBuffer
    private var colorBuffer: FloatBuffer

    private var aPositionHandle = -1
    private var aColorHandle = -1
    private var muMVPMatrixHandle = -1

    private val modelMatrix = FloatArray(16)

    init {
        val coords = initPoints(outRadius, innerRadius, zAxisValue)
        pointCount = coords.size / COORDINATE_SIZE_PER_VERTEX

        vertexBuffer = initVertexBuffer(coords)
        colorBuffer = initColorBuffer(pointCount)

        vertexShader = initShader(GLES30.GL_VERTEX_SHADER, "vertex.sh", resource)
        fragShader = initShader(GLES30.GL_FRAGMENT_SHADER, "frag.sh", resource)
        program = initProgram()

        aPositionHandle = GLES30.glGetAttribLocation(program, "aPosition")
        aColorHandle = GLES30.glGetAttribLocation(program, "aColor")
        muMVPMatrixHandle = GLES30.glGetUniformLocation(program, "uMVPMatrix")
    }

    private fun initColorBuffer(pointCount:Int):FloatBuffer {
        val colorArray = FloatArray(pointCount * 4) //¶¥µã×ÅÉ«Êý¾ÝµÄ³õÊ¼»¯
        for (i in 0 until pointCount) {
            if (i % 3 == 0) { //ÖÐÐÄµãÎª°×É«£¬RGBA 4¸öÍ¨µÀ[1,1,1,0]
                colorArray[i * 4] = 1F
                colorArray[i * 4 + 1] = 1F
                colorArray[i * 4 + 2] = 1F
                colorArray[i * 4 + 3] = 0F
            } else { //±ßÉÏµÄµãÎªµ­À¶É«£¬RGBA 4¸öÍ¨µÀ[0.45,0.75,0.75,0]
                colorArray[i * 4] = 0.45f
                colorArray[i * 4 + 1] = 0.75f
                colorArray[i * 4 + 2] = 0.75f
                colorArray[i * 4 + 3] = 0F
            }
        }
        return ByteBuffer.allocateDirect(colorArray.size * 4).run {
            order(ByteOrder.nativeOrder())
            asFloatBuffer().apply {
                put(colorArray)
                position(0)
            }
        }
    }

    private fun initVertexBuffer(coords: FloatArray):FloatBuffer = ByteBuffer.allocateDirect(coords.size * 4).run {
        order(ByteOrder.nativeOrder())
        asFloatBuffer().apply {
            put(coords)
            position(0)
        }
    }

    private fun initProgram(): Int = GLES30.glCreateProgram().let {
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

    fun draw() {
        GLES30.glUseProgram(program)
        Matrix.setRotateM(modelMatrix, 0, 0f, 0f, 1f, 0f)
        //ÉèÖÃÑØZÖáÕýÏòÎ»ÒÆ1
        Matrix.translateM(modelMatrix, 0, 0f, 0f, 1f)
        //ÉèÖÃÈÆyÖáÐý×ªyAngle¶È
        Matrix.rotateM(modelMatrix, 0, 0f, 0f, 1f, 0f)
        //ÉèÖÃÈÆxÖáÐý×ªxAngle¶È
        Matrix.rotateM(modelMatrix, 0, 0f, 1f, 0f, 0f)
        GLES30.glUniformMatrix4fv(
            muMVPMatrixHandle, 1, false,
            MatrixState.getFinalMatrix(modelMatrix), 0
        )
        //½«¶¥µãÎ»ÖÃÊý¾ÝËÍÈëäÖÈ¾¹ÜÏß
        GLES30.glVertexAttribPointer(
            aPositionHandle,
            COORDINATE_SIZE_PER_VERTEX,
            GLES30.GL_FLOAT,
            false,
            COORDINATE_SIZE_PER_VERTEX * 4,
            vertexBuffer
        )
        //½«¶¥µãÑÕÉ«Êý¾ÝËÍÈëäÖÈ¾¹ÜÏß
        GLES30.glVertexAttribPointer(
            aColorHandle,
            COLOR_SIZE_PER_VERTEX,
            GLES30.GL_FLOAT,
            false,
            COLOR_SIZE_PER_VERTEX * 4,
            colorBuffer
        )
        //ÆôÓÃ¶¥µãÎ»ÖÃÊý¾ÝÊý×é
        GLES30.glEnableVertexAttribArray(aPositionHandle)
        //ÆôÓÃ¶¥µãÑÕÉ«Êý¾ÝÊý×é
        GLES30.glEnableVertexAttribArray(aColorHandle)
        //»æÖÆÁù½ÇÐÇ
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, pointCount)

    }

    private fun initShader(type: Int, fileName: String, resource: Resources): Int {
        val loadShaderFromAsset = ShaderUtil.loadShaderFromAsset(resource, fileName)
        return GLES30.glCreateShader(type).let {
            GLES30.glShaderSource(it, loadShaderFromAsset)
            GLES30.glCompileShader(it)
            val compiled = IntArray(1)
            GLES30.glGetShaderiv(it, GLES30.GL_COMPILE_STATUS, compiled, 0)
            var result = it
            if (compiled[0] == 0) {
                Log.e("ES30_ERROR", "Could not compile shader $type:")
                Log.e("ES30_ERROR", GLES30.glGetShaderInfoLog(it))
                GLES30.glDeleteShader(it)
                result = 0
            }
            result
        }
    }


    private fun initPoints(R: Float, r: Float, z: Float): FloatArray {
        val coords: MutableList<Float> = ArrayList()
        val tempAngle = 60F
        var angle = 0F
        while (angle < 360) {

            coords.add(0f)
            coords.add(0f)
            coords.add(z)

            coords.add((R * UNIT_SIZE * cos(Math.toRadians(angle.toDouble()))).toFloat())
            coords.add((R * UNIT_SIZE * sin(Math.toRadians(angle.toDouble()))).toFloat())
            coords.add(z)

            coords.add((r * UNIT_SIZE * cos(Math.toRadians((angle + tempAngle / 2).toDouble()))).toFloat())
            coords.add((r * UNIT_SIZE * sin(Math.toRadians((angle + tempAngle / 2).toDouble()))).toFloat())
            coords.add(z)


            coords.add(0f)
            coords.add(0f)
            coords.add(z)

            coords.add((r * UNIT_SIZE * cos(Math.toRadians((angle + tempAngle / 2).toDouble()))).toFloat())
            coords.add((r * UNIT_SIZE * sin(Math.toRadians((angle + tempAngle / 2).toDouble()))).toFloat())
            coords.add(z)

            coords.add((R * UNIT_SIZE * cos(Math.toRadians((angle + tempAngle).toDouble()))).toFloat())
            coords.add((R * UNIT_SIZE * sin(Math.toRadians((angle + tempAngle).toDouble()))).toFloat())
            coords.add(z)

            angle += tempAngle
        }


        return coords.toFloatArray()
    }

    companion object {
        const val UNIT_SIZE = 1F
        const val COORDINATE_SIZE_PER_VERTEX = 3
        const val COLOR_SIZE_PER_VERTEX = 4
    }

}