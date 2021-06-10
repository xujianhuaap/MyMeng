package cn.skullmind.mbp.mymeng.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.skullmind.mbp.mymeng.R
import cn.skullmind.mbp.mymeng.chrome.startChromeActivity
import cn.skullmind.mbp.mymeng.chrome.startEyeActivity
import cn.skullmind.mbp.mymeng.fibonacci.startFibonacciActivity
import cn.skullmind.mbp.mymeng.gl.third.universe.startUniverseActivity
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


        findViewById<RecyclerView>(R.id.recycler_view).apply {
            adapter = FunctionAdapter(getFunctionData()) {

                handleClickItem(it)
            }

            layoutManager = LinearLayoutManager(this@MainActivity)
        }


    }

    private fun handleClickItem(it: Function) {
        if (Function.UNIVERSE_SKY == it) {
            startUniverseActivity(this@MainActivity)
        }

        if (Function.FIBONACCI_CURVE == it) {
            startFibonacciActivity(this@MainActivity)

        }

        if (Function.CHROME_LOGO == it) {
            startChromeActivity(this@MainActivity)
        }


        if (Function.VIEW_DRAW == it) {
            startEyeActivity(this@MainActivity)

        }

        if (Function.CAMERA_USE == it) {
            startPickPictureActivity(this@MainActivity)
        }

        if (Function.DB_USE == it) {

            startUserActivity(this@MainActivity)
        }

        if (Function.WORKER_MANAGER_USE == it) {

            ImageOptions().build(this@MainActivity).enqueue()
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

    private fun getFunctionData(): List<Function> {
        val datas = ArrayList<Function>()
        datas.add(Function.UNIVERSE_SKY)
        datas.add(Function.FIBONACCI_CURVE)
        datas.add(Function.CHROME_LOGO)
        datas.add(Function.VIEW_DRAW)
        datas.add(Function.CAMERA_USE)
        return datas
    }
}