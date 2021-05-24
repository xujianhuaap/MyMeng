package cn.skullmind.mbp.mymeng

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import cn.skullmind.mbp.mymeng.chrome.startChromeActivity
import cn.skullmind.mbp.mymeng.chrome.startEyeActivity
import cn.skullmind.mbp.mymeng.fibonacci.startFibonacciActivity
import cn.skullmind.mbp.mymeng.gl.startGLActivity
import cn.skullmind.mbp.mymeng.pick_picture.startPickPictureActivity
import cn.skullmind.mbp.mymeng.user.startUserActivity
import cn.skullmind.mbp.mymeng.work_manager.ImageOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }


        findViewById<View>(R.id.tv_camera).setOnClickListener {
            startPickPictureActivity(this)
        }

        findViewById<View>(R.id.tv_image_upload).setOnClickListener {

            ImageOptions().build(this).enqueue()
        }

        findViewById<View>(R.id.tv_user).setOnClickListener {
            startUserActivity(this)

        }
        findViewById<View>(R.id.tv_fibonacci).setOnClickListener {
            startFibonacciActivity(this)

        }
        findViewById<View>(R.id.tv_chrome).setOnClickListener {
            startChromeActivity(this)

        }

        findViewById<View>(R.id.tv_eye_view).setOnClickListener {
            startEyeActivity(this)

        }

        findViewById<View>(R.id.tv_gl).setOnClickListener {
            startGLActivity(this)

        }




    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}