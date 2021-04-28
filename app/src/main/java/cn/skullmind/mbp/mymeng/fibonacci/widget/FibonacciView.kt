package cn.skullmind.mbp.mymeng.fibonacci.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import cn.skullmind.mbp.mymeng.fibonacci.Fibonacci

class FibonacciView : View {
    private var canvas: Canvas? = null
    private val paint: Paint = Paint().also {
        it.color = Color.GREEN
        it.strokeWidth = 4.0f
        it.style = Paint.Style.STROKE
        it.isAntiAlias = true
    }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val center = getViewCenter()
        if (this.canvas == null && canvas != null) {
            this.canvas = canvas
        }
        Fibonacci.draw(initAngle = 180,initCenter = center, drawArc = this::drawArc)
    }

    private fun getViewCenter(): Point {
        return Point(width / 2, height / 2)
    }

    private fun drawArc(angle: Int, rectF: RectF) {

        paint.let { canvas?.drawArc(rectF, angle.toFloat(), 90.0f, false, it) }
    }
}