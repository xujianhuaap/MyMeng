package cn.skullmind.mbp.mymeng.work_manager

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import cn.skullmind.mbp.mymeng.utils.LogUtil

class FilterImageWork(context: Context, workerParameters: WorkerParameters) :
    CoroutineWorker(context, workerParameters) {

    @SuppressLint("RestrictedApi")
    override suspend fun doWork(): Result {
        val imageId = inputData.getInt(INPUT_DATA_KEY_IMAGE_URLS, 0)
        val options = BitmapFactory.Options()
        val bitmap = BitmapFactory.decodeResource(applicationContext.resources, imageId, options)
        val data = Data.Builder().put(OUTPUT_DATA_KEY_IMAGE, bitmap).build()
        LogUtil.d(FilterImageWork::class.java.simpleName,"filter image work success upload")
        return Result.success(data)
    }

    companion object {
        const val INPUT_DATA_KEY_IMAGE_URLS = "data_input_key_image_urls"

        const val OUTPUT_DATA_KEY_IMAGE = "data_output_key_image"
    }
}