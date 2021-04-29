package cn.skullmind.mbp.mymeng.fibonacci

import android.content.Context

class FibonacciRepo(val context: Context) {
    fun loadFibonacciBean(): FibonacciInfo {
        return FibonacciInfo("人耳朵", 90F)
    }
}