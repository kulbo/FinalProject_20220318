package kr.co.smartsoft.finalproject_20220318

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import kr.co.smartsoft.finalproject_20220318.databinding.ActivitySignUpBinding

class SignUpActivity : BaseActivity() {
    lateinit var binding : ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)
        setUpEvents()
        setValues()
    }

    override fun setUpEvents() {


    }

    override fun setValues() {

    }
}