package kr.co.smartsoft.finalproject_20220318

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kr.co.smartsoft.finalproject_20220318.api.APIList
import kr.co.smartsoft.finalproject_20220318.api.ServerApi

abstract class BaseActivity : AppCompatActivity() {
    lateinit var mContext: Context

    lateinit var apiList : APIList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this

    }

    abstract fun setUpEvents()
    abstract fun setValues()
}