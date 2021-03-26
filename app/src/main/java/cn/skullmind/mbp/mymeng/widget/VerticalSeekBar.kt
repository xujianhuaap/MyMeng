package cn.skullmind.mbp.mymeng.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.Region
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.widget.ProgressBar
import cn.skullmind.mbp.mymeng.R

class VerticalSeekBar : ProgressBar {
    private var thumbRect = Rect()
    private var mOnSeekBarChangeListener: OnSeekBarChangeListener? = null
    private var thumb: Drawable = context.resources.getDrawable(R.drawable.ic_thumb)
    private var scaledTouchSlop = 0
    private var touchThumbOffset = 0.0f
    private var thumbOffset = 0
    private var isDragging = false
    private var touchDownY = 0f
    private var fakeMin = 0;


    constructor(context: Context, attrs: AttributeSet?, defStyleL: Int) : super(
        context, attrs, defStyleL
    ) {
        scaledTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
        thumbOffset = thumb.intrinsicWidth / 2
    }

    constructor(context: Context, attrs: AttributeSet?) : this(
        context, attrs,
        R.style.Widget_AppCompat_SeekBar
    )

    constructor(context: Context) : this(context, null)


    override fun onDraw(canvas: Canvas?) {
        canvas?.also {
            drawTrack(it)
            drawThumb(it)
        }
    }

    fun drawSuperTrack(canvas: Canvas) {
        val d: Drawable = progressDrawable
        if (d != null) {
            // Translate canvas so a indeterminate circular progress bar with padding
            // rotates properly in its animation
            val saveCount = canvas.save()
            canvas.translate(paddingLeft.toFloat(), paddingTop.toFloat())
            d.setBounds(20,0,width-20,height)
            d.draw(canvas)
            canvas.restoreToCount(saveCount)

        }
    }

    fun drawTrack(canvas: Canvas) {
        val offSet = (width - thumb.intrinsicWidth)/2
        thumbRect.left = offSet
        thumbRect.top  = height - thumb.intrinsicHeight-touchThumbOffset.toInt()+thumbOffset
        thumbRect.right = thumb.intrinsicWidth + offSet
        thumbRect.bottom = height-touchThumbOffset.toInt()+thumbOffset

        drawSuperTrack(canvas)
    }

    fun drawThumb(canvas: Canvas) {
        if (thumb != null) {
            val saveCount = canvas.save()
            // Translate the padding. For the x, we need to allow the thumb to
            // draw in its extra space
            canvas.translate(0f, 0f)
            thumb.bounds = thumbRect
            thumb.draw(canvas)
            canvas.restoreToCount(saveCount)
        }
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isEnabled) {
            return false
        }
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (isInScrollingContainer()) {
                    touchDownY = event.y
                } else {
                    startDrag(event)
                }
            }
            MotionEvent.ACTION_MOVE -> if (isDragging) {
                trackTouchEvent(event)
            } else {
                val y = event.y
                if (Math.abs(y - touchDownY) > scaledTouchSlop) {
                    startDrag(event)
                }
            }
            MotionEvent.ACTION_UP -> {
                if (isDragging) {
                    trackTouchEvent(event)
                    onStopTrackingTouch()
                    isPressed = false
                } else {
                    // Touch up when we never crossed the touch slop threshold should
                    // be interpreted as a tap-seek to that location.
                    onStartTrackingTouch()
                    trackTouchEvent(event)
                    onStopTrackingTouch()
                }
                // ProgressBar doesn't know to repaint the thumb drawable
                // in its inactive state when the touch stops (because the
                // value has not apparently changed)
                invalidate()
            }
            MotionEvent.ACTION_CANCEL -> {
                if (isDragging) {
                    onStopTrackingTouch()
                    isPressed = false
                }
                invalidate() // see above explanation
            }
        }
        return true
    }

    private fun trackTouchEvent(event: MotionEvent) {
        val x = Math.round(event.x)
        val y = Math.round(event.y)
        val height = height
        val availableHeight: Int = height - paddingBottom - paddingTop
        val scale: Float
        var progress = 0.0f

        if (y > height - paddingBottom) {
            scale = 0.0f
        } else if (y < paddingTop) {
            scale = 1.0f
        } else {
            scale = ((availableHeight - y + paddingTop) / availableHeight.toFloat())
        }

        val range = max - fakeMin

        touchThumbOffset = scale * height
        progress = scale * range + fakeMin
        setHotspot(x.toFloat(), y.toFloat())
        setProgress(Math.round(progress))
    }

    /**
     * This is called when the user has started touching this widget.
     */
    fun onStartTrackingTouch() {
        isDragging = true
        mOnSeekBarChangeListener?.onStartTrackingTouch(this)
    }

    /**
     * This is called when the user either releases his touch or the touch is
     * canceled.
     */
    fun onStopTrackingTouch() {
        isDragging = false
        mOnSeekBarChangeListener?.onStopTrackingTouch(this)
    }

    private fun setHotspot(x: Float, y: Float) {
        val bg = background
        bg?.setHotspot(x, y)
    }


    private fun startDrag(event: MotionEvent) {
        isPressed = true
        if (thumb != null) {
            // This may be within the padding region.
            invalidate(thumb.getBounds())
        }
        onStartTrackingTouch()
        trackTouchEvent(event)
        attemptClaimDrag()
    }

    private fun attemptClaimDrag() {
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(true)
        }
    }

    private fun isInScrollingContainer(): Boolean {
        return false
    }

    fun setOnSeekBarChangeListener(l: OnSeekBarChangeListener) {
        mOnSeekBarChangeListener = l
    }


}

interface OnSeekBarChangeListener {
    /**
     * Notification that the progress level has changed. Clients can use the fromUser parameter
     * to distinguish user-initiated changes from those that occurred programmatically.
     *
     * @param seekBar The SeekBar whose progress has changed
     * @param progress The current progress level. This will be in the range min..max where min
     * and max were set by [ProgressBar.setMin] and
     * [ProgressBar.setMax], respectively. (The default values for
     * min is 0 and max is 100.)
     * @param fromUser True if the progress change was initiated by the user.
     */
    fun onProgressChanged(seekBar: VerticalSeekBar?, progress: Int, fromUser: Boolean)

    /**
     * Notification that the user has started a touch gesture. Clients may want to use this
     * to disable advancing the seekbar.
     * @param seekBar The SeekBar in which the touch gesture began
     */
    fun onStartTrackingTouch(seekBar: VerticalSeekBar?)

    /**
     * Notification that the user has finished a touch gesture. Clients may want to use this
     * to re-enable advancing the seekbar.
     * @param seekBar The SeekBar in which the touch gesture began
     */
    fun onStopTrackingTouch(seekBar: VerticalSeekBar?)
}