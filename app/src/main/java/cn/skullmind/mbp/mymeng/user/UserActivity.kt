package cn.skullmind.mbp.mymeng.user

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import cn.skullmind.mbp.mymeng.R
import cn.skullmind.mbp.mymeng.utils.LogUtil
import cn.skullmind.mbp.mymeng.utils.RoomUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun startUserActivity(context: AppCompatActivity) {
    val intent = Intent(context, UserActivity::class.java)
    context.startActivity(intent)
}


class UserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        findViewById<View>(R.id.tv_query).setOnClickListener {
            GlobalScope.launch {
                withContext(Dispatchers.IO){
                    RoomUtils.initDataBase(applicationContext).createUserDao().queryAllUsers().forEach {
                        LogUtil.d(UserActivity::class.java.simpleName, "user name: ${it.id} ${it.name}")
                    }
                }
            }
        }

        findViewById<View>(R.id.tv_add).setOnClickListener {
            GlobalScope.launch {
                val user = User("xu", 32, true, Rank.Senior.labelValue)
                UserDao.insertUsers(applicationContext, user)
            }

        }


        findViewById<View>(R.id.tv_delete).setOnClickListener {
            GlobalScope.launch {
                UserDao.deleteUser(applicationContext,"ss")
            }

        }
    }
}