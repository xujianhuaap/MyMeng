package cn.skullmind.mbp.mymeng.widget

import android.content.Context
import android.graphics.Camera
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.hardware.SensorEvent
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import cn.skullmind.mbp.mymeng.R
import cn.skullmind.mbp.mymeng.utils.DegreeUtils
import kotlin.math.abs
import kotlin.math.min

class EyeView : View {
    private val padding = 50
    private val paint = Paint()

    private lateinit var camera: Camera
    private lateinit var cameraMatrix: Matrix
    private var rotateX: Float = 0.0F
    private var rotateY: Float = 0.0F
    private var rotateZ: Float = 0.0F
    private var outRadius: Double = 0.0
    private var centerX: Float = 0.0f
    private var centerY: Float = 0.0f
    private var innerRadius = 0.0
    private var middleRadius = 0.0

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    fun refreshRotate(rotateX: Float, rotateY: Float, rotateZ: Float) {
        this.rotateX = rotateX
        this.rotateY = rotateY
        this.rotateZ = rotateZ
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        initData()
        canvas?.also {
            cameraMatrix.reset()
            camera.save()
            camera.rotateX(rotateX)
            camera.rotateY(rotateY)
            camera.rotateZ(rotateZ)
            camera.getMatrix(cameraMatrix)
            cameraMatrix.preTranslate(-centerX, -centerY)
            cameraMatrix.postTranslate(centerX, centerY)
            camera.restore()
            it.concat(cameraMatrix)

            drawCircle(
                innerRadius.toFloat(),
                it,
                getColor(R.color.color_blue_2196F3)
            )
            drawStrokeCircle(
                middleRadius.toFloat(), it, getColor(R.color.color_green_8FEC8C)
            )

        }

    }


    private fun initData() {

        if (outRadius == 0.0) {
            outRadius = ((min(this.width, this.height) - padding * 2) / 2).toDouble()
        }

        if (centerX == 0.0f) {
            centerX = (outRadius + padding).toFloat()
        }
        if (centerY == 0.0f) {
            centerY = (outRadius + padding).toFloat()
        }





        if (middleRadius == 0.0) {
            middleRadius = 0.5 * outRadius
        }
        if (innerRadius == 0.0) {
            innerRadius = middleRadius * 0.75
        }

        if (!this::camera.isInitialized) {
            camera = Camera()
        }

        if (!this::cameraMatrix.isInitialized) {
            cameraMatrix = Matrix()
        }
    }

    @Suppress("DEPRECATION")
    private fun getColor(@ColorRes resId: Int) = resources.getColor(resId)

    private fun drawCircle(
        radius: Float,
        it: Canvas,
        @ColorInt color: Int
    ) {
        it.save()

        paint.color = color
        paint.isAntiAlias = false
        paint.style = Paint.Style.FILL
        it.drawCircle(centerX, centerY, radius, paint)
        it.restore()
    }

    private fun drawStrokeCircle(
        radius: Float,
        it: Canvas,
        @ColorInt color: Int
    ) {
        it.save()
        paint.color = color
        paint.isAntiAlias = true
        paint.strokeWidth = 150f
        paint.style = Paint.Style.STROKE
        it.drawCircle(centerX, centerY, radius, paint)
        it.restore()
    }

    /***
     * 陀螺仪转换器
     * 通常，陀螺仪的输出会随时间积分，以计算描述角度随时间步长变化的旋转
     */
    class GyroScopeConverter() {
        private var gx = 0f
        private var gy = 0f
        private var gz = 0f
        private var lastTimeStamp: Long = 0
        private val NS2S = 1.0f / 1000000000.0f//纳秒转化成秒
        private val angle = FloatArray(3)

        fun convert(sensorEvent: SensorEvent, callBack: (Float, Float, Float) -> Unit) {

            gx = convertX(sensorEvent)
            gy = convertY(sensorEvent)
            gz = convertZ(sensorEvent)
            lastTimeStamp = sensorEvent.timestamp
            callBack(gx, gy, gz)
        }
        private fun convertX(sensorEvent: SensorEvent):Float{
            if(lastTimeStamp == 0L) return gx

            val dT: Float = (sensorEvent.timestamp - lastTimeStamp) * NS2S
            angle[0] += sensorEvent.values[0] * dT
            val angle = DegreeUtils.toDegree(angle[0].toDouble()).toFloat()

            if(gx == 0f) return angle

            if (abs(gx - angle) >= 0.5) {
                return angle
            }

            return gx
        }

        private fun convertY(sensorEvent: SensorEvent):Float{
            if(lastTimeStamp == 0L) return gy

            val dT: Float = (sensorEvent.timestamp - lastTimeStamp) * NS2S
            angle[1] += sensorEvent.values[1] * dT
            val angle = DegreeUtils.toDegree(angle[1].toDouble()).toFloat()

            if(gy == 0f) return angle

            if (abs(gy - angle) >= 0.5) {
                return angle
            }

            return gy
        }

        private fun convertZ(sensorEvent: SensorEvent):Float{
            if(lastTimeStamp == 0L) return gz

            val dT: Float = (sensorEvent.timestamp - lastTimeStamp) * NS2S
            angle[1] += sensorEvent.values[1] * dT

            return DegreeUtils.toDegree(this.angle[1].toDouble()).toFloat()
        }
    }


}

