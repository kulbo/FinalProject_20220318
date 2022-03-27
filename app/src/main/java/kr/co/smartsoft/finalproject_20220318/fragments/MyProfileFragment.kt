package kr.co.smartsoft.finalproject_20220318.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import kr.co.smartsoft.finalproject_20220318.ManageFriendsActivity
import kr.co.smartsoft.finalproject_20220318.ManageMyPlacesActivity
import kr.co.smartsoft.finalproject_20220318.R
import kr.co.smartsoft.finalproject_20220318.SplashActivity
import kr.co.smartsoft.finalproject_20220318.databinding.FragmentMyProfileBinding
import kr.co.smartsoft.finalproject_20220318.datas.BasicResponse
import kr.co.smartsoft.finalproject_20220318.utils.ContextUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyProfileFragment : BaseFragment() {

    lateinit var binding : FragmentMyProfileBinding

    val REQUEST_CODE_GALLERY = 2000

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_profile, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpEvents()
        setValues()
    }
    override fun setUpEvents() {

        binding.imgProfile.setOnClickListener {
            val myIntent = Intent()
            myIntent.action = Intent.ACTION_PICK    // 뭔가 가지려 가는
            myIntent.type = android.provider.MediaStore.Images.Media.CONTENT_TYPE       // 사진을 가지러 간다
            startActivityForResult(myIntent, REQUEST_CODE_GALLERY )
        }

        binding.btnManageFriends.setOnClickListener {
            val myIntent = Intent(mContext, ManageFriendsActivity::class.java)
            startActivity(myIntent)
        }

        binding.btnStartPlaces.setOnClickListener {
            val myIntent = Intent(mContext, ManageMyPlacesActivity::class.java)
            startActivity(myIntent)
        }
        binding.btnLogOut.setOnClickListener {
            val alert = AlertDialog.Builder(mContext)
                .setTitle("로그아웃")
                .setMessage("정말 로그아웃")
                .setPositiveButton("확인",DialogInterface.OnClickListener { dialogInterface, i ->
                    ContextUtil.setLoginUserToken(mContext, "")
                    val myIntent = Intent(mContext, SplashActivity::class.java)
                    myIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(myIntent)
                })
                .setNegativeButton("최소", null)
                .show()
        }
    }

    override fun setValues() {
        apiList.getRequestMyInfo().enqueue(object : Callback<BasicResponse> {
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                if (response.isSuccessful) {
                    val br = response.body()!!
                    
                    binding.txtNickname.text = br.data.user.nick_name.toString()
                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

            }

        })

    }
}