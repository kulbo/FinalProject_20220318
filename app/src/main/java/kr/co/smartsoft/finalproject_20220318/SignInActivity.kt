package kr.co.smartsoft.finalproject_20220318

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.kakao.sdk.user.UserApiClient
import kr.co.smartsoft.finalproject_20220318.databinding.ActivitySignInBinding
import kr.co.smartsoft.finalproject_20220318.datas.BasicResponse
import kr.co.smartsoft.finalproject_20220318.utils.ContextUtil
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignInActivity : BaseActivity() {
    lateinit var binding: ActivitySignInBinding

    //        facebook 로그인 관리자
    lateinit var mCallbackManager : CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in)

        setUpEvents()
        setValues()
    }

    override fun setUpEvents() {

        binding.btnFacebookLogin.setOnClickListener {
            LoginManager.getInstance().registerCallback(mCallbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult?) {
                    Log.d("페북로그인성공", result?.accessToken.toString())
                    val graphRequest = GraphRequest.newMeRequest(result?.accessToken, object : GraphRequest.GraphJSONObjectCallback {
                        override fun onCompleted(jsonObj: JSONObject?, response: GraphResponse?) {
                            Log.d("페북받아온정보", jsonObj!!.toString())

                            apiList.postRequestSocialLogin(
                                "facebook",
                                jsonObj.getString("id"),
                                jsonObj.getString("name")
                            ).enqueue(object : Callback<BasicResponse> {
                                override fun onResponse(
                                    call: Call<BasicResponse>,
                                    response: Response<BasicResponse>
                                ) {
                                    if (response.isSuccessful) {
                                        val br = response.body()!!
                                        ContextUtil.setLoginUserToken(mContext, br.data.token)
                                        Toast.makeText(mContext, "${br.data.user.nick_name}님 페북로그인을 환영합니다.", Toast.LENGTH_SHORT).show()
                                        val myIntent = Intent(mContext, MainActivity::class.java)
                                        startActivity(myIntent)
                                        finish()
                                    }
                                }

                                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                                }

                            })
                        }

                    })
//                   싱크를 맞추도록 호출
                    graphRequest.executeAsync()
                }

                override fun onCancel() {

                }

                override fun onError(error: FacebookException?) {

                }

            })
        }
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
//        카카오로그인 가능한지 확인한다.
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(mContext)) {
            Log.d("카카오로그인", "카톡 앱으로 로그인")
            UserApiClient.instance.loginWithKakaoTalk(mContext) { token, error ->
                Log.d("카카오로그인", "받아온 토큰 : ${token.toString()}")
                getKakaoUserInfo()
            }
        } else {
//            카톡이 설치되지 않아 로구인 창을 뛰운다.
            Log.d("카카오로그인", "카톡 앱없이 로그인")
            UserApiClient.instance.loginWithKakaoAccount(mContext) { token, error ->
                Log.d("카카오로그인", "받아온 토큰 : ${token.toString()}")
                getKakaoUserInfo()
            }
        }
    }

    fun getKakaoUserInfo() {
        UserApiClient.instance.me { user, error ->
            user?.let {
                Log.d("카카오로그인", "ID:${it.id!!.toString()}")
                apiList.postRequestSocialLogin(
                    "kakao",
                    it.id!!.toString(),
                    it.kakaoAccount!!.profile!!.nickname!!
                ).enqueue(object : Callback<BasicResponse>{
                    override fun onResponse(
                        call: Call<BasicResponse>,
                        response: Response<BasicResponse>
                    ) {
                        if (response.isSuccessful) {
                            val br = response.body()!!
                            ContextUtil.setLoginUserToken(mContext, br.data.token)
                            val myIntent = Intent(mContext, MainActivity::class.java)
                            startActivity(myIntent)
                            Toast.makeText(mContext, "${br.data.user.nick_name}님 카톡으로 로그인 되었습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                    }

                })
            }
        }
    }
}