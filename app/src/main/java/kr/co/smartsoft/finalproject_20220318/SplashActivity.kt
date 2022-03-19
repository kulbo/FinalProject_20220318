package kr.co.smartsoft.finalproject_20220318

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import kr.co.smartsoft.finalproject_20220318.databinding.ActivitySignUpBinding
import kr.co.smartsoft.finalproject_20220318.databinding.ActivitySplashBinding
import kr.co.smartsoft.finalproject_20220318.datas.BasicResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
    }
}