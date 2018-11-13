package com.afirez.knight.core.api

import android.content.Context
import io.rx_cache2.internal.RxCache

interface BuildRxCache {
    fun buildRxCache(context: Context, builder: RxCache.Builder)
}
