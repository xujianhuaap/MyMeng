package cn.skullmind.mbp.mymeng.work_manager

import android.annotation.SuppressLint
import android.content.Context
import androidx.work.*

class ImageOptions {


    fun build(context: Context):WorkContinuation {

        val work = OneTimeWorkRequestBuilder<FilterImageWork>()
            .setInputData(getFilterImageInputData())
            .build()

        return WorkManager.getInstance(context)
            .beginUniqueWork(WORKER_UPLOAD_IMAGES,ExistingWorkPolicy.REPLACE,work)
    }


    @SuppressLint("RestrictedApi")
    fun getFilterImageInputData(): Data {
        return Data.Builder().put(FilterImageWork.INPUT_DATA_KEY_IMAGE_URLS,"").build()
    }

    companion object {
        const val WORKER_UPLOAD_IMAGES = "WORKER_UPLOAD_IMAGES"
    }
}