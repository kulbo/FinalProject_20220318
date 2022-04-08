package kr.co.smartsoft.finalproject_20220318

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import android.widget.TimePicker
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
import kr.co.smartsoft.finalproject_20220318.databinding.ActivityModifyAppointmentBinding
import kr.co.smartsoft.finalproject_20220318.datas.AppointmentData
import kr.co.smartsoft.finalproject_20220318.datas.BasicResponse
import kr.co.smartsoft.finalproject_20220318.datas.PlaceData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ModifyAppointmentActivity : BaseActivity() {
    lateinit var binding : ActivityModifyAppointmentBinding
    lateinit var mAppointment : AppointmentData
    var sMarker : Marker? = null
    var path: PathOverlay? = null
    var naverMap : NaverMap? = null
    val mStartPlaceList = ArrayList<PlaceData>()
    lateinit var mStartPlaceAdapter: StartPlacesSpinnerAdapter
    var marker = Marker()
    var mSelectedLatLng : LatLng? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_modify_appointment)
        mAppointment = intent.getSerializableExtra("appointment") as AppointmentData
        setValues()
        setUpEvents()
    }

    override fun setUpEvents() {

        setDateTime()
    }

    override fun setValues() {
        imgAdd.visibility = View.GONE           // ActionBar 의 플러스 버튼 보이지 않도록
        binding.txtTitle.text = mAppointment.title

        val sdf = SimpleDateFormat("yy/MM/dd")
        val stf = SimpleDateFormat("a H시 m분")
        val sdt = sdf.format(mAppointment.datetime)
        val stt = stf.format(mAppointment.datetime)
        binding.txtDate.text = sdt.toString()
        binding.txtTime.text = stt.toString()
        // EditText 속성에 대해서 텍스트를 변경하려먼 아래와 같이 수정해주어야 한다.
        binding.edtPlaceName.setText("${mAppointment.place}")

        getMyStartPlacesListFromServer()
        mStartPlaceAdapter = StartPlacesSpinnerAdapter(mContext, R.layout.start_place_spinner_list_item, mStartPlaceList)
        binding.spiStartPlace.adapter = mStartPlaceAdapter

        binding.imgViewMap.getMapAsync {
            naverMap = it
            naverMap!!.setOnMapClickListener {
                    pointF,
                    latLng ->
    //                (찍혀있는 마커가 없다면) 마커를 새로 추가
                if (marker == null) marker = Marker()
                marker!!.position = latLng
                marker!!.map = naverMap
                mSelectedLatLng = latLng
                if (path == null) path = PathOverlay()
    //            coordList.add(latLng)   // 클릭된 좌표 추가
    //            path!!.coords = coordList
    //            path!!.map = naverMap

                findWay(latLng.latitude, latLng.longitude)               // ODsay Library 이용
            }
            setMapView()
        }
    }

    fun getMyStartPlacesListFromServer() {
        apiList.getRequestMyPlacesList().enqueue(object : Callback<BasicResponse> {
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
        var mSelectedDateTime = Calendar.getInstance() // 기본값 : 현재일시를 가져온다.
        binding.txtDate.setOnClickListener {
            val dsl = object : DatePickerDialog.OnDateSetListener {
                override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int) {
                    mSelectedDateTime.set(year, month, day)
                    val sdf = SimpleDateFormat("yy/MM/dd")
                    binding.txtDate.text = sdf.format(mSelectedDateTime.time)
                }
            }

//            val now = Calendar.getInstance()
            var sdf = SimpleDateFormat("yyyy")
            val year = sdf.format(mAppointment.datetime).toInt()
            sdf = SimpleDateFormat("MM")
            val month = sdf.format(mAppointment.datetime).toInt()-1
            sdf = SimpleDateFormat("dd")
            val day = sdf.format(mAppointment.datetime).toInt()
            Log.d("년도, 달, 일", "${year}, ${month}, ${day}")
            val dpd = DatePickerDialog(
                mContext, dsl, year, month, day,
            ).show()
        }

        binding.txtTime.setOnClickListener {
            val tsl = object : TimePickerDialog.OnTimeSetListener{
                override fun onTimeSet(p0: TimePicker?, hour: Int, minute: Int) {
                    mSelectedDateTime.set(Calendar.HOUR_OF_DAY, hour)
                    mSelectedDateTime.set(Calendar.MINUTE, minute)
                    val sdf = SimpleDateFormat("a h시 m분")
                    binding.txtTime.text = sdf.format(mSelectedDateTime.time)
                }
            }
            var sdf = SimpleDateFormat("hh")
            val hour = sdf.format(mAppointment.datetime).toInt()
            sdf = SimpleDateFormat("mm")
            val minute = sdf.format(mAppointment.datetime).toInt()
            val tpd = TimePickerDialog(
                mContext,
                tsl,
                hour,
                minute,
                false
            ).show()
        }
    }

    fun setMapView() {
        val sLat = mAppointment.start_latitude
        val sLog = mAppointment.start_longitude
        val sCoord = LatLng(sLat, sLog)

        if (sMarker == null) sMarker = Marker()
        sMarker!!.position = sCoord
        sMarker!!.map = naverMap
        findWay()               // ODsay Library 이용
    }

    //    길 찾기 함수
    fun findWay() {
        val destLatLng = LatLng(mAppointment.latitude, mAppointment.longitude)
        val cameraUpdate = CameraUpdate.scrollTo(destLatLng)
        naverMap!!.moveCamera(cameraUpdate)

        marker.position = destLatLng
        marker.map = naverMap
        val stationList = ArrayList<LatLng>()

//            첫 좌표는 출발 장소.
        stationList.add( LatLng( mAppointment.start_latitude, mAppointment.start_longitude ) )

        val myODsayService = ODsayService.init(mContext, "5NFeRMEhquZ01oO58qPoNba4Y0GJA7417pu+DeUHWQI")
//        val myODsayService = ODsayService.init(mContext, "@string/odsay_token")
        // 대중교통 길찾기 ODsay API 호출
        myODsayService.requestSearchPubTransPath(
            mAppointment.start_longitude.toString(),
            mAppointment.start_latitude.toString(),
            mAppointment.longitude.toString(),
            mAppointment.latitude.toString(),
            null,
            null,
            null,
            object: OnResultCallbackListener {
                override fun onSuccess(p0: ODsayData?, p1: API?) {
                    val jsonObj = p0!!.json!!
                    val resultObj = jsonObj.getJSONObject("result")
                    val pathArr = resultObj.getJSONArray("path")    // 1-9 추천경로들을 pathArr로 지정
                    val firstPathObj = pathArr.getJSONObject(0)     // 첫번째 추천 경로만 받아온다.
//                    출발지 좌표를 정거장 목록에 추가
                    val subPathArr = firstPathObj.getJSONArray("subPath")       // 이동 교통수단정보를 subPathArr에 지정
                    for (i in 0 until subPathArr.length()) {
                        val subPathObj = subPathArr.getJSONObject(i)
                        if ( !subPathObj.isNull("passStopList")) {
                            val passStopListObj = subPathObj.getJSONObject("passStopList")  // 경로 상세구간 정보 확장
                            val stationsArr = passStopListObj.getJSONArray("stations")      // 정류장 정보들을 stationsArr에 지정
                            for(j in 0 until stationsArr.length()) {
                                val stationObj = stationsArr.getJSONObject(j)
                                val lat = stationObj.getString("y").toDouble()  // 정류장의 위도를 lat
                                val lng = stationObj.getString("x").toDouble()  // 정류장의 경도를 lng
                                stationList.add(LatLng(lat, lng))               // 정류장의 위도, 경도를 좌표로 변환하여 경로에 추가
                            }
                        }
                    }
                    stationList.add(LatLng(mAppointment.latitude, mAppointment.longitude))                // 도착지를 경로에 추가
                    path = PathOverlay()
                    path!!.coords = stationList                 // path애 정류장정보를 지정
                    path!!.map = naverMap                       // 지도에 표시

                    val infoObj = firstPathObj.getJSONObject("info")    // 요약정보를 가져온다.
                    val totalTime = infoObj.getInt("totalTime")         // 총 소요시간 분
                    val payment = infoObj.getInt("payment")             // 총 요금

                    val infoWindow = InfoWindow()
                    infoWindow.adapter = object : InfoWindow.DefaultViewAdapter(mContext) {
                        override fun getContentView(p0: InfoWindow): View {
                            val view = LayoutInflater.from(mContext).inflate(R.layout.destination_info_window, null)
                            val txtPlaceName = view.findViewById<TextView>(R.id.txtPlanceName)
                            val txtDataTime = view.findViewById<TextView>(R.id.txtDateTime)
                            val txtPayment = view.findViewById<TextView>(R.id.txtPayment)

                            txtPlaceName.text = mAppointment.place
                            txtDataTime.text = "${totalTime}분 소요"
                            txtPayment.text = "${payment}원 필요"

                            return view
                        }
                    }
                    infoWindow.open(marker)
                    val cameraFocus = CameraUpdate.scrollTo(LatLng(mAppointment.latitude, mAppointment.longitude))
                    naverMap!!.moveCamera(cameraFocus)
                }
                override fun onError(p0: Int, p1: String?, p2: API?) {
                }
            }
        )
    }

    //    길 찾기 함수
    fun findWay(latitude : Double, longitude : Double) {
        val destLatLng = LatLng(latitude, longitude)
        val cameraUpdate = CameraUpdate.scrollTo(destLatLng)
        naverMap!!.moveCamera(cameraUpdate)

        marker.position = destLatLng
        marker.map = naverMap
        val stationList = ArrayList<LatLng>()

//            첫 좌표는 출발 장소.
        stationList.add( LatLng( mAppointment.start_latitude, mAppointment.start_longitude ) )

        val myODsayService = ODsayService.init(mContext, "5NFeRMEhquZ01oO58qPoNba4Y0GJA7417pu+DeUHWQI")
//        val myODsayService = ODsayService.init(mContext, "@string/odsay_token")
        // 대중교통 길찾기 ODsay API 호출
        myODsayService.requestSearchPubTransPath(
            mAppointment.start_longitude.toString(),
            mAppointment.start_latitude.toString(),
            longitude.toString(),
            latitude.toString(),
            null,
            null,
            null,
            object: OnResultCallbackListener {
                override fun onSuccess(p0: ODsayData?, p1: API?) {
                    val jsonObj = p0!!.json!!
//                    Log.d("길찾기 응답", jsonObj.toString())
                    val resultObj = jsonObj.getJSONObject("result")
//                    Log.d("resultObj", resultObj.toString())
                    val pathArr = resultObj.getJSONArray("path")    // 1-9 추천경로들을 pathArr로 지정
                    val firstPathObj = pathArr.getJSONObject(0)     // 첫번째 추천 경로만 받아온다.
//                    출발지 좌표를 정거장 목록에 추가
                    val subPathArr = firstPathObj.getJSONArray("subPath")       // 이동 교통수단정보를 subPathArr에 지정
                    for (i in 0 until subPathArr.length()) {
                        val subPathObj = subPathArr.getJSONObject(i)
                        if ( !subPathObj.isNull("passStopList")) {
                            val passStopListObj = subPathObj.getJSONObject("passStopList")  // 경로 상세구간 정보 확장
                            val stationsArr = passStopListObj.getJSONArray("stations")      // 정류장 정보들을 stationsArr에 지정
                            for(j in 0 until stationsArr.length()) {
                                val stationObj = stationsArr.getJSONObject(j)
                                val lat = stationObj.getString("y").toDouble()  // 정류장의 위도를 lat
                                val lng = stationObj.getString("x").toDouble()  // 정류장의 경도를 lng
                                stationList.add(LatLng(lat, lng))               // 정류장의 위도, 경도를 좌표로 변환하여 경로에 추가
                            }
                        }
                    }
                    stationList.add(LatLng(latitude, longitude))                // 도착지를 경로에 추가
                    path = PathOverlay()
                    path!!.coords = stationList                 // path애 정류장정보를 지정
                    path!!.map = naverMap                       // 지도에 표시

                    val infoObj = firstPathObj.getJSONObject("info")    // 요약정보를 가져온다.
                    val totalTime = infoObj.getInt("totalTime")         // 총 소요시간 분
                    val payment = infoObj.getInt("payment")             // 총 요금

                    val infoWindow = InfoWindow()
                    infoWindow.adapter = object : InfoWindow.DefaultViewAdapter(mContext) {
                        override fun getContentView(p0: InfoWindow): View {
                            val view = LayoutInflater.from(mContext).inflate(R.layout.destination_info_window, null)
                            val txtPlaceName = view.findViewById<TextView>(R.id.txtPlanceName)
                            val txtDataTime = view.findViewById<TextView>(R.id.txtDateTime)
                            val txtPayment = view.findViewById<TextView>(R.id.txtPayment)

                            txtPlaceName.text = binding.edtPlaceName.toString()
                            txtDataTime.text = "${totalTime}분 소요"
                            txtPayment.text = "${payment}원 필요"

                            return view
                        }
                    }
                    infoWindow.open(marker)
                    val cameraFocus = CameraUpdate.scrollTo(LatLng(latitude, longitude))
                    naverMap!!.moveCamera(cameraFocus)
                }
                override fun onError(p0: Int, p1: String?, p2: API?) {
                }
            }
        )
    }

}