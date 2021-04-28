package cn.skullmind.mbp.mymeng.fibonacci

import android.graphics.Point
import android.graphics.RectF

object Fibonacci {
    fun draw(initP1:Int = 0, initP2:Int = 1, initAngle:Float = 0.0f,
             initCenter:Point,
             drawArc: (angle:Float,rectF:RectF) -> Unit){

        var center = initCenter
        var a = initP1
        var b = initP2
        var angleStart  = initAngle
        var radius = a

        while (a < 800){
            if(canDrawArc(radius)){
                drawArc(angleStart,getArcRectF(radius,center))
            }

            val temp  = a
            a = b
            b += temp

            if(canDrawArc(radius)){
                angleStart += 90
                center = getNextCenter(center,angleStart,a - radius)
            }
            radius = a
        }


    }

    private fun canDrawArc(radius: Int) = radius > 0


    private fun getArcRectF(radius:Int, center:Point):RectF{
        val top = (center.y - radius).toFloat()
        val right = (center.x + radius).toFloat()
        val bottom = (center.y + radius).toFloat()
        val left = (center.x - radius).toFloat()
        return RectF(left,top,right,bottom)
    }

    private fun getNextCenter(currentCenter:Point, startAngle:Float, value: Int):Point{
        return when(startAngle % 360){
            0F -> Point(currentCenter.x - value,currentCenter.y)
            90F -> Point(currentCenter.x,currentCenter.y -value)
            180F -> Point(currentCenter.x+value,currentCenter.y)
            270F -> Point(currentCenter.x,currentCenter.y+value)
            else -> Point()
        }

    }
}