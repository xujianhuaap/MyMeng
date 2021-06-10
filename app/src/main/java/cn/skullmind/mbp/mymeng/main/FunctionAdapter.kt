package cn.skullmind.mbp.mymeng.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cn.skullmind.mbp.mymeng.R

class FunctionAdapter(private val datas:List<Function>, val callBack:(Function) -> Unit):RecyclerView.Adapter<FunctionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FunctionViewHolder {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.item_function,parent,false)
        return FunctionViewHolder(view,callBack)
    }

    override fun onBindViewHolder(holder: FunctionViewHolder, position: Int) {
        holder.initView(datas[position])
    }

    override fun getItemCount(): Int =  datas.size
}

class FunctionViewHolder(view: View, private val callBack:(Function) -> Unit):RecyclerView.ViewHolder(view){

    fun initView(fuction:Function){
        val labelView = itemView.findViewById<TextView>(R.id.tv_label)
        itemView.tag = fuction
        itemView.setOnClickListener {
          callBack(it.tag as Function)
        }
        labelView.text = fuction.label
    }
}



enum class Function(val id:Int,val label:String){
    UNIVERSE_SKY(1,"宇宙星空"),
    CHROME_LOGO(2,"谷歌Logo绘制"),
    FIBONACCI_CURVE(3,"斐波那契额曲线"),
    VIEW_DRAW(4,"图形绘制与渲染"),
    CAMERA_USE(5,"相机的使用"),
    WORKER_MANAGER_USE(6,"WorkerManager的使用"),
    DB_USE(7,"数据库的使用"),
}