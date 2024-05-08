package com.dktech.panglelibrary.ultils

import android.content.Context
import android.util.Log
import com.bytedance.sdk.openadsdk.api.init.PAGConfig
import com.bytedance.sdk.openadsdk.api.init.PAGSdk
import com.bytedance.sdk.openadsdk.api.init.PAGSdk.PAGInitCallback

/**
 * You can use a singleton to save the TTFAdManager instance, and call it when you need to initialize sdk
 */
object AdManagerHolder {
    var sInit: Boolean = false

    const val TAG = "AdManagerHolder"

    private var pangleSdkHasInitSuccess: PangleSdkHasInitSuccess? = null

    //step1: Initialize sdk
    fun doInitNewApi(context: Context?,appId : String) {
        if (!sInit) {
            val pAGInitConfig = buildNewConfig(appId)
            PAGSdk.init(context, pAGInitConfig, object : PAGInitCallback {
                override fun success() {
                    sInit = true
                    Log.i(TAG, "PAGInitCallback new api init success: ")
                    if (pangleSdkHasInitSuccess != null) {
                        pangleSdkHasInitSuccess!!.initSuccess()
                        pangleSdkHasInitSuccess = null
                    }
                }

                override fun fail(code: Int, msg: String) {
                    if (pangleSdkHasInitSuccess != null) {
                        pangleSdkHasInitSuccess!!.initFail()
                        pangleSdkHasInitSuccess = null
                    }
                    Log.i(TAG, "PAGInitCallback new api init fail: $code")
                }
            })
        }
    }

    private fun buildNewConfig(appId : String): PAGConfig {
        return PAGConfig.Builder()
            .appId(appId)
            .debugLog(true)
            .supportMultiProcess(false)
            .build()
    }

    fun setPangleSdkHasInitSuccess(sdkHasInitSuccess: PangleSdkHasInitSuccess?) {
        pangleSdkHasInitSuccess = sdkHasInitSuccess
    }

    interface PangleSdkHasInitSuccess {
        fun initSuccess()
        fun initFail()
    }
}
