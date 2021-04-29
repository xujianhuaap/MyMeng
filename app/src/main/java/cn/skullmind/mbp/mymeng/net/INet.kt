package cn.skullmind.mbp.mymeng.net

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object INet {

    private val retrofit:Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.github.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun <T>getService(clazz:Class<T>):T{
        return retrofit.create(clazz)
    }
}