package uz.os3ketchup.navigationdrawer.adapter

import android.content.res.Resources
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import uz.os3ketchup.navigationdrawer.ChatFragment
import uz.os3ketchup.navigationdrawer.GroupFragment

class MyViewPagerAdapter(fragmentActivity: FragmentActivity):FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0->{
                ChatFragment()
            }
            1->{
                GroupFragment()
            }else->{
                throw Resources.NotFoundException("Position not found")
            }
        }
    }
}