package cn.skullmind.mbp.mymeng.utils

class DegreeUtils {
    companion object{
        fun toDegree(radians:Double):Double{
            return radians * 180 /Math.PI
        }

        fun toRadians(degree:Double):Double{
            return degree * Math.PI / 180
        }
    }
}