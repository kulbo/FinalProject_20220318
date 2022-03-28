package kr.co.smartsoft.finalproject_20220318.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.co.smartsoft.finalproject_20220318.R
import kr.co.smartsoft.finalproject_20220318.ViewMapActivity
import kr.co.smartsoft.finalproject_20220318.datas.AppointmentData
import java.text.SimpleDateFormat

class AppointmentRecyclerAdapter(
    val mContext: Context,
    val mList : List<AppointmentData>
) : RecyclerView.Adapter<AppointmentRecyclerAdapter.MyViewHolder>() {

    inner class MyViewHolder(view : View) : RecyclerView.ViewHolder(view){

        val txtTitle = view.findViewById<TextView>(R.id.txtTitle)
        val txtPlaceName = view.findViewById<TextView>(R.id.txtPlanceName)
        val txtDateTime = view.findViewById<TextView>(R.id.txtDateTime)
        val imgViewMap = view.findViewById<ImageView>(R.id.imgViewMap)

        fun bind(data : AppointmentData) {
            txtTitle.text = data.title
            txtPlaceName.text = data.place
            Log.d("약속일시", "${data.datetime}")

            val sdf = SimpleDateFormat("yy년 M월 d일 a h시 m분")
            txtDateTime.text = sdf.format(data.datetime)

            imgViewMap.setOnClickListener {
                val myIntent = Intent(mContext, ViewMapActivity::class.java)
                myIntent.putExtra("appointment", data)
                mContext.startActivity(myIntent)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.appointment_list_item, parent, false)
        return MyViewHolder(view)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = mList[position]
        holder.bind(data)
    }

    override fun getItemCount() = mList.size


}