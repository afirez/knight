package com.afirez.knight.core.internal

import android.content.Context
import com.afirez.knight.core.api.BuildRetrofit
import com.afirez.knight.core.appKodein
import com.google.gson.Gson
import okhttp3.OkHttpClient
import org.kodein.di.generic.instance
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class BuildRetrofitImpl: BuildRetrofit {

    override fun buildRetrofit(context: Context, builder: Retrofit.Builder) {
        val kodein = context.appKodein()
        val baseUrl:String by kodein.instance(tag = "baseUrl")
        val gson:Gson by kodein.instance()
        val client:OkHttpClient by kodein.instance()
        builder.baseUrl(baseUrl)
            .client(client)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
    }

}
