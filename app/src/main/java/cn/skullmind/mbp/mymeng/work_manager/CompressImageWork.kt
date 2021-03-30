package cn.skullmind.mbp.mymeng.work_manager

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import cn.skullmind.mbp.mymeng.utils.LogUtil
import java.io.File
import java.io.FileOutputStream

class CompressImageWork(context: Context, parameters: WorkerParameters) :
    CoroutineWorker(context, parameters) {


    @SuppressLint("RestrictedApi")
    override suspend fun doWork(): Result {
        val inputUri = inputData.getString(SelectImageWork.OUTPUT_DATA_KEY_IMAGE_URL)
        val inputStream = applicationContext.contentResolver.openInputStream(Uri.parse(inputUri))
        val sourceBitmap = BitmapFactory.decodeStream(inputStream)

        val file = File(applicationContext.filesDir,"images_compress")
        if(!file.exists())file.createNewFile()
        val outputStream = FileOutputStream(file)
        sourceBitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)

        val data = Data.Builder().put(DATA_OUTPUT_KEY_COMPRESS_URI, Uri.fromFile(file).toString()).build()
        LogUtil.v(CompressImageWork::class.java.simpleName, "compress success")
        return Result.Success(data)
    }

    companion object {
        const val DATA_OUTPUT_KEY_COMPRESS_URI = "data_output_key_compress_uri"
    }
}