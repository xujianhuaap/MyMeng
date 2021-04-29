package cn.skullmind.mbp.mymeng.fibonacci

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import cn.skullmind.mbp.mymeng.R
import cn.skullmind.mbp.mymeng.databinding.ActivityFibonacciBinding


fun startFibonacciActivity(context: AppCompatActivity) {
    val intent = Intent(context, FibonacciActivity::class.java)
    context.startActivity(intent)
}

class FibonacciActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(android.R.style.Theme_Material_NoActionBar)
        val repo = FibonacciRepo(applicationContext)
        val factory = FibonacciViewModelFactory(repo)
        val vm = ViewModelProvider(this, factory).get(FibonacciViewModel::class.java)

        DataBindingUtil.setContentView<ActivityFibonacciBinding>(
            this,
            R.layout.activity_fibonacci
        ).also {
            it.vm = vm
            it.lifecycleOwner = this@FibonacciActivity
        }
    }
}