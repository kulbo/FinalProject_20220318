package kr.co.smartsoft.finalproject_20220318.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import kr.co.smartsoft.finalproject_20220318.R
import kr.co.smartsoft.finalproject_20220318.adapters.FriendRequestRecyclerAdapter
import kr.co.smartsoft.finalproject_20220318.adapters.MyFriendsListRecyclerAdapter
import kr.co.smartsoft.finalproject_20220318.databinding.FragmentMyFriendsBinding
import kr.co.smartsoft.finalproject_20220318.databinding.FragmentRequestedUsersBinding
import kr.co.smartsoft.finalproject_20220318.datas.BasicResponse
import kr.co.smartsoft.finalproject_20220318.datas.UserData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RequestedUsersFragment : BaseFragment(){
    lateinit var binding : FragmentRequestedUsersBinding

    val mRequestedList = ArrayList<UserData>()

    lateinit var mRequestedAdapter : FriendRequestRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_requested_users, container, false )
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpEvents()
        setValues()
    }

    override fun setUpEvents() {

    }

    override fun setValues() {
        mRequestedAdapter = FriendRequestRecyclerAdapter(mContext, mRequestedList )
        binding.myRequestedListRecyclerView.adapter = mRequestedAdapter
        binding.myRequestedListRecyclerView.layoutManager = LinearLayoutManager(mContext)
    }

    override fun onResume() {
        super.onResume()
//        목록 새로고침을 위해 이동
        getRequestedListFromServer()
    }

    fun getRequestedListFromServer() {
        apiList.getRequestMyFriendsList("requested").enqueue(object : Callback<BasicResponse> {
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {

                if(response.isSuccessful) {
                    val br = response.body()!!

                    mRequestedList.clear()

                    mRequestedList.addAll(br.data.friends)

                    mRequestedAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

            }

        })
    }

}