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


        val innerRadius = outRadius * 0.2
        val strokeWidth = outRadius * 0.1

        val innerRadius1 = outRadius * 0.5
        val strokeWidth1 = outRadius * 0.5


        canvas?.also {
//            drawCircle( centerX, centerY, innerRadius,it,resources.getColor(R.color.color_blue_2196F3))
//            drawStrokeCircle( centerX, centerY, innerRadius1,strokeWidth1,it,resources.getColor(R.color.color_green_8FEC8C))

            it.save()
            val path = Path()

            val degree_0 = Math.PI/2
            val x = Math.cos(degree_0)*innerRadius1+centerX
            val y = - Math.sin(degree_0)*innerRadius1 + centerY
            val point_0 = PointF(x.toFloat(), y.toFloat())


            val degree_offset = acos((innerRadius1/outRadius))


            val degree_1 = degree_0 - degree_offset
            val x_1 = Math.cos(degree_1)*outRadius + centerX
            val y_1 = - Math.sin(degree_1)*outRadius +centerY
            val point_1 = PointF(x_1.toFloat(), y_1.toFloat())


            val degree_3 = degree_0 + DegreeUtils.toRadians(240.0)
            val x_3 = Math.cos(degree_3)*innerRadius1+centerX
            val y_3 = -Math.sin(degree_3)*innerRadius1 + centerY
            val point_3 = PointF(x_3.toFloat(), y_3.toFloat())

            val degree_2 = degree_3 - degree_offset
            val x_2 = Math.cos(degree_2)*outRadius+centerX
            val y_2 = -Math.sin(degree_2)*outRadius + centerY
            val point_2 = PointF(x_2.toFloat(), y_2.toFloat())

            val left = centerX - outRadius
            val top = centerY - outRadius
            val right = centerX + outRadius
            val bottom = centerY + outRadius
            val rectF = RectF(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat())
            val startAngle = DegreeUtils.toDegree(Math.PI *2 - degree_0 + degree_offset)
            val sweepAngle = DegreeUtils.toDegree(Math.PI *2 - degree_2)



            val leftForInner = centerX - innerRadius1
            val topForInner = centerY - innerRadius1
            val rightForInner = centerX + innerRadius1
            val bottomForInner = centerY + innerRadius1
            val rectFForInner = RectF(leftForInner.toFloat(), topForInner.toFloat(),
                rightForInner.toFloat(), bottomForInner.toFloat())
            val startAngleForInner = DegreeUtils.toDegree(Math.PI *2 - degree_0)
            val sweepAngleForInner = DegreeUtils.toDegree(Math.PI *2/3)

            path.moveTo(point_0.x,point_0.y)
            path.lineTo(point_1.x,point_1.y)
            path.addArc(rectF, startAngle.toFloat(),sweepAngle.toFloat())
            path.lineTo(point_3.x,point_3.y)
            path.arcTo(rectFForInner, startAngleForInner.toFloat(),sweepAngleForInner.toFloat())
            path.close()

            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 5.0f
            paint.color = resources.getColor(R.color.color_red)
            it.drawPath(path,paint)
            it.restore()

        }

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
        it.drawCircle(centerX.toFloat(), centerY.toFloat(), radius, paint)
        it.restore()
    }
}