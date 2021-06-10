package cn.skullmind.mbp.mymeng.gl.third.universe

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import cn.skullmind.mbp.mymeng.R
import java.util.*

fun startUniverseActivity(context: Context) {
    val intent = Intent(context, UniverseActivity::class.java)
    context.startActivity(intent)
}

class UniverseActivity : FragmentActivity() {
    private lateinit var universeSurfaceView: Gl30UniverseSurfaceView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_universe)
        hideSystemUI()
        universeSurfaceView = Gl30UniverseSurfaceView(this@UniverseActivity)
        findViewById<ViewGroup>(R.id.container).apply {
            addView(universeSurfaceView)
        }
    }


    override fun onResume() {
        super.onResume()
        universeSurfaceView.resumeRotateTask()
    }

    override fun onPause() {
        super.onPause()
        universeSurfaceView.cancelRotateTask()
    }


    private fun hideSystemUI() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }
}