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
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class SelectImageWork(context: Context, workerParameters: WorkerParameters) :
    CoroutineWorker(context, workerParameters) {

    @SuppressLint("RestrictedApi")
    override suspend fun doWork(): Result {
        val imageId = inputData.getInt(INPUT_DATA_KEY_IMAGE_URLS, 0)
        writeBitmapToFile(applicationContext, getBitmap(imageId))?.also {
            val data = Data.Builder().put(OUTPUT_DATA_KEY_IMAGE_URL, it.toString()).build()
            LogUtil.d(SelectImageWork::class.java.simpleName, "filter image work success upload")
            return Result.success(data)
        }
       return Result.failure()
    }

    private fun getBitmap(imageId: Int): Bitmap? {
        val options = BitmapFactory.Options().apply {
            this.inJustDecodeBounds = true
        }
        BitmapFactory.decodeResource(applicationContext.resources, imageId, options)
        options.inJustDecodeBounds = false
        val bitmap = BitmapFactory.decodeResource(applicationContext.resources, imageId, options)
        return bitmap
    }

    /**
     * Writes a given [Bitmap] to the [Context.getFilesDir] directory.
     *
     * @param applicationContext the application [Context].
     * @param bitmap             the [Bitmap] which needs to be written to the files
     * directory.
     * @return a [Uri] to the output [Bitmap].
     */
    private fun writeBitmapToFile(
        applicationContext: Context,
        bitmap: Bitmap?
    ): Uri? {
        if(bitmap == null) return null
        // Bitmaps are being written to a temporary directory. This is so they can serve as inputs
        // for workers downstream, via Worker chaining.
        val name = String.format("filter-output-%s.png", UUID.randomUUID().toString())
        val outputDir = File(applicationContext.filesDir, OUT_PATH)
        if (!outputDir.exists()) {
            outputDir.mkdirs() // should succeed
        }
        outputDir.listFiles()?.forEach {
            it?.delete()
        }
        val outputFile = File(outputDir, name)
        var out: FileOutputStream? = null
        try {
            out = FileOutputStream(outputFile)
            bitmap.compress(Bitmap.CompressFormat.PNG, 0 /* ignored for PNG */, out)
        } catch (e: FileNotFoundException){
            return null
        }finally {
            if (out != null) {
                try {
                    out.close()
                } catch (ignore: IOException) {
                }

            }
        }
        return Uri.fromFile(outputFile)
    }


    companion object {
        const val INPUT_DATA_KEY_IMAGE_URLS = "data_input_key_image_urls"

        const val OUTPUT_DATA_KEY_IMAGE_URL = "data_output_key_image_url"

        const val OUT_PATH = "images"
    }
}