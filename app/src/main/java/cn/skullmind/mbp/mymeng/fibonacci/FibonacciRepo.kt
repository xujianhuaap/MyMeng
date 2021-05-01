package cn.skullmind.mbp.mymeng.fibonacci

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import cn.skullmind.mbp.mymeng.fibonacci.beans.FibonacciInfo
import cn.skullmind.mbp.mymeng.net.INet
import cn.skullmind.mbp.mymeng.utils.LogUtil
import kotlinx.coroutines.Dispatchers

class FibonacciRepo(val context: Context) {
    fun loadFibonacciBean(): FibonacciInfo {

        return FibonacciInfo("人耳朵", 90F)
    }

   fun loadFibonacciLabel():LiveData<String>{
       LogUtil.d("sss","new live Data "+Thread.currentThread().name)
        return liveData(Dispatchers.IO){
            LogUtil.d("sss",Thread.currentThread().name)
            val value = INet.getService(FibonacciData::class.java).getFibonacciLabel()
            emit(if(value.code() === 200)  "success" else "failure")
            LogUtil.d("sss","emit "+Thread.currentThread().name)
        }
    }



    companion object{
        const val TAG = "FibonacciRepo"
    }
}