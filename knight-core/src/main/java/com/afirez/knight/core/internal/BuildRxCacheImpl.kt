package com.afirez.knight.core.internal

import android.content.Context
import com.afirez.knight.core.api.BuildRxCache
import io.rx_cache2.internal.RxCache

class BuildRxCacheImpl: BuildRxCache {
    override fun buildRxCache(context: Context, builder: RxCache.Builder) {
        builder.useExpiredDataIfLoaderNotAvailable(true)
    }
}