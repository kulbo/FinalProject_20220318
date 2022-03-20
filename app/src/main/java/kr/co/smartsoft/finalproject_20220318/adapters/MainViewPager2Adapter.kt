package kr.co.smartsoft.finalproject_20220318.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import kr.co.smartsoft.finalproject_20220318.fragments.AppointmentListFragment
import kr.co.smartsoft.finalproject_20220318.fragments.MyProfileFragment


class MainViewPager2Adapter(fa : FragmentActivity) : FragmentStateAdapter(fa) {
    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> AppointmentListFragment()
            else -> MyProfileFragment()
        }
    }
}