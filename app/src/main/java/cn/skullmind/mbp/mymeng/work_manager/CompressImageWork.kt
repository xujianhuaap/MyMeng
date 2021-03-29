package cn.skullmind.mbp.mymeng.work_manager

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import cn.skullmind.mbp.mymeng.utils.LogUtil
import java.io.ByteArrayOutputStream

class CompressImageWork(context: Context, parameters: WorkerParameters) :
    CoroutineWorker(context, parameters) {


    @SuppressLint("RestrictedApi")
    override suspend fun doWork(): Result {
        val sourceBitmap =
            inputData.keyValueMap[FilterImageWork.OUTPUT_DATA_KEY_IMAGE] as Bitmap
        val outputStream = ByteArrayOutputStream(1024)
        sourceBitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
        val bytes = outputStream.toByteArray()
        val targetBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        val data = Data.Builder().put(DATA_OUTPUT_KEY_COMPRESS, targetBitmap).build()
        LogUtil.v(CompressImageWork::class.java.simpleName, "compress success")
        return Result.Success(data)
    }

    companion object {
        const val DATA_OUTPUT_KEY_COMPRESS = "data_output_key_compress"
    }
}