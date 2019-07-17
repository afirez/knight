package com.afirez.app.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.afirez.app.LazyFragment
import com.afirez.app.R
import kotlinx.android.synthetic.main.activity_lazy.*

class LazyActivity : AppCompatActivity() {

    val fragments = arrayListOf<MyFragment>()

    var i: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lazy)
        for (i in 0..4) {
            fragments.add(MyFragment.newInstance("Lazy $i"))
        }
        showFragment(0, null)

        btnSwitchFragment.setOnClickListener {
            switchFragment()
        }
    }

    private fun switchFragment() {
        val last = i
        val toHideFragment: MyFragment?
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

    private fun showFragment(i: Int, toHideFragment: MyFragment?) {
        val fm = supportFragmentManager
        val tag = "${MyFragment::class.java.simpleName}@$i"
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

    class MyFragment : LazyFragment() {

        companion object {
            fun newInstance(name: String): MyFragment {
                val fragment = MyFragment()
                val args = Bundle().apply {
                    putString("name", name)
                }
                fragment.arguments = args
                return fragment
            }
        }

        var name_: String = ""

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            name_ = arguments?.getString("name", "") ?: ""
        }

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            val view = TextView(context)
            view.text = name_
            return view
        }

    }
}
