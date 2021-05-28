package cn.skullmind.mbp.tools

import android.content.res.Resources
import android.opengl.GLES30
import android.util.Log
import java.io.ByteArrayOutputStream

object ShaderUtil {
    fun initShader(type: Int, fileName: String, resource: Resources): Int {
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
    fun loadShaderFromAsset(resource:Resources, fileName:String):String {
        val inputStream = resource.assets.open(fileName)
        val outputStream = ByteArrayOutputStream()

        inputStream.copyTo(outputStream,1024)
        outputStream.flush()
        inputStream.close()
        outputStream.close()
        return String(outputStream.toByteArray()).let {
            it.replace("\\r\\n","\n")
        }
    }
}