package cn.skullmind.mbp.mymeng.gl.second.shape

import android.opengl.GLES20
import android.opengl.Matrix
import cn.skullmind.mbp.tools.MatrixState
import java.nio.ByteBuffer
import java.nio.ByteOrder

class Triangle {
    val color = floatArrayOf(255f, 0f, 0f, 1.0f)
    private var program: Int
    private var positionHandle: Int = 0
    private var colorHandle: Int = 0
    private var vertexBuffer = ByteBuffer.allocateDirect(triangleCoords.size*4).run {
        order(ByteOrder.nativeOrder())
        asFloatBuffer().apply {
            put(triangleCoords)
            position(0)
        }
    }

    private val vertexCount: Int = triangleCoords.size / COORDS_PER_VERTEX
    private val vertexStride: Int = COORDS_PER_VERTEX * 4 // 4 bytes per vertex

    private val vertexShaderCode = "attribute vec4 vPosition;" +
            "uniform mat4 uMVPMatrix;"+
            "void main(){" +
            "gl_Position = uMVPMatrix*vPosition;" +
            "}"

    private val fragmentShaderCode = "precision mediump float;" +
            "uniform vec4 vColor;" +
            "void main(){" +
            "gl_FragColor = vColor;" +
            "}"

    init {
        val vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
        val fragShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)
        program = GLES20.glCreateProgram().also {
            GLES20.glAttachShader(it, vertexShader)
            GLES20.glAttachShader(it, fragShader)
            GLES20.glLinkProgram(it)

        }
    }

    fun loadShader(type: Int, shaderCode: String): Int =
        GLES20.glCreateShader(type).also { shader ->
            GLES20.glShaderSource(shader, shaderCode)
            GLES20.glCompileShader(shader)
        }

    fun draw() {
        GLES20.glUseProgram(program)

        positionHandle = GLES20.glGetAttribLocation(program, "vPosition").also {
            GLES20.glEnableVertexAttribArray(it)
            GLES20.glVertexAttribPointer(
                it, COORDS_PER_VERTEX, GLES20.GL_FLOAT,
                false, vertexStride, vertexBuffer
            )

            colorHandle =
                GLES20.glGetUniformLocation(program, "vColor").also { colorHandleParameter ->
                    GLES20.glUniform4fv(colorHandleParameter, 1, color, 0)
                }


            val vPMatrixHandle = GLES20.glGetUniformLocation(program, "uMVPMatrix")
            val modelMatrix = FloatArray(16)
            Matrix.setRotateM(modelMatrix, 0, 0f, 0f, 1f, 0f)
            //ÉèÖÃÑØZÖáÕýÏòÎ»ÒÆ1
            Matrix.translateM(modelMatrix, 0, 0f, 0f, 1f)
            //ÉèÖÃÈÆyÖáÐý×ªyAngle¶È
            Matrix.rotateM(modelMatrix, 0, 0f, 0f, 1f, 0f)
            //ÉèÖÃÈÆxÖáÐý×ªxAngle¶È
            Matrix.rotateM(modelMatrix, 0, 0f, 1f, 0f, 0f)
            GLES20.glUniformMatrix4fv(vPMatrixHandle, 1, false, MatrixState.getFinalMatrix(modelMatrix), 0)

            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount)

            // Disable vertex array
            GLES20.glDisableVertexAttribArray(it)
        }
    }

    companion object {
        const val COORDS_PER_VERTEX = 3
        var triangleCoords = floatArrayOf(
            0f, 0.65f, 0.0f,
            -0.5f, -0.3f, 0.0f,
            0.5f, -0.3f, 0.0f
        )
    }
}