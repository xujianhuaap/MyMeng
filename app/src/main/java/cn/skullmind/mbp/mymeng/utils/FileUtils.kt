package cn.skullmind.mbp.mymeng.utils

import android.content.Context
import android.os.Environment
import android.text.TextUtils
import java.io.File

/***
 * 该目录下的文件存储在外部设备(不是在程序包内),应用删除该目录下的数据也会一并删除,
 * 其他应用可以访问该目录并且修改该目录下的文件,但是对于用户相册中并不可见.
 * @param type 参照Environment
 */
fun getExternalDir(context: Context, dir: String, type: String): File {

    return if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {

        context.getExternalFilesDir(type)?.apply {
            createNotExistedFile(this)
        }?.let {
            if (!TextUtils.isEmpty(dir)) createSubDir(it, dir) else it
        } ?: File(dir)

    } else File(dir)
}

private fun createSubDir(rootDir: File, dir: String) =
    File(rootDir, dir).apply {
        createNotExistedFile(this)
    }

private fun createNotExistedFile(rootDir: File) {
    if (!rootDir.exists()) rootDir.mkdir()
}