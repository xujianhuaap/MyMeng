package cn.skullmind.mbp.mymeng.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import cn.skullmind.mbp.mymeng.R
import cn.skullmind.mbp.mymeng.utils.DegreeUtils
import kotlin.math.*

class EyeView : View {
    private val padding = 50
    private val paint = Paint()

    private lateinit var camera: Camera
    private lateinit var cameraMatrix:Matrix
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

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        initData()
        canvas?.also {
            cameraMatrix.reset()
            camera.save()
            camera.rotateX(30f)
            camera.getMatrix(cameraMatrix)
            cameraMatrix.preTranslate(-centerX,-centerY)
            cameraMatrix.postTranslate(centerX,centerY)
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

        if(!this::camera.isInitialized){
            camera = Camera()
        }

        if(!this::cameraMatrix.isInitialized){
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
}

