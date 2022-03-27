package kr.co.smartsoft.finalproject_20220318

import android.content.Context
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import kr.co.smartsoft.finalproject_20220318.api.APIList
import kr.co.smartsoft.finalproject_20220318.api.ServerApi

abstract class BaseActivity : AppCompatActivity() {
    lateinit var mContext: Context

    lateinit var apiList : APIList

    lateinit var txtTitle: TextView
    lateinit var imgAdd: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this

        val retrofit = ServerApi.getRerofit(mContext)
        apiList = retrofit.create(APIList::class.java)

        supportActionBar?.let {
            setActionBar()
        }

    }

    abstract fun setUpEvents()
    abstract fun setValues()

    fun setActionBar() {
        val defaultActionBar = supportActionBar!!

        defaultActionBar.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        defaultActionBar.setCustomView(R.layout.my_action_bar)
        val toolBar = defaultActionBar.customView.parent as Toolbar
        toolBar.setContentInsetsAbsolute(0, 0)

        txtTitle = defaultActionBar.customView.findViewById(R.id.txtTitle)
        imgAdd = defaultActionBar.customView.findViewById(R.id.imgPlus)
    }
}