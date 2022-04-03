package kr.co.smartsoft.finalproject_20220318.adapters

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import kr.co.smartsoft.finalproject_20220318.ModifyAppointmentActivity
import kr.co.smartsoft.finalproject_20220318.R
import kr.co.smartsoft.finalproject_20220318.ViewMapActivity
import kr.co.smartsoft.finalproject_20220318.api.APIList
import kr.co.smartsoft.finalproject_20220318.api.ServerApi
import kr.co.smartsoft.finalproject_20220318.datas.AppointmentData
import kr.co.smartsoft.finalproject_20220318.datas.BasicResponse
import kr.co.smartsoft.finalproject_20220318.utils.ContextUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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
                popup(imgViewMap, data)
            }
        }
    }

//    약속관리에서 지도 아이콘 클릭하면 팝업메뉴 표시부분
    fun popup(view: View, data : AppointmentData) {
        val popup = PopupMenu(mContext, view)

        popup.inflate(R.menu.popup_menu_for_appointment)
        popup.show()
        popup.setOnMenuItemClickListener ( { item: MenuItem? ->
            when(item!!.itemId) {
                R.id.menu_view -> {
                    val myIntent = Intent(mContext, ViewMapActivity::class.java)
                    myIntent.putExtra("appointment", data)
                    mContext.startActivity(myIntent)
                }
                R.id.menu_edit -> {
                    val myIntent = Intent(mContext, ModifyAppointmentActivity::class.java)
                    mContext.startActivity(myIntent)
                }
                R.id.menu_delete -> {
                    val alert = AlertDialog.Builder(mContext)
                        .setTitle("약속취소")
                        .setMessage("약속을 취소하시겠습니까?")
                        .setPositiveButton("확인", DialogInterface.OnClickListener { dialogInterface, i ->
                            val retrofit = ServerApi.getRerofit(mContext)
                            val apiList = retrofit.create(APIList::class.java)

                            apiList.deleteRequestAppointment(data.id).enqueue(object :
                                Callback<BasicResponse> {
                                override fun onResponse(
                                    call: Call<BasicResponse>,
                                    response: Response<BasicResponse>
                                ) {
                                    if (response.isSuccessful) {
                                        Toast.makeText(mContext, "${data.title.toString()}이 취소되었습니다.", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Toast.makeText(mContext, "${data.id}는 ${response.message()} 입니다.", Toast.LENGTH_SHORT).show()
                                    }
                                }
                                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                                    Toast.makeText(mContext, "${t.message}", Toast.LENGTH_SHORT).show()
                                }
                            })
                        })
                        .setNegativeButton("취소", null)
                        .show()
                }
            }
            true
        } )
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

