package kr.co.smartsoft.finalproject_20220318

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Base64
import android.util.Log
import android.view.View
import kr.co.smartsoft.finalproject_20220318.datas.BasicResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.security.MessageDigest

class SplashActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        setUpEvents()
        setValues()
    }

    override fun setUpEvents() {

    }

    override fun setValues() {

        var isMyInfoLoaded = false

        imgAdd.visibility = View.GONE       // ActionBar에 프러스  버튼 보이지 않계

        apiList.getRequestMyInfo().enqueue(object : Callback<BasicResponse>{
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                if (response.isSuccessful) {
                    isMyInfoLoaded = true
                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

            }

        })

        val myHandler = Handler(Looper.getMainLooper())

        myHandler.postDelayed({

            val myIntent : Intent

            if (isMyInfoLoaded) {
                myIntent = Intent(mContext, MainActivity::class.java)
            } else {
                myIntent = Intent(mContext, SignInActivity::class.java)
            }
            startActivity(myIntent)
            finish()
        },2500)
        getKeyHash()
   }

    // 소셜 로그인시 Hash key값 가져오기
    fun getKeyHash() {
        val info = packageManager.getPackageInfo(
            "kr.co.smartsoft.finalproject_20220318",
            PackageManager.GET_SIGNATURES
        )
        for (signature in info.signatures) {
            val md: MessageDigest = MessageDigest.getInstance("SHA")
            md.update(signature.toByteArray())
            Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
        }
    }

}