package com.afirez.app.ui

import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.afirez.app.CustomLayoutManager
import com.afirez.app.R
import kotlinx.android.synthetic.main.activity_custom_recycler_view.*
import timber.log.Timber

class CustomRecyclerViewActivity : AppCompatActivity() {

    lateinit var worker: HandlerThread
    lateinit var h: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_recycler_view)
        worker = HandlerThread("worker")
        worker.start()
        h = Handler(worker.looper)
        layoutManager = CustomLayoutManager()
        adapter = Adapter()
        rvCustomLayoutManager.apply {
            layoutManager = this@CustomRecyclerViewActivity.layoutManager
            adapter = this@CustomRecyclerViewActivity.adapter
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        worker.quit()
        h.removeCallbacksAndMessages(null)
    }

    override fun onResume() {
        super.onResume()
        h.postDelayed(object : Runnable {
            override fun run() {
                synchronized(layoutManager) {
                    if (times == 0) {
                        items.add(
                            Item(
                                img = R.mipmap.ic_launcher,
                                leftMargin = 5,
                                topMargin = 0,
                                scale = 1f
                            )
                        )
                    }
                    times += 1

                    val iterator = items.iterator()
                    while (iterator.hasNext()) {
                        val next = iterator.next()
                        if (next.removed) {
                            iterator.remove()
                        }
                    }

                    for (item in items) {
                        item.leftMargin += 5
                        item.scale = 1f
                    }

                    if (times.rem(60) == 0) {
                        if (items.size > 2) {
                            items.removeAt(2)
                        }
                        items.add(
                            Item(
                                img = R.mipmap.ic_launcher,
                                leftMargin = 5,
                                topMargin = 0,
                                scale = 1f
                            )
                        )
                    }
                    Timber.i("size: ${items.size}")
                    runOnUiThread {
                        rvCustomLayoutManager.adapter?.notifyDataSetChanged()
                    }
                    h.postDelayed(this, 80)
                }
            }
        }, 80)
    }

    var times: Int = 0

    val items: MutableList<Item> = mutableListOf()

    lateinit var layoutManager: CustomLayoutManager
    lateinit var adapter: Adapter

    data class Item(
        var img: Int,
        var leftMargin: Int,
        var topMargin: Int,
        var scale: Float,
        var removed: Boolean = false
    )

    inner class Holder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        val ivImg: ImageView = itemView.findViewById(R.id.ivImg)

        fun onBind() {
            val position = adapterPosition
            val item = items.get(position)
            ivImg.setImageResource(item.img)
            val params = itemView.layoutParams as androidx.recyclerview.widget.RecyclerView.LayoutParams
            params.leftMargin = item.leftMargin
            params.topMargin = item.topMargin
            ivImg.scaleX = item.scale
            ivImg.scaleY = item.scale
            Timber.i("onBind: $position")
        }

        fun onViewAttachedToWindow() {
            Timber.i("onViewAttachedToWindow: $adapterPosition")
        }

        fun onViewDetachedFromWindow() {
            Timber.i("onViewDetachedFromWindow: $adapterPosition")
        }

        fun onViewRecycled() {
            val position = adapterPosition
            if (position >= 0 && position < items.size) {
                items.get(position).removed = true

            }

            Timber.i("onViewRecycled: $position")
        }

    }

    inner class Adapter : androidx.recyclerview.widget.RecyclerView.Adapter<Holder>() {
        override fun onCreateViewHolder(parant: ViewGroup, viewType: Int): Holder {
            val inflater = LayoutInflater.from(this@CustomRecyclerViewActivity)
            val view = inflater.inflate(R.layout.item_green, parant, false)
            val holder = Holder(view)
            return holder
        }

        override fun getItemCount(): Int {
            return items.size
        }

        override fun onBindViewHolder(holder: Holder, position: Int) {
            holder.onBind()
        }

        override fun onViewRecycled(holder: Holder) {
            super.onViewRecycled(holder)
            holder.onViewRecycled()
        }

        override fun onViewAttachedToWindow(holder: Holder) {
            super.onViewAttachedToWindow(holder)
            holder.onViewAttachedToWindow()
        }

        override fun onViewDetachedFromWindow(holder: Holder) {
            super.onViewDetachedFromWindow(holder)
            holder.onViewDetachedFromWindow()
        }
    }
}
