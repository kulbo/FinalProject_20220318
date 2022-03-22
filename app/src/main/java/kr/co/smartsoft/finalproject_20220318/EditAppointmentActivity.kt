package kr.co.smartsoft.finalproject_20220318

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.databinding.DataBindingUtil
import kr.co.smartsoft.finalproject_20220318.databinding.ActivityEditAppointmentBinding
import java.text.SimpleDateFormat
import java.util.*

class EditAppointmentActivity : BaseActivity() {
    lateinit var binding: ActivityEditAppointmentBinding

    val mSelectedDateTime = Calendar.getInstance() // 기본값 : 현재일시를 가져온다.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_appointment)
        setUpEvents()
        setValues()
    }

    override fun setUpEvents() {

        setDateTime()
    }

    override fun setValues() {

    }

    fun setDateTime() {
        binding.txtAppointmentDate.setOnClickListener {
            val dsl = object : DatePickerDialog.OnDateSetListener {
                override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int) {
                    mSelectedDateTime.set(year, month, day)
                    val sdf = SimpleDateFormat("yy/MM/dd")
                    binding.txtAppointmentDate.text = sdf.format(mSelectedDateTime.time)
                }
            }

            val dpd = DatePickerDialog(
                mContext,
                dsl,
                mSelectedDateTime.get(Calendar.YEAR),
                mSelectedDateTime.get(Calendar.MONTH),
                mSelectedDateTime.get(Calendar.HOUR_OF_DAY)
            ).show()
        }

        binding.txtAppointmentTime.setOnClickListener {
            val tsl = object : TimePickerDialog.OnTimeSetListener{
                override fun onTimeSet(p0: TimePicker?, hour: Int, minute: Int) {
                    mSelectedDateTime.set(Calendar.HOUR_OF_DAY, hour)
                    mSelectedDateTime.set(Calendar.MINUTE, minute)
                    val sdf = SimpleDateFormat("a h시 m분")
                    binding.txtAppointmentTime.text = sdf.format(mSelectedDateTime.time)
                }
            }

            val tpd = TimePickerDialog(
                mContext,
                tsl,
                12,
                0,
                false
            ).show()
        }
    }
}