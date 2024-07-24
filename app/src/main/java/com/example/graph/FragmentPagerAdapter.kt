package com.example.graph

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.graph.RecentLinksFragment
import com.example.graph.TopLinksFragment

class FragmentPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> RecentLinksFragment()
            1 -> TopLinksFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}
