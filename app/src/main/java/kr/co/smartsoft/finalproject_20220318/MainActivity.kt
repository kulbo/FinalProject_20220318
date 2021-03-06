package kr.co.smartsoft.finalproject_20220318

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import kr.co.smartsoft.finalproject_20220318.adapters.MainViewPager2Adapter
import kr.co.smartsoft.finalproject_20220318.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setUpEvents()
        setValues()
    }

    override fun setUpEvents() {
        binding.btnBottomNav.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.myAppointment -> {
                    binding.mainViewPager2.currentItem = 0
                    imgAdd.visibility = View.VISIBLE        // ActionBar 의 플러스 버튼 보이도록
                }
                R.id.myProfile -> {
                    binding.mainViewPager2.currentItem = 1
                    imgAdd.visibility = View.GONE           // ActionBar 의 플러스 버튼 보이지 않도록
                }
            }
            return@setOnItemSelectedListener true
        }
        binding.mainViewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                binding.btnBottomNav.selectedItemId = when(position) {
                    0 -> R.id.myAppointment
                    else -> R.id.myProfile
                }
            }
        })
        imgAdd.setOnClickListener {
            val myIntent = Intent(mContext, EditAppointmentActivity::class.java)
            startActivity(myIntent)
        }

    }

    override fun setValues() {
        binding.mainViewPager2.adapter = MainViewPager2Adapter(this)
    }
}