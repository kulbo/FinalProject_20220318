package kr.co.smartsoft.finalproject_20220318

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import kr.co.smartsoft.finalproject_20220318.databinding.ActivitySignUpBinding
import kr.co.smartsoft.finalproject_20220318.datas.BasicResponse
import kr.co.smartsoft.finalproject_20220318.utils.ContextUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpActivity : BaseActivity() {

    lateinit var binding : ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)
        setValues()
        setUpEvents()
    }

    override fun setUpEvents() {

        var isDupCheck = false
        var isNickDupCheck = false

//            이메일 중복체크
        binding.btnDupCheck.setOnClickListener{
            val inputEmail = binding.edtEmail.text.toString()

            apiList.getDupulicateCheck("EMAIL", inputEmail).enqueue(object : Callback<BasicResponse>{
                override fun onResponse(
                    call: Call<BasicResponse>,
                    response: Response<BasicResponse>
                ) {
                    if (response.isSuccessful) {
                        isDupCheck = true
                        Toast.makeText(mContext, "사용해도 좋은 이메일입니다.", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(mContext, "다른 이메일을 사용하세요.", Toast.LENGTH_SHORT).show()

                    }
                }

                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                }

            })
        }

//        닉네임 중복체크 처리
        binding.btnNickDupCheck.setOnClickListener {
            val inputNickName = binding.edtNickName.text.toString()

            apiList.getDupulicateCheck("NICK_NAME", inputNickName).enqueue(object : Callback<BasicResponse>{
                override fun onResponse(
                    call: Call<BasicResponse>,
                    response: Response<BasicResponse>
                ) {
                    if (response.isSuccessful) {
                        isNickDupCheck = true
                        Toast.makeText(mContext, "사용해도 좋은 닉네임입니다.", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(mContext, "다른 닉네임을 사용하세요.", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                }

            })
        }

        binding.btnSignUp.setOnClickListener {
            val inputEmail = binding.edtEmail.text.toString()
            val inputPassword = binding.edtPassword.text.toString()
            val inputNickName = binding.edtNickName.text.toString()
            val inputPassword1 = binding.edtPassword1.text.toString()

            if (!isDupCheck) {
                Toast.makeText(mContext, "이메일 중복확인을 해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!isNickDupCheck) {
                Toast.makeText(mContext, "닉네임 중복확인을 해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (inputEmail.length == 0) {
                Toast.makeText(mContext, "이메일을 입력해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (inputPassword.length < 8) {
                Toast.makeText(mContext, "비밀번호는 8자리 이상을 입력해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (inputNickName.length == 0) {
                Toast.makeText(mContext, "이메일을 입력해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (inputPassword != inputPassword1) {
                Toast.makeText(mContext, "비밀번호와 비밀번호확인이 일치해야 합니다", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            apiList.putRequestSignUp(inputEmail, inputPassword, inputNickName).enqueue(object : Callback<BasicResponse>{
                override fun onResponse(
                    call: Call<BasicResponse>,
                    response: Response<BasicResponse>
                ) {
                    if (response.isSuccessful) {
                        val br = response.body()!!
                        Toast.makeText(mContext, "${br.data.user.nick_name}님 가입을 축하합니다.", Toast.LENGTH_SHORT).show()
                    }

                }

                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                }

            })
        }

    }

    override fun setValues() {
        ContextUtil.setLoginUserToken(mContext, "")

        imgAdd.visibility = View.GONE       // Action Bar의 + 버튼 보이지 않게
    }
}