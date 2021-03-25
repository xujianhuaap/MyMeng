package cn.skullmind.mbp.mymeng.widget

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatSeekBar
import cn.skullmind.mbp.mymeng.R

class VerticalSeekBar : AppCompatSeekBar {
    constructor(context: Context, attrs: AttributeSet?, defStyleL: Int):super(
        context, attrs, defStyleL){
        thumb = context.resources.getDrawable(R.drawable.ic_thumb)
    }
    constructor(context: Context, attrs: AttributeSet?) : this(
        context, attrs,
        R.style.Widget_AppCompat_SeekBar
    )

    constructor(context: Context) : this(context, null)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(heightMeasureSpec, widthMeasureSpec)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(h, w, oldh, oldw)
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.apply {
            this.rotate(-90f)
            this.translate(-height.toFloat(), 0f)
        }
        super.onDraw(canvas)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (!isEnabled) return false
        when (event?.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE, MotionEvent.ACTION_UP -> {
                progress = max - (max * event.y / height).toInt()
                onSizeChanged(width, height, 0, 0)
            }
        }
        return true
    }
}