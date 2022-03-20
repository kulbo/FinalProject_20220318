package kr.co.smartsoft.finalproject_20220318.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import kr.co.smartsoft.finalproject_20220318.api.APIList
import kr.co.smartsoft.finalproject_20220318.api.ServerApi

abstract class BaseFragment : Fragment() {
    lateinit var mContext : Context

    lateinit var apiList: APIList

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mContext = requireContext()

        val retrofit = ServerApi.getRerofit(mContext)
        apiList = retrofit.create(APIList::class.java)
    }
    abstract fun setUpEvents()
    abstract fun setValues()
}