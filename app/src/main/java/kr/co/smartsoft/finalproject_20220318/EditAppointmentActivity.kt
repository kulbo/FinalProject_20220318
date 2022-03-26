package kr.co.smartsoft.finalproject_20220318

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.PathOverlay
import com.odsay.odsayandroidsdk.API
import com.odsay.odsayandroidsdk.ODsayData
import com.odsay.odsayandroidsdk.ODsayService
import com.odsay.odsayandroidsdk.OnResultCallbackListener
import kr.co.smartsoft.finalproject_20220318.adapters.StartPlacesSpinnerAdapter
import kr.co.smartsoft.finalproject_20220318.databinding.ActivityEditAppointmentBinding
import kr.co.smartsoft.finalproject_20220318.datas.BasicResponse
import kr.co.smartsoft.finalproject_20220318.datas.PlaceData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.sign

class EditAppointmentActivity : BaseActivity() {
    lateinit var binding: ActivityEditAppointmentBinding

    val mSelectedDateTime = Calendar.getInstance() // 기본값 : 현재일시를 가져온다.

    var mSelectedPoint : LatLng? = null
    var marker : Marker? = null
    var path: PathOverlay? = null

    var mSelectedLatLng : LatLng? = null

    var naverMap : NaverMap? = null

//      출발장소목록
     val mStartPlaceList = ArrayList<PlaceData>()
     lateinit var mStartPlaceAdapter: StartPlacesSpinnerAdapter

     var mSelectStartPlace : PlaceData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_appointment)
        setUpEvents()
        setValues()
    }

    override fun setUpEvents() {

        binding.startPlaceSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                mSelectStartPlace = mStartPlaceList[position]
                setMapView()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }

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
            Log.d("선택한약속장소 - 위도", "위도 : ${lat.toString()}")
            Log.d("선택한약속장소 - 경도", "경도 : ${lon.toString()}")

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
            naverMap = it

            setMapView()
//            val coord = LatLng( 37.577935237988264, 127.03360346916413)
//            val cameraUpdate = CameraUpdate.scrollTo(coord)
//            naverMap.moveCamera(cameraUpdate)
//
//            naverMap.setOnMapClickListener { pointF, latLng ->
//                if (marker == null) {
//                    marker = Marker()
//                }
//
//                marker!!.position = latLng
//                marker!!.map = naverMap
//
//                mSelectedLatLng = latLng
//            }
        }
        getMyStartPlacesListFromServer()
        mStartPlaceAdapter = StartPlacesSpinnerAdapter(mContext, R.layout.start_place_spinner_list_item, mStartPlaceList)
        binding.startPlaceSpinner.adapter = mStartPlaceAdapter
    }

    fun setMapView() {

//        출발지점이 선택 되어야 세팅 진행.
        if (mSelectStartPlace == null) {
            return // 우선 함수 강제 종료.
        }
//        네이버 맵도 불러와 져야 세팅 진행.
        if (naverMap == null) {
            return // 이 상황도 함수 강제 종료.
        }

        val sLat = mSelectStartPlace!!.latitude
        val sLog = mSelectStartPlace!!.longitude
        Log.d("지도보기 시작 : ", "y = ${sLat}, x = ${sLog}")
//            지도 시작지점 : 선택된 출발 지점.
        val coord = LatLng( sLat, sLog )
//            coord 에 설정한 좌표로 > 네이버지도의 카메라 이동.

        Log.d("시작위치 : ", coord.toString())
        val cameraUpdate = CameraUpdate.scrollTo( coord )
        naverMap!!.moveCamera( cameraUpdate )

//            첫 마커 좌표 -> 학원 위치 -> null

//            val marker = Marker()  => 멤버변수로 하나의 마커만 만들어서 관리하자.
//            marker = Marker()
//            marker!!.position = coord
//            marker!!.map = naverMap

//            처음 선택된 좌표 -> 학원 위치 ->
//            mSelectedLatLng = coord

//            지도 클릭 이벤트

        naverMap!!.setOnMapClickListener { pointF, latLng ->
            Log.d("클릭된 위/경도", "위도 : ${latLng.latitude}, 경도 : ${latLng.longitude}")
//                (찍혀있는 마커가 없다면) 마커를 새로 추가
            if (marker == null) {
                marker = Marker()
            }
//                그 마커의 위치 / 지도 적용
            marker!!.position = latLng
            marker!!.map = naverMap
//                약속 장소도 새 좌표로 설정.
            mSelectedLatLng = latLng
//      coord ~ 선택한 latLng 까지 대중교통 경로를 그려보자. (PathOverlay 기능 활용) + ODSay 라이브러리 활용
            if (path == null) {
                path = PathOverlay()
            }
//      ArrayList를 만들어서, 출발지와 도착지를 추가.
            val coordList = ArrayList<LatLng>()
            coordList.add( coord ) // 출발지를 임시로 학원으로.
            coordList.add( latLng ) // 클릭 된 좌표 추가
            path!!.coords = coordList
            path!!.map = naverMap

            findWay()
        }
    }

//    길 찾기 함수
    fun findWay() {

        if (mSelectStartPlace == null || mSelectedLatLng == null) {
            return          // 시작과 목적지 좌표중 1개라도 없으면
        }
        val myODsayService = ODsayService.init(mContext, "5NFeRMEhquZ01oO58qPoNba4Y0GJA7417pu+DeUHWQI")

        // 대중교통 길찾기 ODsay API 호출
        myODsayService.requestSearchPubTransPath(
            mSelectStartPlace!!.longitude.toString(),
            mSelectStartPlace!!.latitude.toString(),
            mSelectedLatLng!!.longitude.toString(),
            mSelectedLatLng!!.latitude.toString(),
            null,
            null,
            null,
            object: OnResultCallbackListener {
                override fun onSuccess(p0: ODsayData?, p1: API?) {
                    val jsonObj = p0!!.json!!
                    Log.d("길찾기 응답", jsonObj.toString())
                    val resultObj = jsonObj.getJSONObject("result")
                    Log.d("resultObj", resultObj.toString())
                    val pathArr = resultObj.getJSONArray("path")
                    val firstPathObj = pathArr.getJSONObject(0)
                    val stationLatLngList = ArrayList<LatLng>()
                    val subPathArr = firstPathObj.getJSONArray("subPath")
                    for (i in 0 until subPathArr.length()) {
                        val subPathObj = subPathArr.getJSONObject(i)
                        if ( !subPathObj.isNull("passStopList")) {
                            val passStopListObj = subPathObj.getJSONObject("passStopList")
                            val stationsArr = passStopListObj.getJSONArray("stations")
                            for(j in 0 until stationsArr.length()) {
                                val stationObj = stationsArr.getJSONObject(j)
                                val lat = stationObj.getString("y").toDouble()
                                val lng = stationObj.getString("x").toDouble()
                                stationLatLngList.add(LatLng(lat, lng))
                            }
                        }
                    }
                    stationLatLngList.add(mSelectedLatLng!!)
                    path!!.coords = stationLatLngList
                    path!!.map = naverMap

                    val infoObj = firstPathObj.getJSONObject("info")
                    val totalTime = infoObj.getInt("totalTime") // 소요 분
                    val payment = infoObj.getInt("payment")     // 소요 비용

                    val infoWindow = InfoWindow()
                    infoWindow.adapter = object : InfoWindow.DefaultTextAdapter(mContext) {
                        override fun getText(p0: InfoWindow): CharSequence {
                            return "이동시간 : ${totalTime}, 비용 ${payment}원"
                        }
                    }
                    infoWindow.open(marker!!)
                    marker!!.setOnClickListener {
                        if (marker!!.infoWindow == null) {
                            infoWindow.open(marker!!)
                        }
                        else {
                            infoWindow.close()
                        }
                        return@setOnClickListener true
                    }
                    val cameraFocus = CameraUpdate.scrollTo(mSelectedLatLng!!)
                    naverMap!!.moveCamera(cameraFocus)
                }

                override fun onError(p0: Int, p1: String?, p2: API?) {

                }

            }
        )
    }

    fun getMyStartPlacesListFromServer() {
        apiList.getRequestMyPlacesList().enqueue(object : Callback<BasicResponse>{
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                if (response.isSuccessful) {
                    var br = response.body()!!
                    mStartPlaceList.clear()
                    mStartPlaceList.addAll(br.data.places)
                    mStartPlaceAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

            }

        })
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

            val now = Calendar.getInstance()
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
                18,
                0,
                false
            ).show()
        }
    }
}