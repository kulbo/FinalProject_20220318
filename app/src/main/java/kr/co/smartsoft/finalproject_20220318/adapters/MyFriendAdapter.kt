package kr.co.smartsoft.finalproject_20220318.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import kr.co.smartsoft.finalproject_20220318.R
import kr.co.smartsoft.finalproject_20220318.datas.UserData

class MyFriendAdapter(
    val mContext:Context,
    resId:Int,
    val mList: List<UserData>
) : ArrayAdapter<UserData>(mContext, resId, mList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var tempRow = convertView
        if (tempRow == null) {
            tempRow = LayoutInflater.from(mContext).inflate(R.layout.my_frient_list_item, null)
        }

        val row = tempRow!!

        val data = mList[position]

        val imgProfile = row.findViewById<ImageView>(R.id.imgProfile)
        val txtNickname = row.findViewById<TextView>(R.id.txtNickName)
        val txtEmail = row.findViewById<TextView>(R.id.txtEmail)
        val imgSocialLoginLogo = row.findViewById<ImageView>(R.id.imgSocialLoginLogo)

        Glide.with(mContext).load(data.profile_img).into(imgProfile)
        txtNickname.text = data.nick_name

        when(data.provider) {
            "default" -> {
                txtEmail.text = data.email
            }
            "kakao" -> {
                txtEmail.text = "카카오로그인"
            }
            "facebook" -> {
                txtEmail.text = "페북 로그인"
            }
            "naver" -> {
                txtEmail.text = "네이버 로그인"
            }
            else -> {

            }
        }
        return row
    }
}