package cn.skullmind.mbp.mymeng.fibonacci

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import cn.skullmind.mbp.mymeng.net.INet
import cn.skullmind.mbp.mymeng.utils.LogUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FibonacciRepo(val context: Context) {
    fun loadFibonacciBean(): FibonacciInfo {

        return FibonacciInfo("人耳朵", 90F)
    }

   fun loadFibonacciLabel():LiveData<String>{
        return liveData(Dispatchers.IO){
            val value = INet.getService(FibonacciData::class.java).getFibonacciLabel()
            emit(if(value.code() === 200)  "success" else "failure")
        }
    }



    companion object{
        const val TAG = "FibonacciRepo"
    }
}