package cn.skullmind.mbp.tools

import android.content.res.Resources
import java.io.ByteArrayOutputStream

class ShaderUtil {
    companion object{
        fun loadShaderFromAsset(resource:Resources, fileName:String):String {
            val inputStream = resource.assets.open(fileName)
            val outputStream = ByteArrayOutputStream()

            inputStream.copyTo(outputStream,1024)

            return outputStream.toByteArray().toString()?.let {
                it.replace("\\r\\n","\n")
            }
        }
    }
}