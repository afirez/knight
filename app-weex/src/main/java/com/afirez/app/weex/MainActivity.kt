package com.afirez.app.weex

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import com.taobao.weex.IWXRenderListener
import com.taobao.weex.WXSDKInstance
import com.taobao.weex.common.WXRenderStrategy
import com.taobao.weex.utils.WXFileUtils


class MainActivity : AppCompatActivity() {

    var mWXSDKInstance: WXSDKInstance? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.wx_activity_main)

        mWXSDKInstance =  WXSDKInstance(this)
        mWXSDKInstance?.registerRenderListener(renderListener)
        /**
         * WXSample 可以替换成自定义的字符串，针对埋点有效。
         * template 是.we transform 后的 js文件。
         * option 可以为空，或者通过option传入 js需要的参数。例如bundle js的地址等。
         * jsonInitData 可以为空。
         */
        mWXSDKInstance?.render(
            "WXSample",
            WXFileUtils.loadAsset("index.js", this),
            null,
            null,
            WXRenderStrategy.APPEND_ASYNC
        )
    }

    val renderListener = object : IWXRenderListener {
        override fun onRefreshSuccess(instance: WXSDKInstance?, width: Int, height: Int) {

        }

        override fun onException(instance: WXSDKInstance?, errCode: String?, msg: String?) {

        }

        override fun onViewCreated(instance: WXSDKInstance?, view: View?) {
            setContentView(view)
        }

        override fun onRenderSuccess(instance: WXSDKInstance?, width: Int, height: Int) {

        }

    }

    override fun onResume() {
        super.onResume()
        if (mWXSDKInstance != null) {
            mWXSDKInstance?.onActivityResume()
        }
    }

    override fun onPause() {
        super.onPause()
        if (mWXSDKInstance != null) {
            mWXSDKInstance?.onActivityPause()
        }
    }

    override fun onStop() {
        super.onStop()
        if (mWXSDKInstance != null) {
            mWXSDKInstance?.onActivityStop()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mWXSDKInstance != null) {
            mWXSDKInstance?.onActivityDestroy()
        }
    }

}
