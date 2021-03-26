package cn.skullmind.mbp.mymeng.utils

import android.util.Log

object LogUtil {
    fun d(tag: String, msg: String) {
        Log.d(tag,msg)
    }
    fun v(tag: String, msg: String) {
        Log.v(tag,msg)
    }
}