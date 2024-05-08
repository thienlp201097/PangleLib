package com.dktech.panglelibrary

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.bytedance.sdk.openadsdk.api.open.PAGAppOpenAd
import com.bytedance.sdk.openadsdk.api.open.PAGAppOpenAdInteractionListener
import com.bytedance.sdk.openadsdk.api.open.PAGAppOpenAdLoadListener
import com.bytedance.sdk.openadsdk.api.open.PAGAppOpenRequest
import com.dktech.panglelibrary.sdk.PangleUtils
import com.dktech.panglelibrary.ultils.RitConstants


/**
 * AppOpenAdManger
 */
class PAGAppOpenAdManager(private val myApplication: Application, val adsId: String) : LifecycleObserver,
    ActivityLifecycleCallbacks {
    private var appOpenAd: PAGAppOpenAd? = null

    private var currentActivity: Activity? = null

    private var mAdValidStartTime: Long = 0

    /**
     * Need to get the request advertisement callback in real time
     * （Example：Cold start）
     */
    interface RealTimeFetchListener {
        fun loadedSuccess()

        fun loadedFail()
    }

    /**
     * Need to get ad display and disappear callback
     * （Example：Cold start needs to perform operations such as jumping according to the ad has been displayed and disappeared）
     */
    interface ManagerOpenAdInteractionListener {
        fun onAdShow()

        fun onAdClose()
    }

    /**
     * Constructor
     */
    init {
        //By keeping track of the current activity, you have a context to use to show the ad. You now need to register this interface using the registerActivityLifecycleCallbacks Application method in your PAGAppOpenAdLoadManager constructor.
        myApplication.registerActivityLifecycleCallbacks(this)

        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    /**
     * Request an ad
     */
    fun fetchAd(realTimeFetchListener: RealTimeFetchListener?) {
        Log.d(TAG, "fetchAdNew")
        // Have unused ad, no need to fetch another.
        if (appOpenAd != null) {
            return
        }
        if (!PangleUtils.isNetworkConnected(myApplication) || !PangleUtils.isShowAds) {
            Log.e(TAG, "errorCode: No internet connection")
            return
        }
        var id = adsId
        if (PangleUtils.isTest){
            id = RitConstants.RIT_OPEN_VERTICAL
        }
        val request = PAGAppOpenRequest()
        request.timeout = LOAD_TIMEOUT
        PAGAppOpenAd.loadAd(
            id,
            request,
            object : PAGAppOpenAdLoadListener {
                override fun onError(code: Int, message: String) {
                    Log.e(TAG, "errorCode: $code errorMessage: $message")
                    realTimeFetchListener?.loadedFail()
                }

                override fun onAdLoaded(ad: PAGAppOpenAd) {
                    appOpenAd = ad
                    mAdValidStartTime = System.currentTimeMillis()
                    realTimeFetchListener?.loadedSuccess()
                }
            })
    }


    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
    }

    override fun onActivityStarted(activity: Activity) {
        currentActivity = activity
    }

    override fun onActivityResumed(activity: Activity) {
        currentActivity = activity
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
        currentActivity = null
    }


    /**
     * Show App Open Ad
     */
    fun showAdIfAvailable(managerOpenAdInteractionListener: ManagerOpenAdInteractionListener?) {
        if (mAdValidStartTime > 0 && ((System.currentTimeMillis() - mAdValidStartTime) > 24 * 60 * 60 * 1000)) {
            Log.d(TAG, "Advertising material has expired")
            return
        }
        if (!isShowingAd) {
            if (appOpenAd != null) {
                Log.d(TAG, "Will show ad.")
                appOpenAd!!.setAdInteractionListener(object : PAGAppOpenAdInteractionListener {
                    override fun onAdShowed() {
                        Log.d(TAG, "onAdShow")
                        isShowingAd = true

                        managerOpenAdInteractionListener?.onAdShow()
                    }

                    override fun onAdClicked() {
                        Log.d(TAG, "onAdClicked")
                    }

                    override fun onAdDismissed() {
                        Log.d(TAG, "onAdDismissed")
                        openAdClose(managerOpenAdInteractionListener)
                    }
                })
                appOpenAd!!.show(currentActivity)
                appOpenAd = null
            } else {
                Log.d(TAG, "No ad to show. to fetchAd")
                fetchAd(null)
            }
        } else {
            Log.d(
                TAG,
                "There is currently an ad display or The current scene does not want to show the open screen"
            )
        }
    }

    /**
     * Ad Dismiss control
     */
    private fun openAdClose(managerOpenAdInteractionListener: ManagerOpenAdInteractionListener?) {
        isShowingAd = false
        fetchAd(null)

        managerOpenAdInteractionListener?.onAdClose()
    }

    /**
     * LifecycleObserver methods
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        if (PangleUtils.isAdsShowing){
            Log.d(TAG, "isAdsShowing!!")
            return
        }
        Log.d(TAG, "onStart")
        showAdIfAvailable(null)
    }

    companion object {
        private const val TAG = "PAGAppOpenAdManager"

        private var isShowingAd = false

        private const val LOAD_TIMEOUT = 3000

        /**
         * There is currently an ad display, or The current scene does not want to show the open screen
         * @param isShowingNow not want to show the open screen
         */
        fun setIsShowingAd(isShowingNow: Boolean) {
            isShowingAd = isShowingNow
        }
    }
}
