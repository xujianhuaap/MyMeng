package cn.skullmind.mbp.mymeng.gl.third.shape

import kotlin.math.cos
import kotlin.math.sin

class SixPointStar(outRadius:Float,innerRadius:Float,zAxisValue:Float) {

    val points= ArrayList<Float>()


    init {
        initPoints(outRadius,innerRadius,zAxisValue)
    }

    fun draw(){
    }

    private fun initPoints(R:Float,r:Float,z:Float) {
        val tempAngle = 60
        var angle = 0
        while (angle < 360) {

            points.add(0f)
            points.add(0f)
            points.add(z)

            points.add((R * UNIT_SIZE * cos(Math.toRadians(angle.toDouble()))).toFloat())
            points.add((R * UNIT_SIZE * sin(Math.toRadians(angle.toDouble()))).toFloat())
            points.add(z)

            points.add((r * UNIT_SIZE * cos(Math.toRadians((angle + tempAngle / 2).toDouble()))).toFloat())
            points.add((r * UNIT_SIZE * sin(Math.toRadians((angle + tempAngle / 2).toDouble()))).toFloat())
            points.add(z)


            points.add(0f)
            points.add(0f)
            points.add(z)

            points.add((r * UNIT_SIZE * cos(Math.toRadians((angle + tempAngle / 2).toDouble()))).toFloat())
            points.add((r * UNIT_SIZE * sin(Math.toRadians((angle + tempAngle / 2).toDouble()))).toFloat())
            points.add(z)

            points.add((R * UNIT_SIZE * cos(Math.toRadians((angle + tempAngle).toDouble()))).toFloat())
            points.add((R * UNIT_SIZE * sin(Math.toRadians((angle + tempAngle).toDouble()))).toFloat())
            points.add(z)
            angle += tempAngle
        }
    }

    companion object{
        const val UNIT_SIZE = 1F
    }

}