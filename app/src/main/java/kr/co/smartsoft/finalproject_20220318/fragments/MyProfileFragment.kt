package kr.co.smartsoft.finalproject_20220318.fragments

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import kr.co.smartsoft.finalproject_20220318.ManageFriendsActivity
import kr.co.smartsoft.finalproject_20220318.ManageMyPlacesActivity
import kr.co.smartsoft.finalproject_20220318.R
import kr.co.smartsoft.finalproject_20220318.SplashActivity
import kr.co.smartsoft.finalproject_20220318.databinding.FragmentMyProfileBinding
import kr.co.smartsoft.finalproject_20220318.datas.BasicResponse
import kr.co.smartsoft.finalproject_20220318.utils.ContextUtil
import kr.co.smartsoft.finalproject_20220318.utils.URIPathHelper
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

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
            val pl = object : PermissionListener{
                override fun onPermissionGranted() {
                    val myIntent = Intent()
                    myIntent.action = Intent.ACTION_PICK    // ?????? ????????? ?????? ?????????
                    myIntent.type = android.provider.MediaStore.Images.Media.CONTENT_TYPE       // ????????? ????????? ??????
                    startActivityForResult(myIntent, REQUEST_CODE_GALLERY )
                }

                override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                    Toast.makeText(mContext, "????????? ?????? ????????? ????????????.", Toast.LENGTH_SHORT).show()
                }

            }

            TedPermission.create()
                .setPermissionListener(pl)
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                .check()
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
                .setTitle("????????????")
                .setMessage("?????? ????????????")
                .setPositiveButton("??????",DialogInterface.OnClickListener { dialogInterface, i ->
                    ContextUtil.setLoginUserToken(mContext, "")
                    val myIntent = Intent(mContext, SplashActivity::class.java)
                    myIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(myIntent)
                })
                .setNegativeButton("??????", null)
                .show()
        }
    }

    override fun setValues() {
        apiList.getRequestMyInfo().enqueue(object : Callback<BasicResponse> {
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                if (response.isSuccessful) {
                    val br = response.body()!!
                    
                    binding.txtNickname.text = br.data.user.nick_name.toString()
                    Glide.with(mContext).load(br.data.user.profile_img).into(binding.imgProfile)
                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

            }

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_GALLERY) {
            if (resultCode == Activity.RESULT_OK) {
//                ????????? ????????? ?????? ????????? ????????? ??????
                val selectedImageUri = data?.data!! // ????????? ????????? ????????? ??????
//                    Uri -> ?????? ?????? ????????? ?????? ????????? ??????
                val file = File(URIPathHelper().getPath(mContext, selectedImageUri))
//                ????????? ????????? Retrofit??? ?????? ????????? RequestBody ????????? ??????.
                val fileReqBody = RequestBody.create(MediaType.get("image/*"), file)
//                  ?????????????????? ??????????????? ?????? Multipart ????????? ??????
//                cf) ????????? ?????? ???????????? API ????????? Multipart ????????? ?????? ???????????? ???????????????.
                val multiPartBody = MultipartBody.Part.createFormData("profile_image", "myProfile.jpg", fileReqBody)
//                ????????? multiPartBody  ?????? ???
                apiList.putRequestProfileImg(
                    multiPartBody
                ).enqueue(object :Callback<BasicResponse>{
                    override fun onResponse(
                        call: Call<BasicResponse>,
                        response: Response<BasicResponse>
                    ) {
                        if (response.isSuccessful) {
                            Toast.makeText(mContext, "????????? ????????? ?????????", Toast.LENGTH_SHORT).show()
                            Glide.with(mContext).load(selectedImageUri).into(binding.imgProfile)
                        }
                        else {
                            val jsonObj = JSONObject(response.errorBody()!!.string())
                                Log.d("?????? ??????", jsonObj.toString())
                        }
                    }

                    override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                        Log.d("????????????", call.toString())
                    }

                })

            }
        }
    }
}