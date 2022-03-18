package kr.co.smartsoft.finalproject_20220318

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import kr.co.smartsoft.finalproject_20220318.databinding.ActivitySignInBinding

class SignInActivity : BaseActivity() {
    lateinit var binding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in)

        setUpEvents()
        setValues()
    }

    override fun setUpEvents() {

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
        }
    }

    override fun setValues() {


    }
}