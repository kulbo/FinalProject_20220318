package kr.co.smartsoft.finalproject_20220318

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.PathOverlay
import com.odsay.odsayandroidsdk.API
import com.odsay.odsayandroidsdk.ODsayData
import com.odsay.odsayandroidsdk.ODsayService
import com.odsay.odsayandroidsdk.OnResultCallbackListener
import kr.co.smartsoft.finalproject_20220318.databinding.ActivityViewMapBinding
import kr.co.smartsoft.finalproject_20220318.datas.AppointmentData

class ViewMapActivity : BaseActivity() {

    lateinit var binding : ActivityViewMapBinding

    lateinit var mAppointment : AppointmentData

    var sMarker : Marker? = null
    var marker : Marker? = null

    var path: PathOverlay? = null
    var naverMap : NaverMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_map)
        mAppointment = intent.getSerializableExtra("appointment") as AppointmentData
        setUpEvents()
        setValues()
    }

    override fun setUpEvents() {

    }

    override fun setValues() {

//        ActionBar의 문구를 변경
        txtTitle.text = "약속장소 지도 확인"
        imgAdd.visibility = View.GONE

        binding.naverMapView.getMapAsync {
            naverMap = it

            setMapView()
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

        val marker = Marker()
        marker.position = destLatLng
        marker.map = naverMap
        val stationList = ArrayList<LatLng>()

//            첫 좌표는 출발 장소.
        stationList.add( LatLng( mAppointment.start_latitude, mAppointment.start_longitude ) )

        val myODsayService = ODsayService.init(mContext, "5NFeRMEhquZ01oO58qPoNba4Y0GJA7417pu+DeUHWQI")
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
                    Log.d("길찾기 응답", jsonObj.toString())
                    val resultObj = jsonObj.getJSONObject("result")
                    Log.d("resultObj", resultObj.toString())
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
                    path!!.coords = stationList           // path애 정류장정보를 지정
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
//                    infoWindow.adapter = object : InfoWindow.DefaultTextAdapter(mContext) {
//                        override fun getText(p0: InfoWindow): CharSequence {
//                            return "이동시간 : ${totalTime}분, 비용 ${payment}원"
//                        }
//
//                    }
                    infoWindow.open(marker!!)
                    val cameraFocus = CameraUpdate.scrollTo(LatLng(mAppointment.latitude, mAppointment.longitude))
                    naverMap!!.moveCamera(cameraFocus)
                }

                override fun onError(p0: Int, p1: String?, p2: API?) {

                }

            }
        )
    }
}