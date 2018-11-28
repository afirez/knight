package com.afirez.app.weex

import android.app.Application
import android.content.Context
import com.afirez.app.weex.internal.adapter.ImageLoaderAdapter
import com.afirez.knight.core.api.AppLike
import com.taobao.weex.WXSDKEngine
import com.taobao.weex.InitConfig



class AppWeex: AppLike {
    override fun attachBaseContext(app: Application, base: Context?) {

    }

    override fun onCreate(app: Application) {
        val config = InitConfig.Builder()
            .setImgAdapter(ImageLoaderAdapter())
            .build()
        WXSDKEngine.initialize(app, config)
    }
}