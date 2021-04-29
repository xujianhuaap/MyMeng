package cn.skullmind.mbp.mymeng.fibonacci.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import cn.skullmind.mbp.mymeng.R
import cn.skullmind.mbp.mymeng.fibonacci.Fibonacci

class FibonacciView : View {
    // private can not use,or can not use in layout by model
    var startAngle: Float = 0.0f
        set(value) {
            field = value
            postInvalidate()
        }

    private lateinit var canvas: Canvas
    private val paint: Paint = Paint().also {
        it.color = Color.GREEN
        it.strokeWidth = 4.0f
        it.style = Paint.Style.STROKE
        it.isAntiAlias = true
    }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        context?.let { initAttr(it, attrs) }
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        context?.let { initAttr(it, attrs) }
    }


    private fun initAttr(context: Context, attrs: AttributeSet?) {
        val attrsArray = context.obtainStyledAttributes(attrs, R.styleable.FibonacciView)
        val lineWidth = attrsArray.getDimension(
            R.styleable.FibonacciView_lineWidth,
            resources.getDimension(R.dimen.dimen_1dp))
        paint.strokeWidth = lineWidth
        startAngle = attrsArray.getFloat(R.styleable.FibonacciView_startAngle, 0F)

        attrsArray.recycle()
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val center = getViewCenter()
        canvas?.also {
            this.canvas = canvas
            Fibonacci.draw(20,40,initAngle = startAngle, initCenter = center, drawArc = this::drawArc)

        }
    }

    private fun getViewCenter(): Point {
        return Point(width / 2, height / 2)
    }

    private fun drawArc(angle: Float, rectF: RectF) {

        paint.let { canvas.drawArc(rectF, angle, 90.0f, false, it) }
    }
}