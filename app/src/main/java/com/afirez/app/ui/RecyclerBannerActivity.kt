package com.afirez.app.ui

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.afirez.app.R
import com.afirez.app.ui.banner.BannerAdapterHelper
import com.afirez.app.ui.banner.BannerScaleHelper
import com.leochuan.ScaleLayoutManager
import com.yinglan.shadowimageview.ShadowImageView
import kotlinx.android.synthetic.main.activity_recycler_banner.*
import java.util.*

class RecyclerBannerActivity : AppCompatActivity() {

    lateinit var lm: RecyclerView.LayoutManager
    lateinit var bannerScaleHelper: BannerScaleHelper
    lateinit var theBannerAdapter: BannerAdapter

    var curPosition = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_banner)

        lm = LinearLayoutManager(this@RecyclerBannerActivity).apply {
            orientation = LinearLayoutManager.HORIZONTAL
        }

//        lm = ScaleLayoutManager.Builder(this, 0)
//            .setMinScale(1f)
//            .setMoveSpeed(0.1f)
//            .build()
//        lm.setItemSpace(0)

//        CenterSnapHelper().attachToRecyclerView(rvBanner)
//        PagerSnapHelper().attachToRecyclerView(rvBanner)

        theBannerAdapter = BannerAdapter()


        rvBanner.apply {
            layoutManager = lm
            adapter = theBannerAdapter
        }

        bannerScaleHelper = BannerScaleHelper()
        bannerAdapterHelper.pagePadding = dp(10f).toInt()
        bannerAdapterHelper.showLeftCardWidth = dp(20f).toInt()
        bannerScaleHelper.setPagePadding(dp(10f).toInt())
        bannerScaleHelper.setShowLeftCardWidth(dp(20f).toInt())
        bannerScaleHelper.setFirstItemPosition(1000)
        bannerScaleHelper.isPagerLike = true
        bannerScaleHelper.attachToRecyclerView(rvBanner)
    }

    val itmes = ArrayList<Int>().apply {
        add(R.drawable.ic_mask)
        add(R.drawable.ic_mask)
        add(R.drawable.ic_mask)
        add(R.drawable.ic_mask)
    }

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        //        val ivImage = itemView.findViewById<ImageView>(R.id.ivImg)
        val slShadow = itemView.findViewById<ShadowImageView>(R.id.slShadow)

        fun onBind(position: Int) {
//            ivImage.setImageResource(itmes.get(position))
            val realPosition = position.rem(itmes.size)
            slShadow.setImageResource(itmes.get(realPosition))

            if (curPosition >= 0 && curPosition == position) {
//                slShadow.setImageShadowColor(Color.parseColor("#29666666"))
                slShadow.setImageShadowColor(Color.parseColor("#00ff00"))
                slShadow.postInvalidate()
            } else {
//                slShadow.setImageShadowColor(Color.parseColor("#29666666"))
                slShadow.setImageShadowColor(Color.parseColor("#00ff00"))
                slShadow.postInvalidate()
            }
        }
    }

    val bannerAdapterHelper: BannerAdapterHelper = BannerAdapterHelper()

    inner class BannerAdapter : RecyclerView.Adapter<VH>() {

        override fun onCreateViewHolder(parent: ViewGroup, type: Int): VH {
            val context = parent.context
            val inflater = LayoutInflater.from(context)
            val view = inflater.inflate(R.layout.item_recycler_banner, parent, false)
            bannerAdapterHelper.onCreateViewHolder(parent, view)
            return VH(view)
        }

        override fun getItemCount(): Int {
            return Integer.MAX_VALUE
        }

        override fun onBindViewHolder(vh: VH, position: Int) {
            bannerAdapterHelper.onBindViewHolder(vh.itemView, position, itemCount)
            vh.onBind(position)
        }
    }

    private fun dp(dpValue: Float): Float {
        val dm = resources.displayMetrics
        val scale = dm.density
        return dpValue * scale + 0.5f
    }
}
