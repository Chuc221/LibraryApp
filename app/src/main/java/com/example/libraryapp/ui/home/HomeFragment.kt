package com.example.libraryapp.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import com.example.libraryapp.R
import com.example.libraryapp.databinding.FragmentHomeBinding
import com.example.libraryapp.ui.home.book.BooksFragment
import com.example.libraryapp.ui.home.borrow.BorrowBookFragment
import com.example.libraryapp.ui.home.user.UserFragment

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigationView()
    }

    private fun navigationView() = with(binding) {
        viewPager.adapter = HomePagerAdapter(
            listOf(
                Pair(BooksFragment(), getString(R.string.book)),
                Pair(BorrowBookFragment(), getString(R.string.borrow_give)),
                Pair(UserFragment(), getString(R.string.info))
            ), childFragmentManager
        )

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int, positionOffset: Float, positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                bottomNav.menu.getItem(position).isChecked = true
            }

            override fun onPageScrollStateChanged(state: Int) {}

        })
        bottomNav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.mBook -> viewPager.currentItem = 0
                R.id.mBorrow -> viewPager.currentItem = 1
                R.id.mInfo -> viewPager.currentItem = 2
            }
            true
        }
    }
}