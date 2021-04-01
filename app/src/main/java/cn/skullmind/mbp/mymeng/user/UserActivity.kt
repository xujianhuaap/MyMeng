package cn.skullmind.mbp.mymeng.user

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import cn.skullmind.mbp.mymeng.R
import cn.skullmind.mbp.mymeng.utils.LogUtil
import cn.skullmind.mbp.mymeng.utils.RoomUtils
import kotlinx.coroutines.*

fun startUserActivity(context: AppCompatActivity) {
    val intent = Intent(context, UserActivity::class.java)
    context.startActivity(intent)
}


class UserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        findViewById<View>(R.id.tv_query).setOnClickListener {
            MainScope().launch {
                withContext(Dispatchers.IO) {
                        RoomUtils.initDataBase(applicationContext).createUserDao().queryAllUsers()
                }.forEach {
                    LogUtil.d(UserActivity::class.java.simpleName,"${it.id}")
                }

            }
        }

        findViewById<View>(R.id.tv_add).setOnClickListener {
            MainScope().launch() {
                withContext(Dispatchers.IO) {
                    val user = User("xu", 32, true, Rank.Senior.labelValue)
                    UserDao.insertUsers(applicationContext, user)
                    LogUtil.v(
                        UserActivity::class.java.simpleName,
                        "with context ${Thread.currentThread().name}"
                    )
                }
                LogUtil.v(
                    UserActivity::class.java.simpleName,
                    "launch ${Thread.currentThread().name}"
                )
            }

            LogUtil.v(UserActivity::class.java.simpleName, "click ${Thread.currentThread().name}")

        }


        findViewById<View>(R.id.tv_delete).setOnClickListener {
            GlobalScope.launch {
                UserDao.deleteUser(applicationContext, "ss")
            }

        }
    }
}