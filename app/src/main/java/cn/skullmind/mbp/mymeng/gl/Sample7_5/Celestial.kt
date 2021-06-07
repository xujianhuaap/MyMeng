package cn.skullmind.mbp.mymeng.gl.Sample7_5

import android.opengl.GLES30
import cn.skullmind.mbp.mymeng.gl.Sample7_5.ShaderUtil.checkGlError
import cn.skullmind.mbp.mymeng.gl.Sample7_5.ShaderUtil.createProgram
import cn.skullmind.mbp.mymeng.gl.Sample7_5.ShaderUtil.loadFromAssetsFile
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class Celestial(//���ǳߴ�  
    var scale: Float, //������Y����ת�ĽǶ�
    var yAngle: Float, vCount: Int, mv: MySurfaceView
) {
    //��ʾ�ǿ��������
    val UNIT_SIZE = 10.0f //����뾶
    private lateinit var mVertexBuffer //�����������ݻ���
            : FloatBuffer
    var vCount = 0 //��������
    var mVertexShader //������ɫ�� ����ű�
            : String? = null
    var mFragmentShader //ƬԪ��ɫ������ű�
            : String? = null
    var mProgram //�Զ�����Ⱦ���߳���id 
            = 0
    var muMVPMatrixHandle //�ܱ任��������   
            = 0
    var maPositionHandle //����λ����������  
            = 0
    var uPointSizeHandle //����ߴ��������
            = 0

    fun initVertexData() { //�Զ����ʼ���������ݵ�initVertexData����  	  	
        //�����������ݵĳ�ʼ��       
        val vertices = FloatArray(vCount * 3)
        for (i in 0 until vCount) {
            //�������ÿ�����ǵ�xyz����
            val angleTempJD = Math.PI * 2 * Math.random()
            val angleTempWD = Math.PI * (Math.random() - 0.5f)
            vertices[i * 3] = (UNIT_SIZE * Math.cos(angleTempWD) * Math.sin(angleTempJD)).toFloat()
            vertices[i * 3 + 1] = (UNIT_SIZE * Math.sin(angleTempWD)).toFloat()
            vertices[i * 3 + 2] =
                (UNIT_SIZE * Math.cos(angleTempWD) * Math.cos(angleTempJD)).toFloat()
        }
        //���������������ݻ���
        val vbb = ByteBuffer.allocateDirect(vertices.size * 4)
        vbb.order(ByteOrder.nativeOrder()) //�����ֽ�˳��
        mVertexBuffer = vbb.asFloatBuffer() //ת��Ϊfloat�ͻ���
        mVertexBuffer.put(vertices) //�򻺳����з��붥������
        mVertexBuffer.position(0) //���û�������ʼλ��
    }

    fun intShader(mv: MySurfaceView) {    //��ʼ����ɫ��
        //���ض�����ɫ���Ľű�����       
        mVertexShader = loadFromAssetsFile("vertex_universe_sky.sh", mv.resources)
        checkGlError("==ss==")
        //����ƬԪ��ɫ���Ľű�����
        mFragmentShader = loadFromAssetsFile("frag_universe_sky.sh", mv.resources)
        //���ڶ�����ɫ����ƬԪ��ɫ����������
        checkGlError("==ss==")
        mProgram = createProgram(mVertexShader, mFragmentShader)
        //��ȡ�����ж���λ����������
        maPositionHandle = GLES30.glGetAttribLocation(mProgram, "aPosition")
        //��ȡ�������ܱ任��������
        muMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix")
        //��ȡ����ߴ��������
        uPointSizeHandle = GLES30.glGetUniformLocation(mProgram, "uPointSize")
    }

    fun drawSelf() {
        GLES30.glUseProgram(mProgram) //ָ��ʹ��ĳ����ɫ������
        //�����ձ任��������Ⱦ����
        GLES30.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.finalMatrix, 0)
        GLES30.glUniform1f(uPointSizeHandle, scale) //������ߴ紫����Ⱦ����
        GLES30.glVertexAttribPointer( //������λ������������Ⱦ����    
            maPositionHandle,
            3,
            GLES30.GL_FLOAT,
            false,
            3 * 4,
            mVertexBuffer
        )
        //���ö���λ����������
        GLES30.glEnableVertexAttribArray(maPositionHandle)
        GLES30.glDrawArrays(GLES30.GL_POINTS, 0, vCount) //�������ǵ�    
    }

    init {
        //��Y����ת�ĽǶ�
        this.vCount = vCount //���������
        initVertexData() //���ó�ʼ���������ݵ�initVertexData����
        intShader(mv) //���ó�ʼ����ɫ����initShader����
    }
}