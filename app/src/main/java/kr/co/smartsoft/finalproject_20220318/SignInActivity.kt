package kr.co.smartsoft.finalproject_20220318

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.kakao.sdk.user.UserApiClient
import kr.co.smartsoft.finalproject_20220318.databinding.ActivitySignInBinding
import kr.co.smartsoft.finalproject_20220318.datas.BasicResponse
import kr.co.smartsoft.finalproject_20220318.utils.ContextUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignInActivity : BaseActivity() {
    lateinit var binding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in)

        setUpEvents()
        setValues()
    }

    override fun setUpEvents() {

        binding.btnKakaoLogin.setOnClickListener {
            getKakaoLogin()
        }
        binding.btnSignUp.setOnClickListener {
            val myIntent = Intent(mContext, SignUpActivity::class.java)
            startActivity(myIntent)
        }

        binding.btnLogIn.setOnClickListener {
            val edtId = binding.edtEmail.text.toString()
            val edtPasswd = binding.edtPassword.text.toString()

            if (edtId == ""){
                Toast.makeText(mContext, "이메일이 입력되지 않았습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (edtPasswd == ""){
                Toast.makeText(mContext, "비밀번호가 입력되지 않았습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            apiList.postRequestLogin(edtId, edtPasswd).enqueue(object : Callback<BasicResponse>{
                override fun onResponse(
                    call: Call<BasicResponse>,
                    response: Response<BasicResponse>
                ) {
                    if (response.isSuccessful) {
                        val br = response.body()!!
                        Toast.makeText(mContext, "${br.data.user.nick_name}님 환영합니다.", Toast.LENGTH_SHORT).show()

                        ContextUtil.setLoginUserToken(mContext, br.data.token)

                        val myIntent = Intent(mContext, MainActivity::class.java)
                        startActivity(myIntent)
                        finish()
                    }
                    else {
                        Toast.makeText(mContext, response.message(), Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                }

            })


        }
    }

    override fun setValues() {
    }

    fun getKakaoLogin(){
    }
}