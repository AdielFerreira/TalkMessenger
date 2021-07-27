package br.com.talkmessenger.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

import androidx.viewpager2.adapter.FragmentStateAdapter
import br.com.talkmessenger.fragments.ChatsFragment
import br.com.talkmessenger.fragments.UsersFragment

class ViewPagerAdapter (fa: FragmentActivity): FragmentStateAdapter(fa){

    override fun getItemCount(): Int = 2
    override fun createFragment(position: Int): Fragment = when(position){
        0 -> ChatsFragment()
        else ->  UsersFragment()
    }

}