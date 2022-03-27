package kr.co.smartsoft.finalproject_20220318

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import kr.co.smartsoft.finalproject_20220318.adapters.FrientViewPagerAdapter
import kr.co.smartsoft.finalproject_20220318.adapters.MyPlacesRecyclerAdapter
import kr.co.smartsoft.finalproject_20220318.adapters.SerchRequestRecyclerAdapter
import kr.co.smartsoft.finalproject_20220318.databinding.ActivityManageMyPlacesBinding
import kr.co.smartsoft.finalproject_20220318.datas.BasicResponse
import kr.co.smartsoft.finalproject_20220318.datas.PlaceData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ManageMyPlacesActivity : BaseActivity() {
    lateinit var binding: ActivityManageMyPlacesBinding

    val mMyPlaces = ArrayList<PlaceData>()

    lateinit var mAdapter : MyPlacesRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_manage_my_places)
        setUpEvents()
        setValues()
    }

    override fun onResume() {
        super.onResume()

        getMyPlacesFromServer()
    }
    override fun setUpEvents() {

        binding.btnAddPlace.setOnClickListener {
            val myIntent = Intent(mContext, EditPlaceActivity::class.java)
            startActivity(myIntent)
        }
        imgAdd.setOnClickListener {
            val myIntent = Intent(mContext, EditPlaceActivity::class.java)
            startActivity(myIntent)
        }

    }

    override fun setValues() {
        mAdapter = MyPlacesRecyclerAdapter(mContext, mMyPlaces)
        binding.managePlacesRccyclerView.adapter = mAdapter
        binding.managePlacesRccyclerView.layoutManager = LinearLayoutManager(mContext)

        imgAdd.visibility = View.VISIBLE
        binding.btnAddPlace.visibility = View.GONE
    }

    fun getMyPlacesFromServer(){
        apiList.getRequestMyPlacesList().enqueue(object : Callback<BasicResponse>{
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                if (response.isSuccessful) {
                    val res = response.body()!!
                    mMyPlaces.clear()

                    mMyPlaces.addAll(res.data.places)

                    mAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

            }

        })
    }
}