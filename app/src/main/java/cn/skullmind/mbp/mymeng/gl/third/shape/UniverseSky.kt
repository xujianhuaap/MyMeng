package cn.skullmind.mbp.mymeng.gl.third.shape

import android.opengl.GLES30
import cn.skullmind.mbp.mymeng.gl.GLShape
import cn.skullmind.mbp.tools.ShaderUtil
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class UniverseSky(var pointCount: Int):GLShape {
    private lateinit var pointBuffer: FloatBuffer

    private var program:Int = 0
    private var aPositionHandle  = 0
    private var uPointSizeHandle = 0

    init {
        if(pointCount < 100){
            pointCount = 100
        }

        val points = FloatArray(pointCount * 3)
        for(i in 0 until pointCount){

            //�������ÿ�����ǵ�xyz����
            val angleTempJD = Math.PI * 2 * Math.random()
            val angleTempWD = Math.PI * (Math.random() - 0.5f)
            points[i * 3] = (UNIT_SIZE * Math.cos(angleTempWD) * Math.sin(angleTempJD)).toFloat()
            points[i * 3 + 1] = (UNIT_SIZE * Math.sin(angleTempWD)).toFloat()
            points[i * 3 + 2] = (UNIT_SIZE * Math.cos(angleTempWD) * Math.cos(angleTempJD)).toFloat()
        }

        pointBuffer = ByteBuffer.allocateDirect(pointCount*4).run {
            order(ByteOrder.nativeOrder())
            asFloatBuffer().apply {
                put(points)
                position(0)
            }
        }

        ShaderUtil.initShader(GLES30.GL_VERTEX_SHADER,"")

        uPointSizeHandle = GLES30.glGetUniformLocation(program,"uPointSize")
    }
    override fun draw() {

    }

    companion object {
        const val UNIT_SIZE = 1f
    }
}