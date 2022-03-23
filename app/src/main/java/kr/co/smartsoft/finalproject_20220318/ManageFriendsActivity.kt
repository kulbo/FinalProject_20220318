package kr.co.smartsoft.finalproject_20220318

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import kr.co.smartsoft.finalproject_20220318.adapters.FrientViewPagerAdapter
import kr.co.smartsoft.finalproject_20220318.databinding.ActivityManageFriendsBinding
import kr.co.smartsoft.finalproject_20220318.databinding.FragmentMyFriendsBinding

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

    }

    override fun setValues() {
        mAdapter = FrientViewPagerAdapter(supportFragmentManager)
        binding.viewFriends.adapter = mAdapter
        binding.tabFriends.setupWithViewPager(binding.viewFriends)

    }
}