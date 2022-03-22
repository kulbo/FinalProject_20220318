package kr.co.smartsoft.finalproject_20220318

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import kr.co.smartsoft.finalproject_20220318.databinding.ActivityEditAppointmentBinding
import kr.co.smartsoft.finalproject_20220318.datas.BasicResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class EditAppointmentActivity : BaseActivity() {
    lateinit var binding: ActivityEditAppointmentBinding

    val mSelectedDateTime = Calendar.getInstance() // 기본값 : 현재일시를 가져온다.

    var mSelectedPoint : LatLng? = null
    var marker : Marker? = null

    var mSelectedLatLng : LatLng? = null

    var naverMap : NaverMap? = null

    // val mStartPlaceList : LatLng? = null
    // lateinit var mStartPlaceAdapter: StartPlaceSpinnerAdapter

    // var mSelectStartPlace : PlaceData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_appointment)
        setUpEvents()
        setValues()
    }

    override fun setUpEvents() {

        setDateTime()

        binding.btnAppointSave.setOnClickListener {
            val edtTitle = binding.edtTitle.text.toString()
            val edtPlace = binding.edtAppointmentPlace.text.toString()
            val txtDate = binding.txtAppointmentDate.text.toString()
            val txtTime = binding.txtAppointmentTime.text.toString()

            if ( edtTitle == "" ) {
                Toast.makeText(mContext, "약속 이름은 입력해 주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (edtPlace == "") {
                Toast.makeText(mContext, "약속 장소를 입력해 주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (txtDate == "") {
                Toast.makeText(mContext, "약속 일자를 입력해 주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (txtTime == "") {
                Toast.makeText(mContext, "약속 시간을 입력해 주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (mSelectedLatLng == null) {
                Toast.makeText(mContext, "약속 장소를 선택하지 않았습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val now = Calendar.getInstance()
            if (mSelectedDateTime.timeInMillis < now.timeInMillis) {
                Toast.makeText(mContext, "현재 이후의 시간으로 선택해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val lat = mSelectedLatLng!!.latitude
            val lon = mSelectedLatLng!!.longitude
            Log.d("선택한약속장소 - 위도", "위도 : ${lat}")
            Log.d("선택한약속장소 - 위도", "위도 : ${lon}")

            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm")
            val sdt = sdf.format(mSelectedDateTime.time)

            apiList.postRequestAddAppointment(
                edtTitle, sdt, edtPlace, lat, lon

            ).enqueue(object : Callback<BasicResponse>{
                override fun onResponse(
                    call: Call<BasicResponse>,
                    response: Response<BasicResponse>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(mContext, "약속이 등록되었습니다.", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }

                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                }

            })

        }
    }

    override fun setValues() {

        binding.naverMapView.getMapAsync {
            val naverMap = it
            val coord = LatLng( 37.577935237988264, 127.03360346916413)
            val cameraUpdate = CameraUpdate.scrollTo(coord)
            naverMap.moveCamera(cameraUpdate)

            naverMap.setOnMapClickListener { pointF, latLng ->
                if (marker == null) {
                    marker = Marker()
                }

                marker!!.position = latLng
                marker!!.map = naverMap

                mSelectedLatLng = latLng
            }
        }
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