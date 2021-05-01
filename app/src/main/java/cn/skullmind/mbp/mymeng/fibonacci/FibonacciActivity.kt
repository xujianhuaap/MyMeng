package cn.skullmind.mbp.mymeng.fibonacci

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
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

        val binding = DataBindingUtil.setContentView<ActivityFibonacciBinding>(
            this,
            R.layout.activity_fibonacci
        )
        binding.vm = getViewModel()
        binding.lifecycleOwner = this
        initVm(binding)
    }

    private fun initVm(binding: ActivityFibonacciBinding) {
        val observer = Observer<String>() {
            binding.vm?.label?.set(it)
        }
        binding.vm?.getLabelData()?.observe(this, observer)
    }

    private fun getViewModel(): FibonacciViewModel {
        val repo = FibonacciRepo(applicationContext)
        val factory = FibonacciViewModelFactory(repo)
        return ViewModelProvider(this, factory).get(FibonacciViewModel::class.java)
    }
}