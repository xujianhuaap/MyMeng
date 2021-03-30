package cn.skullmind.mbp.mymeng.work_manager.works

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import cn.skullmind.mbp.mymeng.utils.LogUtil
import java.io.*

class CompressImageWork(context: Context, parameters: WorkerParameters) :
    CoroutineWorker(context, parameters) {


    @SuppressLint("RestrictedApi")
    override suspend fun doWork(): Result {
        val inputUri = inputData.getString(SelectImageWork.OUTPUT_DATA_KEY_IMAGE_URL)
        val inputStream:InputStream? = openInputStream(inputUri)
        val file = getOutputFile()
        val outputStream:FileOutputStream? = openOutputStream(file)

        BitmapFactory.decodeStream(inputStream)?.let {
            it.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
            val data = Data.Builder().put(DATA_OUTPUT_KEY_COMPRESS_URI, Uri.fromFile(file).toString()).build()
            LogUtil.v(CompressImageWork::class.java.simpleName, "compress success")
            return Result.Success(data)
        }

        return Result.failure()
    }

    private fun openOutputStream(file:File): FileOutputStream? {
        return try {
            FileOutputStream(file)
        }catch (e: FileNotFoundException){
            null
        }
    }

    private fun getOutputFile(): File {
        val file = File(applicationContext.filesDir, "images_compress")
        try {
            if (!file.exists()) file.createNewFile()
        }catch (ignore: IOException){

        }
        return file
    }

    private fun openInputStream(inputUri: String?):InputStream? {
        return try {
            applicationContext.contentResolver.openInputStream(Uri.parse(inputUri))
        }catch (e: FileNotFoundException){
            null
        }
    }


    companion object {
        const val DATA_OUTPUT_KEY_COMPRESS_URI = "data_output_key_compress_uri"
    }
}