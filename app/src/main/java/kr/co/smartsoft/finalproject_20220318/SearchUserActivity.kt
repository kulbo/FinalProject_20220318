package kr.co.smartsoft.finalproject_20220318

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import kr.co.smartsoft.finalproject_20220318.adapters.SerchRequestRecyclerAdapter
import kr.co.smartsoft.finalproject_20220318.api.APIList
import kr.co.smartsoft.finalproject_20220318.api.ServerApi
import kr.co.smartsoft.finalproject_20220318.databinding.ActivitySearchUserBinding
import kr.co.smartsoft.finalproject_20220318.datas.BasicResponse
import kr.co.smartsoft.finalproject_20220318.datas.UserData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchUserActivity : BaseActivity() {
    lateinit var binding: ActivitySearchUserBinding

    val mSearchedUserList = ArrayList<UserData>()

    lateinit var mAdapter: SerchRequestRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_user)
        setUpEvents()
        setValues()
    }

    override fun setUpEvents() {

        binding.btnSearch.setOnClickListener {
            val txtNickname = binding.edtNickname.text.toString()
            val retrofit = ServerApi.getRerofit(mContext)
            val apiList = retrofit.create(APIList::class.java)

            apiList.getRequestUserList(txtNickname).enqueue(object : Callback<BasicResponse>{
                override fun onResponse(
                    call: Call<BasicResponse>,
                    response: Response<BasicResponse>
                ) {
                    mSearchedUserList.clear()

                    if(response.isSuccessful) {
                        val body = response.body()!!

                        mSearchedUserList.addAll(body.data.users)

                        Log.d("검색된", mSearchedUserList.toString())
                        mAdapter.notifyDataSetChanged()
                    }
                }

                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                }

            })
        }
    }

    override fun setValues() {
        imgAdd.visibility = View.GONE
        mAdapter = SerchRequestRecyclerAdapter(mContext, mSearchedUserList)
        binding.seachListRecyclerView.adapter = mAdapter
        binding.seachListRecyclerView.layoutManager = LinearLayoutManager(mContext)
    }
}