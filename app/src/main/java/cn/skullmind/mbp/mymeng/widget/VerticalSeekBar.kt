package cn.skullmind.mbp.mymeng.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.widget.ProgressBar
import cn.skullmind.mbp.mymeng.R
import cn.skullmind.mbp.mymeng.utils.LogUtil
import kotlin.math.abs
import kotlin.math.roundToInt

class VerticalSeekBar : ProgressBar {
    private val scaledTouchSlop = ViewConfiguration.get(context).scaledTouchSlop

    private val thumb: Drawable? = context.getDrawable(R.drawable.ic_thumb)
    private val thumbHeight = thumb?.intrinsicHeight ?: 0
    private val thumbRect = Rect()
    private val thumbOffset = (thumb?.intrinsicWidth ?: 0) / 2
    private var touchThumbOffset = 0.0f
        set(value) {
            if (value >= 0) field = value
        }
    private var availableHeight: Int = 0
    private val attributeValues: VerticalSeekBarAttrs

    var seekBarChangeListener: OnSeekBarChangeListener? = null
    private var isDragging = false
    private var touchDownY = 0f


    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(
        context, attrs,
        R.style.Widget_AppCompat_SeekBar
    )

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context, attrs, defStyle
    ) {
        attributeValues = initAttr(context, attrs)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        refreshAvailableHeight()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        refreshAvailableHeight()
    }

    private fun refreshAvailableHeight() {
        availableHeight = height - paddingBottom - paddingTop - thumbHeight
    }

    private fun initAttr(context: Context, attrs: AttributeSet?): VerticalSeekBarAttrs {
        val attrsArray = context.obtainStyledAttributes(attrs, R.styleable.VerticalSeekBar)
        val drawablePadding = attrsArray.getDimensionPixelSize(
            R.styleable.VerticalSeekBar_progressBarPadding,
            resources.getDimensionPixelSize(R.dimen.dimen_10)
        )

        val min = attrsArray.getInteger(R.styleable.VerticalSeekBar_min, 0)

        attrsArray.recycle()
        return VerticalSeekBarAttrs(drawablePadding, min)
    }


    override fun onDraw(canvas: Canvas?) {
        canvas?.also {
            drawTrack(it)
            drawThumb(it)
        }
    }

    private fun drawSuperTrack(canvas: Canvas) {
        progressDrawable?.also {
            val saveCount = canvas.save()
            canvas.translate(paddingLeft.toFloat(), paddingTop.toFloat())
            val progressBarPadding = attributeValues.progressBarPadding
            val height = height - paddingBottom - paddingTop
            progressDrawable.setBounds(progressBarPadding,
                0, width - progressBarPadding, height
            )
            progressDrawable.draw(canvas)
            canvas.restoreToCount(saveCount)
        }
    }

    private fun drawTrack(canvas: Canvas) {
        thumb?.also {
            val offSet = (width - thumb.intrinsicWidth) / 2
            thumbRect.left = offSet
            thumbRect.top = height - thumbHeight - touchThumbOffset.toInt()
            thumbRect.right = thumb.intrinsicWidth + offSet
            thumbRect.bottom = height - touchThumbOffset.toInt() - paddingBottom
        }
        drawSuperTrack(canvas)
    }

    private fun drawThumb(canvas: Canvas) {
        if (thumb != null) {
            val saveCount = canvas.save()
            // Translate the padding. For the x, we need to allow the thumb to
            // draw in its extra space
            thumb.bounds = thumbRect
            thumb.draw(canvas)
            canvas.restoreToCount(saveCount)
        }
    }


    @SuppressLint("ClickableViewAccessibility")
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
                if (abs(y - touchDownY) > scaledTouchSlop) {
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

    private  fun trackTouchEvent(event: MotionEvent) {
        val x = event.x.roundToInt()
        val y = event.y.roundToInt()
        if (!thumbRect.contains(x, y)) return

        val scale = when {
            y > this.height - paddingBottom - thumbOffset  -> {
                0.0f
            }
            y < paddingTop + thumbOffset -> {
                1.0f
            }
            else -> {
                ((availableHeight - (y - paddingTop - thumbOffset)) / availableHeight.toFloat())
            }
        }
        val range = max - attributeValues.min
        this.touchThumbOffset = scale * availableHeight
        LogUtil.v(VerticalSeekBar::class.java.simpleName,"scale: $scale, touchThumbOffset: ${this.touchThumbOffset}")
        val progress = scale * range + attributeValues.min
        setHotspot(x.toFloat(), y.toFloat())
        setProgress(progress.roundToInt())
    }

    /**
     * This is called when the user has started touching this widget.
     */
    private fun onStartTrackingTouch() {
        isDragging = true
        seekBarChangeListener?.onStartTrackingTouch(this)
    }

    /**
     * This is called when the user either releases his touch or the touch is
     * canceled.
     */
    private fun onStopTrackingTouch() {
        isDragging = false
        invalidate()
        seekBarChangeListener?.onStopTrackingTouch(this)
    }

    private fun setHotspot(x: Float, y: Float) {
        val bg = background
        bg?.setHotspot(x, y)
    }


    private fun startDrag(event: MotionEvent) {
        isPressed = true
        thumb?.also { invalidate() }
        onStartTrackingTouch()
        trackTouchEvent(event)
        attemptClaimDrag()
    }

    private fun attemptClaimDrag() {
        parent?.also { parent.requestDisallowInterceptTouchEvent(true) }
    }

    private fun isInScrollingContainer(): Boolean {
        return false
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
     * to disable advancing the seek bar.
     * @param seekBar The SeekBar in which the touch gesture began
     */
    fun onStartTrackingTouch(seekBar: VerticalSeekBar?)

    /**
     * Notification that the user has finished a touch gesture. Clients may want to use this
     * to re-enable advancing the vertical seek bar.
     * @param seekBar The SeekBar in which the touch gesture began
     */
    fun onStopTrackingTouch(seekBar: VerticalSeekBar?)
}

data class VerticalSeekBarAttrs(val progressBarPadding: Int, val min: Int)