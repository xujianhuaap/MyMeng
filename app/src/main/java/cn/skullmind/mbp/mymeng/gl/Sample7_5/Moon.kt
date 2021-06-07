package cn.skullmind.mbp.mymeng.gl.Sample7_5

import android.opengl.GLES30
import cn.skullmind.mbp.mymeng.gl.Sample7_5.ShaderUtil.createProgram
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.util.*

//��ʾ������࣬Ϊ��ͨ������δ���ö�������
class Moon(mv: MySurfaceView, r: Float) {
    var mProgram //�Զ�����Ⱦ���߳���id
            = 0
    var muMVPMatrixHandle //�ܱ任��������
            = 0
    var muMMatrixHandle //λ�á���ת�任����
            = 0
    var maCameraHandle //�����λ���������� 
            = 0
    var maPositionHandle //����λ����������
            = 0
    var maNormalHandle //���㷨������������  
            = 0
    var maTexCoorHandle //��������������������  
            = 0
    var maSunLightLocationHandle //��Դλ����������  
            = 0
    var mVertexShader //������ɫ��  ����ű�
            : String? = null
    var mFragmentShader //ƬԪ��ɫ������ű�
            : String? = null
    lateinit var mVertexBuffer //�����������ݻ���
            : FloatBuffer
    lateinit var mTexCoorBuffer //���������������ݻ���
            : FloatBuffer
    var vCount = 0

    //��ʼ���������ݵķ���
    fun initVertexData(r: Float) {
        //�����������ݵĳ�ʼ��================begin============================    	
        val UNIT_SIZE = 0.5f
        val alVertix = ArrayList<Float>() //��Ŷ��������ArrayList
        val angleSpan = 10f //������е�λ�зֵĽǶ�
        var vAngle = 90f
        while (vAngle > -90) {
            var hAngle = 360f
            while (hAngle > 0) {
                //����������һ���ǶȺ�����Ӧ�Ĵ˵��������ϵ�����    	
                var xozLength = r * UNIT_SIZE * Math.cos(Math.toRadians(vAngle.toDouble()))
                val x1 = (xozLength * Math.cos(Math.toRadians(hAngle.toDouble()))).toFloat()
                val z1 = (xozLength * Math.sin(Math.toRadians(hAngle.toDouble()))).toFloat()
                val y1 = (r * UNIT_SIZE * Math.sin(Math.toRadians(vAngle.toDouble()))).toFloat()
                xozLength =
                    r * UNIT_SIZE * Math.cos(Math.toRadians((vAngle - angleSpan).toDouble()))
                val x2 = (xozLength * Math.cos(Math.toRadians(hAngle.toDouble()))).toFloat()
                val z2 = (xozLength * Math.sin(Math.toRadians(hAngle.toDouble()))).toFloat()
                val y2 =
                    (r * UNIT_SIZE * Math.sin(Math.toRadians((vAngle - angleSpan).toDouble()))).toFloat()
                xozLength =
                    r * UNIT_SIZE * Math.cos(Math.toRadians((vAngle - angleSpan).toDouble()))
                val x3 =
                    (xozLength * Math.cos(Math.toRadians((hAngle - angleSpan).toDouble()))).toFloat()
                val z3 =
                    (xozLength * Math.sin(Math.toRadians((hAngle - angleSpan).toDouble()))).toFloat()
                val y3 =
                    (r * UNIT_SIZE * Math.sin(Math.toRadians((vAngle - angleSpan).toDouble()))).toFloat()
                xozLength = r * UNIT_SIZE * Math.cos(Math.toRadians(vAngle.toDouble()))
                val x4 =
                    (xozLength * Math.cos(Math.toRadians((hAngle - angleSpan).toDouble()))).toFloat()
                val z4 =
                    (xozLength * Math.sin(Math.toRadians((hAngle - angleSpan).toDouble()))).toFloat()
                val y4 = (r * UNIT_SIZE * Math.sin(Math.toRadians(vAngle.toDouble()))).toFloat()

                //������һ������
                alVertix.add(x1)
                alVertix.add(y1)
                alVertix.add(z1)
                alVertix.add(x2)
                alVertix.add(y2)
                alVertix.add(z2)
                alVertix.add(x4)
                alVertix.add(y4)
                alVertix.add(z4)
                //�����ڶ�������
                alVertix.add(x4)
                alVertix.add(y4)
                alVertix.add(z4)
                alVertix.add(x2)
                alVertix.add(y2)
                alVertix.add(z2)
                alVertix.add(x3)
                alVertix.add(y3)
                alVertix.add(z3)
                hAngle = hAngle - angleSpan
            }
            vAngle = vAngle - angleSpan
        }
        vCount = alVertix.size / 3 //���������Ϊ����ֵ������1/3����Ϊһ��������3������

        //��alVertix�е�����ֵת�浽һ��float������
        val vertices = FloatArray(vCount * 3)
        for (i in alVertix.indices) {
            vertices[i] = alVertix[i]
        }

        //���������������ݻ���
        //vertices.length*4����Ϊһ�������ĸ��ֽ�
        val vbb = ByteBuffer.allocateDirect(vertices.size * 4)
        vbb.order(ByteOrder.nativeOrder()) //�����ֽ�˳��
        mVertexBuffer = vbb.asFloatBuffer() //ת��Ϊfloat�ͻ���
        mVertexBuffer.put(vertices) //�򻺳����з��붥������
        mVertexBuffer.position(0) //���û�������ʼλ��
        //�ر���ʾ�����ڲ�ͬƽ̨�ֽ�˳��ͬ���ݵ�Ԫ�����ֽڵ�һ��Ҫ����ByteBuffer
        //ת�����ؼ���Ҫͨ��ByteOrder����nativeOrder()�������п��ܻ������

        //��alTexCoor�е���������ֵת�浽һ��float������
        val texCoor = generateTexCoor( //��ȡ�з���ͼ����������                
            (360 / angleSpan).toInt(),  //����ͼ�зֵ�����
            (180 / angleSpan).toInt() //����ͼ�зֵ�����
        )
        val llbb = ByteBuffer.allocateDirect(texCoor.size * 4)
        llbb.order(ByteOrder.nativeOrder()) //�����ֽ�˳��
        mTexCoorBuffer = llbb.asFloatBuffer()
        mTexCoorBuffer.put(texCoor)
        mTexCoorBuffer.position(0)
        //�����������ݵĳ�ʼ��================end============================
    }

    //��ʼ����ɫ��
    fun initShader(mv: MySurfaceView) {
        //���ض�����ɫ���Ľű�����       
        mVertexShader = ShaderUtil.loadFromAssetsFile("vertex_moon.sh", mv.resources)
        ShaderUtil.checkGlError("==ss==")
        //����ƬԪ��ɫ���Ľű�����
        mFragmentShader = ShaderUtil.loadFromAssetsFile("frag_moon.sh", mv.resources)
        //���ڶ�����ɫ����ƬԪ��ɫ����������
        ShaderUtil.checkGlError("==ss==")
        mProgram = createProgram(mVertexShader, mFragmentShader)
        //��ȡ�����ж���λ���������� 
        maPositionHandle = GLES30.glGetAttribLocation(mProgram, "aPosition")
        //��ȡ�����ж��㾭γ����������   
        maTexCoorHandle = GLES30.glGetAttribLocation(mProgram, "aTexCoor")
        //��ȡ�����ж��㷨������������  
        maNormalHandle = GLES30.glGetAttribLocation(mProgram, "aNormal")
        //��ȡ�������ܱ任��������
        muMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix")
        //��ȡ�����������λ������
        maCameraHandle = GLES30.glGetUniformLocation(mProgram, "uCamera")
        //��ȡ�����й�Դλ������
        maSunLightLocationHandle = GLES30.glGetUniformLocation(mProgram, "uLightLocationSun")
        //��ȡλ�á���ת�任��������
        muMMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMMatrix")
    }

    fun drawSelf(texId: Int) {
        //ָ��ʹ��ĳ��shader����
        GLES30.glUseProgram(mProgram)
        //�����ձ任��������Ⱦ����
        GLES30.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.finalMatrix, 0)
        //��λ�á���ת�任��������Ⱦ����
        GLES30.glUniformMatrix4fv(muMMatrixHandle, 1, false, MatrixState.mMatrix, 0)
        //�������λ�ô�����Ⱦ����  
        GLES30.glUniform3fv(maCameraHandle, 1, MatrixState.cameraFB)
        //����Դλ�ô�����Ⱦ����
        GLES30.glUniform3fv(maSunLightLocationHandle, 1, MatrixState.lightPositionFBSun)

        //������λ������������Ⱦ����
        GLES30.glVertexAttribPointer(
            maPositionHandle,
            3,
            GLES30.GL_FLOAT,
            false,
            3 * 4,
            mVertexBuffer
        )
        //��������������������Ⱦ����
        GLES30.glVertexAttribPointer(
            maTexCoorHandle,
            2,
            GLES30.GL_FLOAT,
            false,
            2 * 4,
            mTexCoorBuffer
        )
        //�����㷨��������������Ⱦ����
        GLES30.glVertexAttribPointer(
            maNormalHandle,
            4,
            GLES30.GL_FLOAT,
            false,
            3 * 4,
            mVertexBuffer
        )
        //���ö���λ����������
        GLES30.glEnableVertexAttribArray(maPositionHandle)
        //���ö���������������
        GLES30.glEnableVertexAttribArray(maTexCoorHandle)
        //���ö��㷨������������
        GLES30.glEnableVertexAttribArray(maNormalHandle)
        //������
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0)
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texId)
        //�������η�ʽִ�л���
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vCount)
    }

    //�Զ��з����������������ķ���
    fun generateTexCoor(bw: Int, bh: Int): FloatArray {
        val result = FloatArray(bw * bh * 6 * 2)
        val sizew = 1.0f / bw //����
        val sizeh = 1.0f / bh //����
        var c = 0
        for (i in 0 until bh) {
            for (j in 0 until bw) {
                //ÿ����һ�����Σ������������ι��ɣ��������㣬12����������
                val s = j * sizew
                val t = i * sizeh
                result[c++] = s
                result[c++] = t
                result[c++] = s
                result[c++] = t + sizeh
                result[c++] = s + sizew
                result[c++] = t
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

    init {
        //���ó�ʼ���������ݵķ���
        initVertexData(r)
        //���ó�ʼ����ɫ���ķ���        
        initShader(mv)
    }
}