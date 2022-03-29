package kr.co.smartsoft.finalproject_20220318

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import kr.co.smartsoft.finalproject_20220318.adapters.FrientViewPagerAdapter
import kr.co.smartsoft.finalproject_20220318.databinding.ActivityManageFriendsBinding
import kr.co.smartsoft.finalproject_20220318.databinding.FragmentMyFriendsBinding

// 친구관리화면 보여주는 Activity
class ManageFriendsActivity : BaseActivity() {
    lateinit var binding : ActivityManageFriendsBinding

    lateinit var mAdapter : FrientViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_manage_friends)
        setUpEvents()
        setValues()
    }

    override fun setUpEvents() {

//        binding.btnAddFriend.setOnClickListener {
//            val myIntent = Intent(mContext, SearchUserActivity::class.java)
//            startActivity(myIntent)
//        }
        // ActionBar를 클릭하면 친구추가 화면으로 이동
        imgAdd.setOnClickListener {
            val myIntent = Intent(mContext, SearchUserActivity::class.java)
            startActivity(myIntent)
        }

    }

    override fun setValues() {
        mAdapter = FrientViewPagerAdapter(supportFragmentManager)
        binding.viewFriends.adapter = mAdapter
        binding.tabFriends.setupWithViewPager(binding.viewFriends)

        // 친구추가 버튼을 숨긴다.
        binding.btnAddFriend.visibility = View.GONE

    }
}