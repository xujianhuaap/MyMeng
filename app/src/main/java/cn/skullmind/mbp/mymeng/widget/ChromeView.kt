package cn.skullmind.mbp.mymeng.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import cn.skullmind.mbp.mymeng.R
import cn.skullmind.mbp.mymeng.utils.DegreeUtils
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class ChromeView : View {
    private val padding = 5
    private val paint = Paint()
    private lateinit var firstPartRing: PartRing
    private lateinit var secondPartRing: PartRing
    private lateinit var thirdPartRing: PartRing
    private  var outRadius: Double = 0.0
    private  var centerX: Float = 0.0f
    private  var centerY: Float = 0.0f
    private var innerRadius  = 0.0
    private var innerRadius1  = 0.0

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
                centerX,
                centerY,
                innerRadius.toFloat(),
                it,
                getColor(R.color.color_blue_2196F3)
            )
            drawStrokeCircle(
                centerX, centerY, innerRadius1.toFloat(),it, getColor(R.color.color_green_8FEC8C)
            )

            if(!this::firstPartRing.isInitialized){
                firstPartRing = PartRing(
                    innerRadius1,
                    centerX,
                    centerY,
                    outRadius,
                    Math.PI / 2,
                    getColor(R.color.color_yellow_FFC922),
                    true
                )
            }
            var start = firstPartRing.draw(canvas,paint)

            if(!this::secondPartRing.isInitialized){
                secondPartRing = PartRing(
                    innerRadius1,
                    centerX,
                    centerY,
                    outRadius,
                    start,
                    getColor(R.color.color_green_337934),
                    false
                )
            }
            start = secondPartRing.draw(canvas,paint)
            if(!this::thirdPartRing.isInitialized){
                thirdPartRing =  PartRing(
                    innerRadius1,
                    centerX,
                    centerY,
                    outRadius,
                    start,
                    getColor(R.color.color_orange_FF5F20),
                    false
                )
            }
           thirdPartRing.draw(canvas,paint)

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

        if (innerRadius == 0.0) {
            innerRadius = outRadius * 0.4
        }

        if (innerRadius1 == 0.0) {
            innerRadius1 = 0.9 * outRadius - innerRadius
        }
    }

    @Suppress("DEPRECATION")
    private fun getColor(@ColorRes resId:Int) = resources.getColor(resId)

    private fun drawCircle(
        centerX: Float,
        centerY: Float,
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
        centerX: Float,
        centerY: Float,
        radius: Float,
        it: Canvas,
        @ColorInt color: Int
    ) {
        it.save()
        paint.color = color
        paint.isAntiAlias = false
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
    @ColorInt val color: Int,
    private val active: Boolean
) {

    private fun getFirstPoint(): PointExt {
        val degree = startRadiant
        val x = cos(degree) * innerRadius + centerX
        val y = -sin(degree) * innerRadius + centerY
        return PointExt(x.toFloat(), y.toFloat(), degree)
    }

    private fun getSecondPoint(): PointExt {
        val degree = getFirstPoint().radiant - getOffsetRadiant()
        val x = cos(degree) * outRadius + centerX
        val y = -sin(degree) * outRadius + centerY
        return PointExt(x.toFloat(), y.toFloat(), degree)
    }

    private fun getOffsetRadiant() = acos((innerRadius / outRadius))

    private fun getThirdPoint(): PointExt {
        val degree = getFourthPoint().radiant - getOffsetRadiant()
        val x = cos(degree) * outRadius + centerX
        val y = -sin(degree) * outRadius + centerY
        return PointExt(x.toFloat(), y.toFloat(), degree)
    }

    private fun getFourthPoint(): PointExt {
        val toRadians = if (active) DegreeUtils.toRadians(240.0) else DegreeUtils.toRadians(-120.0)
        val degree = getFirstPoint().radiant + toRadians
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
        return DegreeUtils.toDegree(Math.PI * 2 - getSecondPoint().radiant)
    }

    private fun getOutArcSweepAngle(): Double {
        val thirdPointRadiant = getThirdPoint().radiant
        val secondPointRadiant = getSecondPoint().radiant
        return if (active) DegreeUtils.toDegree(Math.PI * 2 - thirdPointRadiant + secondPointRadiant) else DegreeUtils.toDegree(
            secondPointRadiant - thirdPointRadiant
        )
    }

    private fun getInnerRect(): RectF {
        val left = centerX - innerRadius
        val top = centerY - innerRadius
        val right = centerX + innerRadius
        val bottom = centerY + innerRadius
        return RectF(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat())
    }

    private fun getInnerArcStartAngle(): Double =
        DegreeUtils.toDegree(Math.PI * 2 - getFirstPoint().radiant)

    private fun getInnerArcSweepAngle(): Double = DegreeUtils.toDegree(Math.PI * 2 / 3)

    fun draw(canvas: Canvas,paint:Paint): Double {
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
        return getFourthPoint().radiant
    }

}

/***
 *@param radiant 是Radiant 单位 而非 degree
 */
class PointExt(x: Float, y: Float, val radiant: Double) : PointF(x, y)