package cn.skullmind.mbp.mymeng.gl.Sample7_5

import android.content.res.Resources
import android.opengl.GLES30
import android.util.Log
import java.io.ByteArrayOutputStream

//���ض���Shader��ƬԪShader�Ĺ�����
object ShaderUtil {
    //�����ƶ�shader�ķ���
    fun loadShader(
        shaderType: Int,  //shader������  GLES30.GL_VERTEX_SHADER   GLES30.GL_FRAGMENT_SHADER
        source: String? //shader�Ľű��ַ���
    ): Int {
        //����һ����shader
        var shader = GLES30.glCreateShader(shaderType)
        //�������ɹ������shader
        if (shader != 0) {
            //����shader��Դ����
            GLES30.glShaderSource(shader, source)
            //����shader
            GLES30.glCompileShader(shader)
            //��ű���ɹ�shader����������
            val compiled = IntArray(1)
            //��ȡShader�ı������
            GLES30.glGetShaderiv(shader, GLES30.GL_COMPILE_STATUS, compiled, 0)
            if (compiled[0] == 0) { //������ʧ������ʾ������־��ɾ����shader
                Log.e("ES30_ERROR", "Could not compile shader $shaderType:")
                Log.e("ES30_ERROR", GLES30.glGetShaderInfoLog(shader))
                GLES30.glDeleteShader(shader)
                shader = 0
            }
        }
        return shader
    }

    //����shader����ķ���
    fun createProgram(vertexSource: String?, fragmentSource: String?): Int {
        //���ض�����ɫ��
        val vertexShader = loadShader(GLES30.GL_VERTEX_SHADER, vertexSource)
        if (vertexShader == 0) {
            return 0
        }

        //����ƬԪ��ɫ��
        val pixelShader = loadShader(GLES30.GL_FRAGMENT_SHADER, fragmentSource)
        if (pixelShader == 0) {
            return 0
        }

        //��������
        var program = GLES30.glCreateProgram()
        //�����򴴽��ɹ���������м��붥����ɫ����ƬԪ��ɫ��
        if (program != 0) {
            //������м��붥����ɫ��
            GLES30.glAttachShader(program, vertexShader)
            checkGlError("glAttachShader")
            //������м���ƬԪ��ɫ��
            GLES30.glAttachShader(program, pixelShader)
            checkGlError("glAttachShader")
            //���ӳ���
            GLES30.glLinkProgram(program)
            //������ӳɹ�program����������
            val linkStatus = IntArray(1)
            //��ȡprogram���������
            GLES30.glGetProgramiv(program, GLES30.GL_LINK_STATUS, linkStatus, 0)
            //������ʧ���򱨴�ɾ������
            if (linkStatus[0] != GLES30.GL_TRUE) {
                Log.e("ES30_ERROR", "Could not link program: ")
                Log.e("ES30_ERROR", GLES30.glGetProgramInfoLog(program))
                GLES30.glDeleteProgram(program)
                program = 0
            }
        }
        return program
    }

    //���ÿһ�������Ƿ��д���ķ��� 
    @JvmStatic
    fun checkGlError(op: String) {
        var error: Int
        while (GLES30.glGetError().also { error = it } != GLES30.GL_NO_ERROR) {
            Log.e("ES30_ERROR", "$op: glError $error")
            throw RuntimeException("$op: glError $error")
        }
    }

    //��sh�ű��м���shader���ݵķ���
    @JvmStatic
    fun loadFromAssetsFile(fname: String?, r: Resources): String? {
        var result: String? = null
        try {
            val `in` = r.assets.open(fname!!)
            var ch = 0
            val baos = ByteArrayOutputStream()
            while (`in`.read().also { ch = it } != -1) {
                baos.write(ch)
            }
            val buff = baos.toByteArray()
            baos.close()
            `in`.close()
            result = String(buff)
            result = result.replace("\\r\\n".toRegex(), "\n")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }
}