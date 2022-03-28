package kr.co.smartsoft.finalproject_20220318.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import kr.co.smartsoft.finalproject_20220318.EditAppointmentActivity
import kr.co.smartsoft.finalproject_20220318.R
import kr.co.smartsoft.finalproject_20220318.adapters.AppointmentRecyclerAdapter
import kr.co.smartsoft.finalproject_20220318.databinding.FragmentAppointmentListBinding
import kr.co.smartsoft.finalproject_20220318.datas.AppointmentData
import kr.co.smartsoft.finalproject_20220318.datas.BasicResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AppointmentListFragment : BaseFragment() {

    lateinit var binding : FragmentAppointmentListBinding

    val mAppointmentList = ArrayList<AppointmentData>()

    lateinit var mAppointmentAdapter: AppointmentRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_appointment_list, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpEvents()
        setValues()
    }
    override fun setUpEvents() {

        binding.btnMakeAppoint.setOnClickListener {
            val myIntent = Intent(mContext, EditAppointmentActivity::class.java)
            startActivity(myIntent)
        }
    }

    override fun setValues() {
        mAppointmentAdapter = AppointmentRecyclerAdapter(mContext, mAppointmentList)
        binding.appointmentRecycleView.adapter = mAppointmentAdapter
        binding.appointmentRecycleView.layoutManager = LinearLayoutManager(mContext)

        binding.btnMakeAppoint.visibility = View.GONE       // 약속추가 버튼 보이지 않게
    }

    override fun onResume() {
        super.onResume()

        getAppointmentListFromServer()
    }


    fun getAppointmentListFromServer() {
        apiList.getRequestAppointmentList().enqueue(object : Callback<BasicResponse> {
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {

                if(response.isSuccessful) {
                    mAppointmentList.clear()
                    val br = response.body()!!

                    mAppointmentList.addAll(br.data.appointments)
                    mAppointmentAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

            }

        })
    }
}