package cn.skullmind.mbp.mymeng.user

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Insert fun insertUsers(vararg users:User)

    @Delete fun deleteUser(user:User)

    @Query("select * from user") fun queryAllUsers():List<User>

    @Query("select * from user where id in (:ids)") fun queryUsersById(ids:List<Int>):List<User>



}