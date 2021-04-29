package cn.skullmind.mbp.mymeng.fibonacci

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import cn.skullmind.mbp.mymeng.R
import cn.skullmind.mbp.mymeng.databinding.ActivityFibonacciBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.jar.Manifest


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

        val observer = Observer<String>(){
            vm.label.set(it)
        }
        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED){
            repo.loadFibonacciLabel().observe(this,observer)
        }else{
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.INTERNET),12)

        }
    }
}