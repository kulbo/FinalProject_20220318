package kr.co.smartsoft.finalproject_20220318.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kr.co.smartsoft.finalproject_20220318.R
import kr.co.smartsoft.finalproject_20220318.datas.AppointmentData
import kr.co.smartsoft.finalproject_20220318.datas.UserData
import java.text.SimpleDateFormat

class MyFriendsListRecyclerAdapter(
    val mContext: Context,
    val mList : List<UserData>
) : RecyclerView.Adapter<MyFriendsListRecyclerAdapter.MyViewHolder>() {

    inner class MyViewHolder(view : View) : RecyclerView.ViewHolder(view){

        val imgProfile = view.findViewById<ImageView>(R.id.imgProfile)
        val txtNickname = view.findViewById<TextView>(R.id.txtNickname)
        val imgSocialLoginLogo = view.findViewById<ImageView>(R.id.imgSocialLoginLogo)
        val txtEmail = view.findViewById<TextView>(R.id.txtEmail)

        fun bind(data : UserData) {
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
                    imgProfile.setImageResource(R.drawable.kakao)
                    imgSocialLoginLogo.visibility = View.VISIBLE
                    Glide.with(mContext).load(R.drawable.kakao).into(imgSocialLoginLogo)
                }
                "facebook" -> {
                    txtEmail.text = "페북 로그인"
                    imgProfile.setImageResource(R.drawable.facebook)
                    imgSocialLoginLogo.visibility = View.VISIBLE
                    Glide.with(mContext).load(R.drawable.facebook).into(imgSocialLoginLogo)
                }
                "naver" -> {
                    txtEmail.text  = "네이버 로그인"
                    imgSocialLoginLogo.visibility = View.VISIBLE
                    imgProfile.setImageResource(R.drawable.facebook)
                    Glide.with(mContext).load(R.drawable.naver).into(imgSocialLoginLogo)
                }
                else -> {
//                그 외의 잘못된 경우.
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.my_frient_list_item, parent, false)
        return MyViewHolder(view)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = mList[position]
        holder.bind(data)
    }

    override fun getItemCount() = mList.size
}