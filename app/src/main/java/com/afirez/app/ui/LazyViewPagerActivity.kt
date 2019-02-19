package com.afirez.app.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import com.afirez.app.LazyFragment
import com.afirez.app.R
import kotlinx.android.synthetic.main.activity_lazy_view_pager.*

class LazyViewPagerActivity : AppCompatActivity() {

    val fragments = arrayListOf<LazyFragment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lazy_view_pager)

        for (i in 0..4) {
            fragments.add(LazyFragment.newInstance("Lazy $i"))
        }
        pager.apply {
            adapter = object : FragmentStatePagerAdapter(supportFragmentManager) {
                override fun getItem(position: Int): Fragment {
                    return fragments.get(position)
                }

                override fun getCount(): Int {
                    return fragments.size
                }
            }
        }
    }
}
