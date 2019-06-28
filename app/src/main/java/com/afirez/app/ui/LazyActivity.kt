package com.afirez.app.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.afirez.app.LazyFragment
import com.afirez.app.R
import kotlinx.android.synthetic.main.activity_lazy.*

class LazyActivity : AppCompatActivity() {

    val fragments = arrayListOf<LazyFragment>()

    var i: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lazy)
        for (i in 0..4) {
            fragments.add(LazyFragment.newInstance("Lazy $i"))
        }
        showFragment(0, null)

        btnSwitchFragment.setOnClickListener {
            switchFragment()
        }
    }

    private fun switchFragment() {
        val last = i
        val toHideFragment: LazyFragment?
        if (last >= 0 && last <= 4) {
            toHideFragment = fragments.get(last)
        } else {
            toHideFragment = null
        }

        i++
        if (i > 4) {
            i = 0
        }
        showFragment(i, toHideFragment)
    }

    private fun showFragment(i: Int, toHideFragment: LazyFragment?) {
        val fm = supportFragmentManager
        val tag = "${LazyFragment::class.java.simpleName}@$i"
        val fragment = fm.findFragmentByTag(tag)
        val transaction = fm.beginTransaction()
        if (toHideFragment != null) {
            transaction.hide(toHideFragment)
        }
        if (fragment != null) {
            transaction.show(fragment)
        } else {
            transaction.add(R.id.flContent, fragments.get(i), tag)
        }
        transaction.commitNowAllowingStateLoss()
    }
}
