package com.rikatech.panglelib

import android.app.Application
import android.util.Log
import com.bytedance.sdk.openadsdk.api.init.PAGSdk
import com.bytedance.sdk.openadsdk.api.init.PAGSdk.PAGInitCallback
import com.dktech.panglelibrary.PAGAppOpenAdManager
import com.dktech.panglelibrary.sdk.PangleUtils

class MyApplication : Application() {
    private val TAG = "==PangleSdk=="
    private var mPAGAppOpenAdManager: PAGAppOpenAdManager? = null

    override fun onCreate() {
        super.onCreate()
        PAGSdk.addPAGInitCallback(object : PAGInitCallback {
            override fun success() {
                Log.i(TAG,
                    "PAGInitCallback success: addPAGInitCallback"
                )
            }

            override fun fail(code: Int, msg: String) {
                Log.i(
                    TAG,
                    "PAGInitCallback fail: addPAGInitCallback"
                )
            }
        })
        PangleUtils.initSdk(this,"8025677")
        //app open ad
        mPAGAppOpenAdManager = PAGAppOpenAdManager(this)
    }



    fun fetchAd(realTimeFetchListener: PAGAppOpenAdManager.RealTimeFetchListener?) {
        mPAGAppOpenAdManager?.fetchAd(realTimeFetchListener)
    }

    /**
     * Shows an app open ad.
     */
    fun showAdIfAvailable(managerOpenAdInteractionListener: PAGAppOpenAdManager.ManagerOpenAdInteractionListener?) {
        mPAGAppOpenAdManager?.showAdIfAvailable(managerOpenAdInteractionListener)
    }
}