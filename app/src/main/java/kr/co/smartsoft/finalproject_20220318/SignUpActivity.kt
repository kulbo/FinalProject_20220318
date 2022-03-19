package kr.co.smartsoft.finalproject_20220318

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
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

        binding.btnSignUp.setOnClickListener {
            val inputEmail = binding.edtEmail.text.toString()
            val inputPassword = binding.edtPassword.toString()
            val inputNickName = binding.edtNickName.toString()

            if (!isDupCheck) {
                Toast.makeText(mContext, "이메일 중복확인을 해주세요", Toast.LENGTH_SHORT).show()
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
    }
}