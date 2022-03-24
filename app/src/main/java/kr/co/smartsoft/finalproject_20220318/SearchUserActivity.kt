package kr.co.smartsoft.finalproject_20220318

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import kr.co.smartsoft.finalproject_20220318.databinding.ActivitySearchUserBinding

class SearchUserActivity : BaseActivity() {
    lateinit var binding: ActivitySearchUserBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_user)
        setUpEvents()
        setValues()
    }

    override fun setUpEvents() {

    }

    override fun setValues() {

    }
}