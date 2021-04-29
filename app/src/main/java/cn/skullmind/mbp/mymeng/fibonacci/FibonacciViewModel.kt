package cn.skullmind.mbp.mymeng.fibonacci

import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class FibonacciViewModel(repo: FibonacciRepo) : ViewModel() {

    // by　lazy 属性委托，在属性第一次被使用的时候被初始化
    var fibonacciInfo = ObservableField<FibonacciInfo>(ObservableField(repo.loadFibonacciBean()))

    fun onChange() {
        val startAngel = fibonacciInfo.get()?.startAngle?.plus(90F) ?: 90F
        fibonacciInfo.set(FibonacciInfo("default ${startAngel % 360}", startAngel))
    }


}

class FibonacciViewModelFactory(val repo: FibonacciRepo) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FibonacciViewModel(repo) as T
    }
}


