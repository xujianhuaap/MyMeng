package cn.skullmind.mbp.tools

import android.opengl.Matrix

object MatrixState {
    private val mProjMatrix = FloatArray(16)
    private val mVMatrix = FloatArray(16) //设置相机产生的矩阵
    //m 最初模型 v 相机设置 p 正交变换 与 透视变换
    private lateinit var mMVPMatrix: FloatArray //最终结果

    fun setCamera(
        cx: Float,
        cy: Float,
        cz: Float,
        tx: Float,
        ty: Float,
        tz: Float,
        upx: Float,
        upy: Float,
        upz: Float
    ) {
        Matrix.setLookAtM(
            mVMatrix,
            0,
            cx, cy, cz,
            tx, ty, tz,
            upx, upy, upz
        )
    }

    //正交转换
    fun setProjectOrtho(
        left: Float,
        right: Float,
        bottom: Float,
        top: Float,
        near: Float,
        far: Float
    ) {
        Matrix.orthoM(
            mProjMatrix,
            0,
            left, right,
            bottom, top,
            near, far
        )
    }

    //透视变化
    fun setProjectfrustumM(
        left: Float,
        right: Float,
        bottom: Float,
        top: Float,
        near: Float,
        far: Float
    ) {
        Matrix.frustumM(
            mProjMatrix,
            0,
            left, right,
            bottom, top,
            near, far
        )
    }



    /***
     * spec 是基本的变换例如旋转 缩放 移动
     */
    fun getFinalMatrix(spec: FloatArray?): FloatArray
    {
        mMVPMatrix = FloatArray(16)
        Matrix.multiplyMM(mMVPMatrix, 0, mVMatrix, 0, spec, 0)// 设置相机
        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mMVPMatrix, 0)
        return mMVPMatrix
    }
}