package cn.skullmind.mbp.mymeng.fibonacci

import androidx.databinding.ObservableField
import androidx.lifecycle.*
import cn.skullmind.mbp.mymeng.fibonacci.beans.FibonacciInfo

class FibonacciViewModel(private val repo: FibonacciRepo) : ViewModel() {

    // by　lazy 属性委托，在属性第一次被使用的时候被初始化
    var fibonacciInfo = ObservableField<FibonacciInfo>(ObservableField(repo.loadFibonacciBean()))

    var label = ObservableField<String>()

    fun onChange() {
        val startAngel = fibonacciInfo.get()?.startAngle?.plus(90F) ?: 90F
        fibonacciInfo.set(FibonacciInfo("default ${startAngel % 360}", startAngel))
    }

    fun getLabelData():LiveData<String>{
        return repo.loadFibonacciLabel()
    }



}

class FibonacciViewModelFactory(private val repo: FibonacciRepo) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return FibonacciViewModel(repo) as T
    }
}


