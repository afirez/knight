package com.afirez.app.weex.internal.adapter

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.taobao.weex.adapter.IWXImgLoaderAdapter
import com.taobao.weex.common.WXImageStrategy
import com.taobao.weex.dom.WXImageQuality

class ImageLoaderAdapter : IWXImgLoaderAdapter {
    override fun setImage(
        url: String?,
        view: ImageView?,
        quality: WXImageQuality?,
        strategy: WXImageStrategy?
    ) {
        if (view != null) {
            Glide.with(view)
                .load(url)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        strategy?.imageListener?.onImageFinish(url, view, false, null)
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        strategy?.imageListener?.onImageFinish(url, view, true, null)
                        return false
                    }

                })
                .into(view)
        }
    }
}