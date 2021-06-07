package cn.skullmind.mbp.mymeng.gl.Sample7_5

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.GLES30
import android.opengl.GLSurfaceView
import android.opengl.GLUtils
import android.view.MotionEvent
import cn.skullmind.mbp.mymeng.R
import cn.skullmind.mbp.mymeng.gl.Sample7_5.Constant.ratio
import cn.skullmind.mbp.mymeng.gl.Sample7_5.Constant.threadFlag
import java.io.IOException
import java.io.InputStream
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

@SuppressLint("ClickableViewAccessibility")
class MySurfaceView(context: Context?) : GLSurfaceView(context) {
    private val TOUCH_SCALE_FACTOR = 180.0f / 320 //�Ƕ����ű���
    private val mRenderer //������Ⱦ��
            : SceneRenderer
    private var mPreviousX //�ϴεĴ���λ��X����
            = 0f
    private var mPreviousY //�ϴεĴ���λ��Y����
            = 0f
    var textureIdEarth //ϵͳ����ĵ�������id
            = 0
    var textureIdEarthNight //ϵͳ����ĵ���ҹ������id
            = 0
    var textureIdMoon //ϵͳ�������������id    
            = 0
    var yAngle = 0f //̫���ƹ���y����ת�ĽǶ�
    var xAngle = 0f //�������X����ת�ĽǶ�
    var eAngle = 0f //������ת�Ƕ�    
    var cAngle = 0f //������ת�ĽǶ�

    //�����¼��ص�����
    override fun onTouchEvent(e: MotionEvent): Boolean {
        val x: Float = e.getX()
        val y: Float = e.getY()
        when (e.getAction()) {
            MotionEvent.ACTION_MOVE -> {
                //���غ���λ��̫����y����ת
                val dx = x - mPreviousX //���㴥�ر�Xλ�� 
                yAngle += dx * TOUCH_SCALE_FACTOR //��Xλ������ɽǶ�
                val sunx = (Math.cos(Math.toRadians(yAngle.toDouble())) * 100).toFloat()
                val sunz = (-(Math.sin(Math.toRadians(yAngle.toDouble())) * 100)).toFloat()
                MatrixState.setLightLocationSun(sunx, 5f, sunz)

                //��������λ���������x����ת -90��+90
                val dy = y - mPreviousY //���㴥�ر�Yλ�� 
                xAngle += dy * TOUCH_SCALE_FACTOR //��Yλ���������X����ת�ĽǶ�
                if (xAngle > 90) {
                    xAngle = 90f
                } else if (xAngle < -90) {
                    xAngle = -90f
                }
                val cy = (7.2 * Math.sin(Math.toRadians(xAngle.toDouble()))).toFloat()
                val cz = (7.2 * Math.cos(Math.toRadians(xAngle.toDouble()))).toFloat()
                val upy = Math.cos(Math.toRadians(xAngle.toDouble()))
                    .toFloat()
                val upz = (-Math.sin(Math.toRadians(xAngle.toDouble()))).toFloat()
                MatrixState.setCamera(0f, cy, cz, 0f, 0f, 0f, 0f, upy, upz)
            }
        }
        mPreviousX = x //��¼���ر�λ��
        mPreviousY = y
        return true
    }

    private inner class SceneRenderer : GLSurfaceView.Renderer {
        var earth //����
                : Earth? = null
        var moon //����
                : Moon? = null
        var cSmall //С��������
                : Celestial? = null
        var cBig //����������
                : Celestial? = null

        override fun onDrawFrame(gl: GL10) {
            //�����Ȼ�������ɫ����
            GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT or GLES30.GL_COLOR_BUFFER_BIT)

            //�����ֳ�
            MatrixState.pushMatrix()
            //������ת
            MatrixState.rotate(eAngle, 0f, 1f, 0f)
            //���Ƶ���
            earth!!.drawSelf(textureIdEarth, textureIdEarthNight)
            //������ϵ������λ��            
            MatrixState.translate(2f, 0f, 0f)
            //������ת     
            MatrixState.rotate(eAngle, 0f, 1f, 0f)
            //��������
            moon!!.drawSelf(textureIdMoon)
            //�ָ��ֳ�
            MatrixState.popMatrix()

            //�����ֳ�
            MatrixState.pushMatrix()
            //�ǿ�������ת
            MatrixState.rotate(cAngle, 0f, 1f, 0f)
            //����С�ߴ����ǵ�����
            cSmall!!.drawSelf()
            //���ƴ�ߴ����ǵ�����
            cBig!!.drawSelf()
            //�ָ��ֳ�
            MatrixState.popMatrix()
        }

        override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
            //�����Ӵ���С��λ�� 
            GLES30.glViewport(0, 0, width, height)
            //����GLSurfaceView�Ŀ�߱�
            ratio = width.toFloat() / height
            //���ô˷����������͸��ͶӰ����
            MatrixState.setProjectFrustum(-ratio, ratio, -1f, 1f, 4f, 100f)
            //���ô˷������������9����λ�þ���
            MatrixState.setCamera(0f, 0f, 7.2f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)
            //�򿪱������
            GLES30.glEnable(GLES30.GL_CULL_FACE)
            //��ʼ������
            textureIdEarth = initTexture(R.mipmap.earth)
            textureIdEarthNight = initTexture(R.mipmap.earthn)
            textureIdMoon = initTexture(R.mipmap.moon)
            //����̫���ƹ�ĳ�ʼλ��
            MatrixState.setLightLocationSun(100f, 5f, 0f)

            //����һ���̶߳�ʱ��ת��������
            object : Thread() {
                override fun run() {
                    while (threadFlag) {
                        //������ת�Ƕ�
                        eAngle = (eAngle + 2) % 360
                        //������ת�Ƕ�
                        cAngle = (cAngle + 0.2f) % 360
                        try {
                            sleep(100)
                        } catch (e: InterruptedException) {
                            e.printStackTrace()
                        }
                    }
                }
            }.start()
        }

        override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
            //������Ļ����ɫRGBA
            GLES30.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
            //����������� 
            earth = Earth(this@MySurfaceView, 2.0f)
            //����������� 
            moon = Moon(this@MySurfaceView, 1.0f)
            //����С�����������
            cSmall = Celestial(1f, 0f, 1000, this@MySurfaceView)
            //�����������������
            cBig = Celestial(2f, 0f, 500, this@MySurfaceView)
            //����ȼ��
            GLES30.glEnable(GLES30.GL_DEPTH_TEST)
            //��ʼ���任����
            MatrixState.setInitStack()
        }
    }

    fun initTexture(drawableId: Int): Int //textureId
    {
        //��������ID
        val textures = IntArray(1)
        GLES30.glGenTextures(
            1,  //����������id������
            textures,  //����id������
            0 //ƫ����
        )
        val textureId = textures[0]
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureId)
        GLES30.glTexParameterf(
            GLES30.GL_TEXTURE_2D,
            GLES30.GL_TEXTURE_MIN_FILTER,
            GLES30.GL_NEAREST.toFloat()
        )
        GLES30.glTexParameterf(
            GLES30.GL_TEXTURE_2D,
            GLES30.GL_TEXTURE_MAG_FILTER,
            GLES30.GL_LINEAR.toFloat()
        )
        GLES30.glTexParameterf(
            GLES30.GL_TEXTURE_2D,
            GLES30.GL_TEXTURE_WRAP_S,
            GLES30.GL_CLAMP_TO_EDGE.toFloat()
        )
        GLES30.glTexParameterf(
            GLES30.GL_TEXTURE_2D,
            GLES30.GL_TEXTURE_WRAP_T,
            GLES30.GL_CLAMP_TO_EDGE.toFloat()
        )

        //ͨ������������ͼƬ===============begin===================
        val `is`: InputStream = this.getResources().openRawResource(drawableId)
        val bitmapTmp: Bitmap
        bitmapTmp = try {
            BitmapFactory.decodeStream(`is`)
        } finally {
            try {
                `is`.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        //ͨ������������ͼƬ===============end=====================  

        //ʵ�ʼ�������
        GLUtils.texImage2D(
            GLES30.GL_TEXTURE_2D,  //��������
            0,  //����Ĳ�Σ�0��ʾ����ͼ��㣬�������Ϊֱ����ͼ
            bitmapTmp,  //����ͼ��
            0 //����߿�ߴ�
        )
        bitmapTmp.recycle() //������سɹ����ͷ�ͼƬ
        return textureId
    }

    init {
        this.setEGLContextClientVersion(3) //����ʹ��OPENGL ES3.0
        mRenderer = SceneRenderer() //����������Ⱦ��
        setRenderer(mRenderer) //������Ⱦ��		        
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY) //������ȾģʽΪ������Ⱦ   
    }
}