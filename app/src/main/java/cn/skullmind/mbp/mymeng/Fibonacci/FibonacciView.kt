package cn.skullmind.mbp.mymeng.Fibonacci

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class FibonacciView: View {
    var canvas:Canvas? = null
    val  paint:Paint = Paint().also {
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

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val center = Point(400,500)
        if (this.canvas == null && canvas != null){
            this.canvas = canvas
        }
        Fibonacci.draw(80,120,center,this::drawArc)
    }

    fun drawArc(angle:Int,rectF: RectF){

        paint.let { canvas?.drawArc(rectF,angle.toFloat(),90.0f,false, it) }
    }
}