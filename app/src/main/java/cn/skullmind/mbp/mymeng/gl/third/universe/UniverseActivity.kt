package cn.skullmind.mbp.mymeng.gl.third.universe

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import cn.skullmind.mbp.mymeng.R
import cn.skullmind.mbp.mymeng.gl.second.Gl20SurfaceView
import cn.skullmind.mbp.mymeng.gl.third.Gl30SurfaceView
import cn.skullmind.mbp.mymeng.gl.third.renders.GL30UniverseSkyRender
import java.util.*

fun startUniverseActivity(context: Context) {
    val intent = Intent(context, UniverseActivity::class.java)
    context.startActivity(intent)
}

class UniverseActivity : FragmentActivity() {
    private lateinit var universeSurfaceView:Gl30UniverseSurfaceView
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
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }
}