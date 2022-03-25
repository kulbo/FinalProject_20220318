package kr.co.smartsoft.finalproject_20220318.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.co.smartsoft.finalproject_20220318.R
import kr.co.smartsoft.finalproject_20220318.datas.AppointmentData
import kr.co.smartsoft.finalproject_20220318.datas.PlaceData
import java.text.SimpleDateFormat

class MyPlacesRecyclerAdapter(
    val mContext: Context,
    val mList : List<PlaceData>
) : RecyclerView.Adapter<MyPlacesRecyclerAdapter.MyViewHolder>() {

    inner class MyViewHolder(view : View) : RecyclerView.ViewHolder(view){

        val txtPlaceName = view.findViewById<TextView>(R.id.txtPlanceName)

        fun bind(data : PlaceData) {
            txtPlaceName.text = data.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.my_place_list_item, parent, false)
        return MyViewHolder(view)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = mList[position]
        holder.bind(data)
    }

    override fun getItemCount() = mList.size


}