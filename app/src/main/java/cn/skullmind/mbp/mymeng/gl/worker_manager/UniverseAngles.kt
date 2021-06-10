package cn.skullmind.mbp.mymeng.gl.worker_manager

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters

data class UniverseAngles(var earthRotateAngle: Float, var starSkyRotateAngle: Float)

class UniverseAngleWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {
   private var starSkyRotateAngle = 0f
    private var earthRotateAngle = 0f


    override suspend fun doWork(): Result {
        earthRotateAngle = inputData.getFloat(INPUT_UNIVERSE_ANGLE_EARTH,0f)
        starSkyRotateAngle = inputData.getFloat(INPUT_UNIVERSE_ANGLE_SKY,0f)
        refreshAngles()
        val data = Data.Builder().putFloat(OUTPUT_UNIVERSE_ANGLE_EARTH,earthRotateAngle)
            .putFloat(OUTPUT_UNIVERSE_ANGLE_SKY,starSkyRotateAngle)
            .build()

        return Result.success(data)
    }

    private fun refreshAngles() {
        this.earthRotateAngle = (this.earthRotateAngle + 2) % 360
        this.starSkyRotateAngle = (this.starSkyRotateAngle + 0.2f) % 360
    }

    companion object{
        const val INPUT_UNIVERSE_ANGLE_EARTH = "input_universe_angle_earth"
        const val INPUT_UNIVERSE_ANGLE_SKY = "input_universe_angle_sky"

        const val OUTPUT_UNIVERSE_ANGLE_EARTH = "output_universe_angle_earth"
        const val OUTPUT_UNIVERSE_ANGLE_SKY = "output_universe_angle_sky"
    }
}



