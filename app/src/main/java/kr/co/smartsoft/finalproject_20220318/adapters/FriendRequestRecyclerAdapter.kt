package kr.co.smartsoft.finalproject_20220318.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kr.co.smartsoft.finalproject_20220318.R
import kr.co.smartsoft.finalproject_20220318.api.APIList
import kr.co.smartsoft.finalproject_20220318.api.ServerApi
import kr.co.smartsoft.finalproject_20220318.datas.AppointmentData
import kr.co.smartsoft.finalproject_20220318.datas.BasicResponse
import kr.co.smartsoft.finalproject_20220318.datas.UserData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FriendRequestRecyclerAdapter(
    val mContext: Context,
    val mList : List<UserData>
) : RecyclerView.Adapter<FriendRequestRecyclerAdapter.MyViewHolder>() {

    inner class MyViewHolder(view : View) : RecyclerView.ViewHolder(view) {

        val imgProfile = view.findViewById<ImageView>(R.id.imgProfile)
        val txtNickname = view.findViewById<TextView>(R.id.txtNickname)
        val txtEmail = view.findViewById<TextView>(R.id.txtEmail)
        val btnAccept = view.findViewById<Button>(R.id.btnAccept)
        val btnDeny = view.findViewById<Button>(R.id.btnDeny)
        val imgSocialLoginLogo = view.findViewById<ImageView>(R.id.imgSocialLoginLogo)

        fun bind(data: UserData) {
            Glide.with(mContext).load(data.profile_img).into(imgProfile)
            txtNickname.text = data.nick_name

            when (data.provider) {
                "default" -> {
                    txtEmail.text = data.email
                    imgSocialLoginLogo.visibility = View.GONE
                }
                "kakao" -> {
//                "카카오로그인"
                    txtEmail.text = "카카오로그인"
                    imgSocialLoginLogo.visibility = View.VISIBLE
                    Glide.with(mContext).load(R.drawable.kakao).into(imgSocialLoginLogo)
                }
                "facebook" -> {
                    txtEmail.text = "페북 로그인"
                    imgSocialLoginLogo.visibility = View.VISIBLE
                    Glide.with(mContext).load(R.drawable.facebook).into(imgSocialLoginLogo)
                }
                "naver" -> {
                    txtEmail.text = "네이버 로그인"
                    imgSocialLoginLogo.visibility = View.VISIBLE
                    Glide.with(mContext).load(R.drawable.naver).into(imgSocialLoginLogo)
                }
                else -> {
//                그 외의 잘못된 경우.
                }
            }

//            친구 요청 수락/거절 로직 구현 (함수로 분리하는 것 고려)
            val ocl = View.OnClickListener {
                val tagStr = it.tag.toString()

                Log.d("친구 요청 결과 : 파람", tagStr)

                val retrofit = ServerApi.getRerofit(mContext)
                val apiList = retrofit.create(APIList::class.java)

                apiList.putRequestFriendAcceptDeny(
                    data.id,
                    tagStr
                ).enqueue(object : Callback<BasicResponse>{
                    override fun onResponse(
                        call: Call<BasicResponse>,
                        response: Response<BasicResponse>
                    ) {
                        Toast.makeText(mContext, "${data.nick_name}님의 친구요청이 ${data.nick_name} 되었습니다.", Toast.LENGTH_SHORT).show()
                    }

                    override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                    }

                })
            }
            btnAccept.setOnClickListener(ocl)
            btnDeny.setOnClickListener(ocl)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.requested_friend_list_item, parent, false)
        return MyViewHolder(view)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = mList[position]
        holder.bind(data)
    }

    override fun getItemCount() = mList.size
}