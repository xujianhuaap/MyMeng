package cn.skullmind.mbp.mymeng.user

import android.content.Context
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import cn.skullmind.mbp.mymeng.utils.RoomUtils

@Dao
interface IUserDao {
    @Insert
    fun insertUsers(vararg users: User)

    @Delete(entity = User::class)
    fun deleteUser(user: Identify)

    @Query("select * from user")
    fun queryAllUsers(): List<User>

    @Query("select * from user where id in (:ids)")
    fun queryUsersById(vararg ids: String): List<User>

}

object UserDao {
    fun insertUsers(context: Context, vararg users: User) {
        RoomUtils.initDataBase(context).createUserDao().insertUsers(*users)
    }

    fun deleteUser(context: Context, id: String) {
        RoomUtils.initDataBase(context).createUserDao().deleteUser(Identify(id))
    }

    fun queryAllUsers(context: Context): List<User> =
        RoomUtils.initDataBase(context).createUserDao().queryAllUsers()


    fun queryUsersById(context: Context, vararg ids: String): List<User> =
        RoomUtils.initDataBase(context).createUserDao().queryUsersById(*ids)
}