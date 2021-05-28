package cn.skullmind.mbp.mymeng.gl

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import cn.skullmind.mbp.mymeng.R
import cn.skullmind.mbp.mymeng.gl.second.Gl20SurfaceView
import cn.skullmind.mbp.mymeng.gl.third.Gl30SurfaceView

fun startGLActivity(context: Context) {
    val intent = Intent(context, GLActivity::class.java)
    context.startActivity(intent)
}

class GLActivity : FragmentActivity() {
    private lateinit var container: ViewGroup
    private var isOOD = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gl)
        container = findViewById<ViewGroup>(R.id.container).apply {
            addView(Gl30SurfaceView(this@GLActivity))
            addView(Gl20SurfaceView(this@GLActivity))
        }
        findViewById<View>(R.id.tv_switch_view).setOnClickListener {
            getGL20SurfaceView().visibility = if (isOOD) View.VISIBLE else View.GONE
            getGL30SurfaceView().visibility = if (isOOD) View.GONE else View.VISIBLE
            isOOD = !isOOD
        }
    }

    private fun getGL30SurfaceView() = container.getChildAt(1)

    private fun getGL20SurfaceView() = container.getChildAt(0)
}