package cn.skullmind.mbp.mymeng.gl.Sample7_5

import android.opengl.Matrix
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.util.*

//�洢ϵͳ����״̬����
object MatrixState {
    private val mProjMatrix = FloatArray(16) //4x4���� ͶӰ��
    private val mVMatrix = FloatArray(16) //�����λ�ó���9��������   

    //��ȡ��������ı任����
    lateinit var mMatrix //��ǰ�任����
            : FloatArray
        private set
    var lightLocationSun = floatArrayOf(0f, 0f, 0f) //̫����λ���Դλ��
    lateinit var cameraFB: FloatBuffer
    lateinit var lightPositionFBSun: FloatBuffer
    var mStack = Stack<FloatArray>() //�����任�����ջ
    fun setInitStack() //��ȡ���任��ʼ����
    {
        mMatrix = FloatArray(16)
        Matrix.setRotateM(mMatrix, 0, 0f, 1f, 0f, 0f)
    }

    fun pushMatrix() //�����任����
    {
        mStack.push(mMatrix.clone())
    }

    fun popMatrix() //�ָ��任����
    {
        mMatrix = mStack.pop()
    }

    fun translate(x: Float, y: Float, z: Float) //������xyz���ƶ�
    {
        Matrix.translateM(mMatrix, 0, x, y, z)
    }

    fun rotate(angle: Float, x: Float, y: Float, z: Float) //������xyz���ƶ�
    {
        Matrix.rotateM(mMatrix, 0, angle, x, y, z)
    }

    //���������
    fun setCamera(
        cx: Float,  //�����λ��x
        cy: Float,  //�����λ��y
        cz: Float,  //�����λ��z
        tx: Float,  //�����Ŀ���x
        ty: Float,  //�����Ŀ���y
        tz: Float,  //�����Ŀ���z
        upx: Float,  //�����UP����X����
        upy: Float,  //�����UP����Y����
        upz: Float //�����UP����Z����		
    ) {
        Matrix.setLookAtM(
            mVMatrix,
            0,
            cx,
            cy,
            cz,
            tx,
            ty,
            tz,
            upx,
            upy,
            upz
        )
        val cameraLocation = FloatArray(3) //�����λ��
        cameraLocation[0] = cx
        cameraLocation[1] = cy
        cameraLocation[2] = cz
        val llbb = ByteBuffer.allocateDirect(3 * 4)
        llbb.order(ByteOrder.nativeOrder()) //�����ֽ�˳��
        cameraFB = llbb.asFloatBuffer()
        cameraFB.put(cameraLocation)
        cameraFB.position(0)
    }

    //����͸��ͶӰ����
    fun setProjectFrustum(
        left: Float,  //near���left
        right: Float,  //near���right
        bottom: Float,  //near���bottom
        top: Float,  //near���top
        near: Float,  //near�����
        far: Float //far�����
    ) {
        Matrix.frustumM(mProjMatrix, 0, left, right, bottom, top, near, far)
    }

    //��������ͶӰ����
    fun setProjectOrtho(
        left: Float,  //near���left
        right: Float,  //near���right
        bottom: Float,  //near���bottom
        top: Float,  //near���top
        near: Float,  //near�����
        far: Float //far�����
    ) {
        Matrix.orthoM(mProjMatrix, 0, left, right, bottom, top, near, far)
    }

    //��ȡ����������ܱ任����
    @JvmStatic
    val finalMatrix: FloatArray
        get() {
            val mMVPMatrix = FloatArray(16)
            Matrix.multiplyMM(mMVPMatrix, 0, mVMatrix, 0, mMatrix, 0)
            Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mMVPMatrix, 0)
            return mMVPMatrix
        }

    //����̫����Դλ�õķ���
    fun setLightLocationSun(x: Float, y: Float, z: Float) {
        lightLocationSun[0] = x
        lightLocationSun[1] = y
        lightLocationSun[2] = z
        val llbb = ByteBuffer.allocateDirect(3 * 4)
        llbb.order(ByteOrder.nativeOrder()) //�����ֽ�˳��
        lightPositionFBSun = llbb.asFloatBuffer()
        lightPositionFBSun.put(lightLocationSun)
        lightPositionFBSun.position(0)
    }
}