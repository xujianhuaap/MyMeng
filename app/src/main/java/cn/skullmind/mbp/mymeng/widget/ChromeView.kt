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

class ChromeView : View {
    private val padding = 5
    private val startRadiant = Math.random()*Math.PI*2
    private val paint = Paint()

    private lateinit var firstPartRing: PartRing
    private lateinit var secondPartRing: PartRing
    private lateinit var thirdPartRing: PartRing
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
            drawCircle(
                innerRadius.toFloat(),
                it,
                getColor(R.color.color_blue_2196F3)
            )
            drawStrokeCircle(
                middleRadius.toFloat(), it, getColor(R.color.color_green_8FEC8C)
            )

            drawRing(it)
        }

    }

    private fun drawRing(canvas: Canvas) {
        firstPartRing.draw(canvas, paint)
        this.secondPartRing.draw(canvas, paint)
        this.thirdPartRing.draw(canvas, paint)
    }

    private fun initRingData(startRadiant: Double) {
        val secondRadiant = startRadiant + Math.PI * 2 / 3
        val thirdRadiant = secondRadiant + Math.PI * 2 / 3
        if (!this::firstPartRing.isInitialized) {
            firstPartRing = getPositivePartRing(startRadiant)
        }


        if (!this::secondPartRing.isInitialized) {
            this.secondPartRing = getNegativePartRing(secondRadiant, R.color.color_orange_FF5F20)
        }


        if (!this::thirdPartRing.isInitialized) {
            this.thirdPartRing = getNegativePartRing(thirdRadiant, R.color.color_green_337934)
        }
    }

    private fun getPositivePartRing(startRadiant: Double) =
        getPartRing(startRadiant, R.color.color_yellow_FFC922, true)

    private fun getNegativePartRing(startRadiant: Double, @ColorRes colorId: Int) =
        getPartRing(startRadiant, colorId, false)

    private fun getPartRing(startRadiant: Double, @ColorRes colorId: Int, positive: Boolean) =
        PartRing.getInstance(
            middleRadius,
            centerX,
            centerY,
            outRadius,
            startRadiant,
            getColor(colorId)
        )

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
        initRingData(startRadiant)
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
        paint.style = Paint.Style.STROKE
        it.drawCircle(centerX, centerY, radius, paint)
        it.restore()
    }
}


class PartRing(
    private val innerRadius: Double,
    private val centerX: Float,
    private val centerY: Float,
    private val outRadius: Double,
    private val startRadiant: Double,
    @ColorInt val color: Int
) {

    private fun getFirstPoint(): PointExt {
        val degree = startRadiant
        val x = cos(degree) * innerRadius + centerX
        val y = -sin(degree) * innerRadius + centerY
        return PointExt(x.toFloat(), y.toFloat(), degree)
    }

    private fun getSecondPoint(): PointExt {
        val degree = getFirstPoint().getRadiant() - getOffsetRadiant()
        val x = cos(degree) * outRadius + centerX
        val y = -sin(degree) * outRadius + centerY
        return PointExt(x.toFloat(), y.toFloat(), degree)
    }

    private fun getOffsetRadiant() = acos((innerRadius / outRadius))

    private fun getThirdPoint(): PointExt {
        val degree = getFourthPoint().getRadiant() - getOffsetRadiant()
        val x = cos(degree) * outRadius + centerX
        val y = -sin(degree) * outRadius + centerY
        return PointExt(x.toFloat(), y.toFloat(), degree)
    }

    private fun getFourthPoint(): PointExt {
        val toRadians = DegreeUtils.toRadians(240.0)
        val degree = getFirstPoint().getRadiant() + toRadians
        val x = cos(degree) * innerRadius + centerX
        val y = -sin(degree) * innerRadius + centerY
        return PointExt(x.toFloat(), y.toFloat(), degree)
    }

    private fun getOutRect(): RectF {
        val left = centerX - outRadius
        val top = centerY - outRadius
        val right = centerX + outRadius
        val bottom = centerY + outRadius
        return RectF(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat())
    }


    private fun getOutArcStartAngle(): Double {
        return DegreeUtils.toDegree( - getSecondPoint().getRadiant())
    }

    private fun getOutArcSweepAngle(): Double {
        val thirdPointRadiant = getThirdPoint().getRadiant()
        val secondPointRadiant = getSecondPoint().getRadiant()
        val radians = abs(thirdPointRadiant - secondPointRadiant)
        return DegreeUtils.toDegree(min(radians,Math.PI*2 - radians))
    }

    private fun getInnerRect(): RectF {
        val left = centerX - innerRadius
        val top = centerY - innerRadius
        val right = centerX + innerRadius
        val bottom = centerY + innerRadius
        return RectF(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat())
    }

    private fun getInnerArcStartAngle(): Double =
        DegreeUtils.toDegree(- getFirstPoint().getRadiant())

    private fun getInnerArcSweepAngle(): Double = DegreeUtils.toDegree(Math.PI * 2 / 3)

    fun draw(canvas: Canvas, paint: Paint): Double {
        canvas.save()
        val path = Path()
        path.moveTo(getFirstPoint().x, getFirstPoint().y)
        path.addArc(
            getInnerRect(),
            getInnerArcStartAngle().toFloat(),
            getInnerArcSweepAngle().toFloat()
        )
        path.lineTo(getSecondPoint().x, getSecondPoint().y)
        path.close()

        path.moveTo(getSecondPoint().x, getSecondPoint().y)
        path.addArc(getOutRect(), getOutArcStartAngle().toFloat(), getOutArcSweepAngle().toFloat())
        path.close()

        paint.style = Paint.Style.FILL
        paint.strokeWidth = 5.0f
        paint.color = color
        canvas.drawPath(path, paint)
        canvas.restore()
        return getFourthPoint().getRadiant()
    }

    companion object {
        fun getInstance(
            innerRadius: Double,
            centerX: Float,
            centerY: Float,
            outRadius: Double,
            startRadiant: Double,
            @ColorInt color: Int
        ): PartRing {
            val startRadiant1 = startRadiant % (Math.PI * 2)
            return PartRing(innerRadius, centerX, centerY, outRadius, startRadiant1, color)
        }
    }
}

/***
 *@param radiant 是Radiant 单位 而非 degree
 */
class PointExt(x: Float, y: Float, private val radiant: Double) : PointF(x, y){
    fun getRadiant():Double{
        return radiant % (Math.PI*2)
    }
}