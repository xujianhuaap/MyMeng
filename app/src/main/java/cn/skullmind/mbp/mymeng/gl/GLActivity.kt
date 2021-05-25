package cn.skullmind.mbp.mymeng.gl

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import cn.skullmind.mbp.mymeng.R

fun startGLActivity(context:Context){
    val intent = Intent(context,GLActivity::class.java)
    context.startActivity(intent)
}
class GLActivity:FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val glView = MyGlSurfaceView(this)
        setContentView(glView)
    }
}