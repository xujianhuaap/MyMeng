package cn.skullmind.mbp.mymeng.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import cn.skullmind.mbp.mymeng.R
import cn.skullmind.mbp.mymeng.utils.DegreeUtils
import kotlin.math.acos

class ChromeView : View {
    val padding  = 5
    val paint = Paint()
    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val width = width
        val height = height

        val outRadius:Double = ((Math.min(width, height) - padding * 2) / 2).toDouble()
        val centerX:Float = (outRadius + padding).toFloat()
        val centerY:Float = (outRadius + padding).toFloat()


        val innerRadius = outRadius * 0.4

        val innerRadius1 = 0.9*outRadius - innerRadius


        canvas?.also {
            drawCircle( centerX, centerY, innerRadius.toFloat(),it,resources.getColor(R.color.color_blue_2196F3))
            drawStrokeCircle( centerX, centerY, innerRadius1.toFloat(),
                0.0f,it,resources.getColor(R.color.color_green_8FEC8C))

            var start = drawOutRing(it, innerRadius1, centerX, centerY, outRadius,Math.PI / 2, resources.getColor(R.color.color_yellow_FFC922),true)

            start = drawOutRing(it, innerRadius1, centerX, centerY, outRadius,start,resources.getColor(R.color.color_green_337934),false)
            drawOutRing(it, innerRadius1, centerX, centerY, outRadius,start,resources.getColor(R.color.color_orange_FF5F20),false)

        }

    }

    private fun drawOutRing(
        it: Canvas,
        innerRadius1: Double,
        centerX: Float,
        centerY: Float,
        outRadius: Double,
        startRadiant:Double,
        @ColorInt color: Int,
        active: Boolean
    ):Double {
        it.save()
        val path = Path()

        val degree_0 = startRadiant
        val x = Math.cos(degree_0) * innerRadius1 + centerX
        val y = -Math.sin(degree_0) * innerRadius1 + centerY
        val point_0 = PointF(x.toFloat(), y.toFloat())


        val degree_offset = acos((innerRadius1 / outRadius))


        val degree_1 = degree_0 - degree_offset
        val x_1 = Math.cos(degree_1) * outRadius + centerX
        val y_1 = -Math.sin(degree_1) * outRadius + centerY
        val point_1 = PointF(x_1.toFloat(), y_1.toFloat())


        val toRadians = if(active) DegreeUtils.toRadians(240.0) else DegreeUtils.toRadians(-120.0)
        val degree_3 = degree_0 + toRadians
        val x_3 = Math.cos(degree_3) * innerRadius1 + centerX
        val y_3 = -Math.sin(degree_3) * innerRadius1 + centerY
        val point_3 = PointF(x_3.toFloat(), y_3.toFloat())

        val degree_2 = degree_3 - degree_offset
        val x_2 = Math.cos(degree_2) * outRadius + centerX
        val y_2 = -Math.sin(degree_2) * outRadius + centerY
        val point_2 = PointF(x_2.toFloat(), y_2.toFloat())

        val left = centerX - outRadius
        val top = centerY - outRadius
        val right = centerX + outRadius
        val bottom = centerY + outRadius
        val rectF = RectF(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat())
        val startAngle = DegreeUtils.toDegree(Math.PI * 2 - degree_1)
        val sweepAngle = if(active)DegreeUtils.toDegree(Math.PI * 2 - degree_2 + degree_1) else DegreeUtils.toDegree(degree_1 - degree_2)


        val leftForInner = centerX - innerRadius1
        val topForInner = centerY - innerRadius1
        val rightForInner = centerX + innerRadius1
        val bottomForInner = centerY + innerRadius1
        val rectFForInner = RectF(
            leftForInner.toFloat(), topForInner.toFloat(),
            rightForInner.toFloat(), bottomForInner.toFloat()
        )
        val startAngleForInner = DegreeUtils.toDegree(Math.PI * 2 - degree_0)
        val sweepAngleForInner = DegreeUtils.toDegree(Math.PI * 2 / 3)

        path.moveTo(point_0.x, point_0.y)
        path.addArc(rectFForInner, startAngleForInner.toFloat(), sweepAngleForInner.toFloat())
        path.lineTo(point_1.x, point_1.y)
        path.close()

        path.moveTo(point_1.x, point_1.y)
        path.addArc(rectF, startAngle.toFloat(), sweepAngle.toFloat())
//        path.lineTo(point_3.x, point_3.y)
        path.close()

        paint.style = Paint.Style.FILL
        paint.strokeWidth = 5.0f
        paint.color = color
        it.drawPath(path, paint)
        it.restore()
        return degree_3
    }

    private fun drawCircle(
        centerX: Float,
        centerY: Float,
        radius:Float,
        it: Canvas,
        @ColorInt color:Int
    ) {
        it.save()
        paint.color = color
        paint.isAntiAlias = false
        paint.style = Paint.Style.FILL
        it.drawCircle(centerX.toFloat(), centerY.toFloat(), radius.toFloat(), paint)
        it.restore()
    }

    private fun drawStrokeCircle(
        centerX: Float,
        centerY: Float,
        radius:Float,
        strokeWidth:Float,
        it: Canvas,
        @ColorInt color:Int
    ) {
        it.save()
        paint.color = color
        paint.isAntiAlias = false
        paint.strokeWidth = strokeWidth
        paint.style = Paint.Style.STROKE
        it.drawCircle(centerX, centerY, radius, paint)
        it.restore()
    }
}