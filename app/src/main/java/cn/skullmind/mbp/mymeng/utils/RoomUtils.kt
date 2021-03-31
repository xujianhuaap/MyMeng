package cn.skullmind.mbp.mymeng.utils

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import cn.skullmind.mbp.mymeng.user.RankInfo
import cn.skullmind.mbp.mymeng.user.User
import cn.skullmind.mbp.mymeng.user.UserDao
import kotlinx.coroutines.CoroutineDispatcher

object RoomUtils {
    fun initDataBase(context: CoroutineDispatcher) = Room.databaseBuilder(
        context, AppDataBase::class.java, "database_my_meng"
    ).build()
}

@Database(entities = [User::class,RankInfo::class],version = 1)
abstract class AppDataBase : RoomDatabase() {
    abstract fun createUserDao(): UserDao
}