package kr.co.smartsoft.finalproject_20220318

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import kr.co.smartsoft.finalproject_20220318.databinding.ActivityEditPlaceBinding
import kr.co.smartsoft.finalproject_20220318.datas.BasicResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditPlaceActivity : BaseActivity() {
    lateinit var binding: ActivityEditPlaceBinding

    // 선택된 장소 저장 변수 / 지도의 위치 표시 변수
    var mSelectedPoint : LatLng? = null
    var marker : Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_place)
        setUpEvents()
        setValues()
    }

    override fun setUpEvents() {
//        출발장소 저장
        binding.btnSavePlace.setOnClickListener {
            val txtPlaceName = binding.edtPlaceName.text.toString()
            if (txtPlaceName == "") {
                Toast.makeText(mContext, "장소이름을 입력해 주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (mSelectedPoint == null) {
                Toast.makeText(mContext, "지도에서 장소를 선택해 주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val latitude = mSelectedPoint!!.latitude
            val longitude = mSelectedPoint!!.longitude
            val bp = binding.chkBasicPlace.isChecked

            apiList.postRequestAddPlace(
                txtPlaceName,
                latitude,
                longitude,
                bp
            ).enqueue(object :Callback<BasicResponse>{
                override fun onResponse(
                    call: Call<BasicResponse>,
                    response: Response<BasicResponse>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(mContext, "출발장소가 등록되었습니다.", Toast.LENGTH_SHORT).show()
                        finish()
                    }

                }

                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                }

            })


        }

    }

    override fun setValues() {

        imgAdd.visibility = View.GONE

        binding.impMap.getMapAsync {
            val imMap = it

            imMap.setOnMapClickListener { pointF, latLng ->
                if (mSelectedPoint == null) {
                    marker = Marker()
                    marker!!.icon = OverlayImage.fromResource(R.drawable.red_maker_icon)
                }

                mSelectedPoint = latLng

                marker!!.position = latLng
                marker!!.map = imMap
            }
        }
    }
}