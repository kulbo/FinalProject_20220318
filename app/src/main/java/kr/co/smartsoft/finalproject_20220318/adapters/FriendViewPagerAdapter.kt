package kr.co.smartsoft.finalproject_20220318.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import kr.co.smartsoft.finalproject_20220318.databinding.FragmentMyProfileBinding
import kr.co.smartsoft.finalproject_20220318.fragments.AppointmentListFragment
import kr.co.smartsoft.finalproject_20220318.fragments.MyFriendsFragment
import kr.co.smartsoft.finalproject_20220318.fragments.MyProfileFragment
import kr.co.smartsoft.finalproject_20220318.fragments.RequestedUsersFragment


open class FriendViewPagerAdapter(fm : FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getCount() = 2
    override fun getItem(position: Int): Fragment {

        return when(position) {
            0 -> MyFriendsFragment()
            else -> RequestedUsersFragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position) {
            0 -> "내 친구 목록"
            else -> "친구 요청 확인"
        }
    }
}
