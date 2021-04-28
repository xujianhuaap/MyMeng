package cn.skullmind.mbp.mymeng.fibonacci

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import cn.skullmind.mbp.mymeng.R
import cn.skullmind.mbp.mymeng.databinding.ActivityFibonacciBinding


fun startFibonacciActivity(context: AppCompatActivity) {
    val intent = Intent(context, FibonacciActivity::class.java)
    context.startActivity(intent)
}

class FibonacciActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityFibonacciBinding>(this,R.layout.activity_fibonacci)
    }
}