package cn.skullmind.mbp.mymeng.fibonacci;

import cn.skullmind.mbp.mymeng.fibonacci.widget.Ｄ
import retrofit2.Response
import retrofit2.http.GET

interface FibonacciData {
    @GET("/")
    suspend fun getFibonacciLabel(): Response<Ｄ>
}
