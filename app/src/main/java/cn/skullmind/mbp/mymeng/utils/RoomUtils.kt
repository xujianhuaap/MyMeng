package cn.skullmind.mbp.mymeng.utils

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import cn.skullmind.mbp.mymeng.user.RankInfo
import cn.skullmind.mbp.mymeng.user.User
import cn.skullmind.mbp.mymeng.user.IUserDao

object RoomUtils {
    private  var appDataBase: AppDataBase? = null

    fun initDataBase(context: Context): AppDataBase {

        if (appDataBase == null) {
            LogUtil.v(RoomUtils::class.java.simpleName, "init app data base")
            appDataBase =
                Room.databaseBuilder(context, AppDataBase::class.java, "database_my_meng").build()
        }
        return appDataBase!!
    }
}

@Database(entities = [User::class, RankInfo::class], version = 1)
abstract class AppDataBase : RoomDatabase() {
    abstract fun createUserDao(): IUserDao
}