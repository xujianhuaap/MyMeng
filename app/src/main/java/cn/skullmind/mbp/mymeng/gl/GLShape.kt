package cn.skullmind.mbp.mymeng.gl

import android.content.res.Resources
import cn.skullmind.mbp.mymeng.gl.third.shape.SixPointStar

interface GLShape{
    fun draw()
}

object Shape{
    fun getSixPointStar(outRadius: Float, innerRadius: Float, zAxisValue: Float,
                        resource: Resources):SixPointStar =
        SixPointStar(outRadius, innerRadius,zAxisValue,resource)
}