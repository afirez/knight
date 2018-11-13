package com.afirez.knight.core.internal

import android.content.Context
import com.github.moduth.blockcanary.BlockCanaryContext
import com.github.moduth.blockcanary.internal.BlockInfo
import java.io.File

class BlockCanaryContextImpl : BlockCanaryContext() {

    override fun provideContext(): Context {
        return super.provideContext()
    }

    override fun provideQualifier(): String {
        //version + flavor
        return super.provideQualifier()
    }

    override fun provideUid(): String {
        //user id
        return super.provideUid()
    }

    override fun provideNetworkType(): String {
        // 2G, 3G, 4G, wifi
        return super.provideNetworkType()
    }

    override fun provideMonitorDuration(): Int {
        // stop after duration
        return super.provideMonitorDuration()
    }

    override fun provideBlockThreshold(): Int {
        //1000
        return 600
        //            return super.provideBlockThreshold();
    }

    override fun provideDumpInterval(): Int {
        //1000
        return super.provideDumpInterval()
    }

    override fun providePath(): String {
        return super.providePath()
    }

    override fun displayNotification(): Boolean {
        return super.displayNotification()
    }

    override fun zip(src: Array<File>?, dest: File?): Boolean {
        return super.zip(src, dest)
    }

    override fun upload(zippedFile: File?) {
        super.upload(zippedFile)
    }

    override fun concernPackages(): List<String> {
        return super.concernPackages()
    }

    override fun filterNonConcernStack(): Boolean {
        return super.filterNonConcernStack()
    }

    override fun provideWhiteList(): List<String> {
        return super.provideWhiteList()
    }

    override fun deleteFilesInWhiteList(): Boolean {
        return super.deleteFilesInWhiteList()
    }

    override fun onBlock(context: Context?, blockInfo: BlockInfo?) {
        super.onBlock(context, blockInfo)
    }

    override fun stopWhenDebugging(): Boolean {
        return super.stopWhenDebugging()
    }
}