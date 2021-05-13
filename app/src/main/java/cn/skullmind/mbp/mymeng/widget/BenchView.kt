package cn.skullmind.mbp.mymeng.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import cn.skullmind.mbp.mymeng.R

class BenchView : View {
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

        val outRadius = (Math.min(width, height) - padding * 2) / 2
        val centerX:Float = (outRadius + padding).toFloat()
        val centerY:Float = (outRadius + padding).toFloat()


        val innerRadius = outRadius * 0.2F
        val strokeWidth = outRadius * 0.1F

        val innerRadius1 = outRadius * 0.5F
        val strokeWidth1 = outRadius * 0.5F


        canvas?.also {
//            drawCircle( centerX, centerY, innerRadius,it,resources.getColor(R.color.color_blue_2196F3))
//            drawStrokeCircle( centerX, centerY, innerRadius1,strokeWidth1,it,resources.getColor(R.color.color_green_8FEC8C))

            it.save()
            val path = Path()

            val degree_0 = Math.PI/2
            val x = Math.cos(degree_0)*innerRadius1+centerX
            val y = - Math.sin(degree_0)*innerRadius1 + centerY
            val point_0 = PointF(x.toFloat(), y.toFloat())


            val degree_offset = Math.acos((innerRadius1/outRadius).toDouble())


            val degree_1 = degree_0 - degree_offset
            val x_1 = Math.cos(degree_1)*outRadius + centerX
            val y_1 = - Math.sin(degree_1)*outRadius +centerY
            val point_1 = PointF(x_1.toFloat(), y_1.toFloat())


            val degree_3 = degree_0 + 120*Math.PI/180
            val x_3 = Math.cos(degree_3)*innerRadius1+centerX
            val y_3 = -Math.sin(degree_3)*innerRadius1 + centerY
            val point_3 = PointF(x_3.toFloat(), y_3.toFloat())

            val degree_2 = degree_3 + degree_offset
            val x_2 = Math.cos(degree_2)*outRadius+centerX
            val y_2 = -Math.sin(degree_2)*outRadius + centerY
            val point_2 = PointF(x_2.toFloat(), y_2.toFloat())

            path.moveTo(centerX,centerY)
            path.lineTo(point_0.x,point_0.y)
            path.lineTo(point_1.x,point_1.y)


//            path.lineTo(point_2.x,point_2.y)
//            path.lineTo(point_3.x,point_3.y)
            path.close()


            paint.style = Paint.Style.FILL_AND_STROKE
            paint.strokeWidth = 5.0f
            paint.color = resources.getColor(R.color.color_red)
            it.drawPath(path,paint)
            it.restore()


            it.save()
            val path1 = Path()

            val left = centerX - outRadius
            val top = centerY - outRadius
            val right = centerX + outRadius
            val bottom = centerY + outRadius
            val rectF = RectF(left,top,right,bottom)
            val startAngle = (Math.PI *2 - degree_0 + degree_offset)*180+13

            path1.addArc(rectF, startAngle.toFloat(),180.0f)
            paint.style = Paint.Style.FILL_AND_STROKE
            paint.strokeWidth = 5.0f
            paint.color = resources.getColor(R.color.color_green_0ADA03)
            it.drawPath(path1,paint)
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