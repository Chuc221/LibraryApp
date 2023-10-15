package com.example.libraryapp.ui.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class HomePagerAdapter(
    private val listFragment: List<Pair<Fragment, String>>, fragmentManager: FragmentManager
) : FragmentStatePagerAdapter(fragmentManager) {

    override fun getCount(): Int {
        return listFragment.size
    }

    override fun getItem(position: Int): Fragment {
        return listFragment[position].first
    }

    override fun getPageTitle(position: Int): CharSequence {
        return listFragment[position].second
    }
}