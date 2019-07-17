package com.afirez.app.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import com.afirez.app.LazyFragment
import com.afirez.app.R
import kotlinx.android.synthetic.main.activity_lazy_view_pager.*

class LazyViewPagerActivity : AppCompatActivity() {

    val fragments = arrayListOf<MyFragment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lazy_view_pager)

        for (i in 0..4) {
            fragments.add(MyFragment.newInstance("Lazy $i"))
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
