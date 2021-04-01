package cn.skullmind.mbp.mymeng.user

import androidx.room.*
import java.util.*

@Entity
data class User(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "_name") val name: String,
    @ColumnInfo(name = "_age") val age: Short,
    @ColumnInfo(name = "_gender") val gender: Boolean,
    @ForeignKey(entity = RankInfo::class,parentColumns = ["id"],childColumns = ["_rank"],
        onDelete = ForeignKey.SET_DEFAULT,onUpdate = ForeignKey.CASCADE)
    @ColumnInfo(name = "_rank")  var rank: Int = Rank.Default.labelValue
){

    @Ignore
    constructor(
         name: String,
         age: Short,
         gender: Boolean,
        rank: Int = Rank.Default.labelValue
    ):this(UUID.randomUUID().toString(),name,age,gender,rank)
}

data class Identify(val id: String)

@Entity
data class RankInfo(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "_salary")val salary:Int,
    @ColumnInfo(name = "_vacation") val vacation:Short
)


enum class Rank(val label: String, val labelValue: Int) {
    Default("默认", 0),
    Junior("初级", 1),
    Medium("中级", 2),
    Senior("高级", 3)
}