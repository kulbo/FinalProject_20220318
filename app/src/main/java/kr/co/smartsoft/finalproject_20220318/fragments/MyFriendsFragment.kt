package kr.co.smartsoft.finalproject_20220318.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import kr.co.smartsoft.finalproject_20220318.R
import kr.co.smartsoft.finalproject_20220318.adapters.MyFriendsListRecyclerAdapter
import kr.co.smartsoft.finalproject_20220318.databinding.FragmentMyFriendsBinding
import kr.co.smartsoft.finalproject_20220318.datas.BasicResponse
import kr.co.smartsoft.finalproject_20220318.datas.UserData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyFriendsFragment : BaseFragment(){
    lateinit var binding : FragmentMyFriendsBinding

    val mMyFriendsList = ArrayList<UserData>()

    lateinit var mMyFriendAdapter : MyFriendsListRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_my_friends, container, false )
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

        mMyFriendAdapter = MyFriendsListRecyclerAdapter(mContext, mMyFriendsList )
        binding.myFriendsListRecyclerView.adapter = mMyFriendAdapter
        binding.myFriendsListRecyclerView.layoutManager = LinearLayoutManager(mContext)

        getMyFriendsListFromServer()
    }

    fun getMyFriendsListFromServer() {
        apiList.getRequestMyFriendsList("my").enqueue(object : Callback<BasicResponse> {
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {

                if(response.isSuccessful) {
                    val br = response.body()!!

                    mMyFriendsList.clear()

                    mMyFriendsList.addAll(br.data.friends)

                    mMyFriendAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

            }

        })
    }

}