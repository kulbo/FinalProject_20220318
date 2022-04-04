package kr.co.smartsoft.finalproject_20220318

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import kr.co.smartsoft.finalproject_20220318.databinding.ActivityModifyAppointmentBinding
import kr.co.smartsoft.finalproject_20220318.datas.AppointmentData

class ModifyAppointmentActivity : BaseActivity() {
    lateinit var binding : ActivityModifyAppointmentBinding
    lateinit var mAppoinment : AppointmentData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_modify_appointment)
        mAppoinment = intent.getSerializableExtra("appointment") as AppointmentData
        setUpEvents()
        setValues()
    }

    override fun setUpEvents() {

    }

    override fun setValues() {
        imgAdd.visibility = View.GONE           // ActionBar 의 플러스 버튼 보이지 않도록

        binding.txtTitle.text = mAppoinment.title
    }
}