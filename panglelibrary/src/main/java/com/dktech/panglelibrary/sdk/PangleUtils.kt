package com.dktech.panglelibrary.sdk

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.airbnb.lottie.LottieAnimationView
import com.bytedance.sdk.openadsdk.api.banner.PAGBannerAd
import com.bytedance.sdk.openadsdk.api.banner.PAGBannerAdInteractionListener
import com.bytedance.sdk.openadsdk.api.banner.PAGBannerAdLoadListener
import com.bytedance.sdk.openadsdk.api.banner.PAGBannerRequest
import com.bytedance.sdk.openadsdk.api.banner.PAGBannerSize
import com.bytedance.sdk.openadsdk.api.interstitial.PAGInterstitialAd
import com.bytedance.sdk.openadsdk.api.interstitial.PAGInterstitialAdInteractionListener
import com.bytedance.sdk.openadsdk.api.interstitial.PAGInterstitialAdLoadListener
import com.bytedance.sdk.openadsdk.api.interstitial.PAGInterstitialRequest
import com.bytedance.sdk.openadsdk.api.nativeAd.PAGNativeAd
import com.bytedance.sdk.openadsdk.api.nativeAd.PAGNativeAdLoadListener
import com.bytedance.sdk.openadsdk.api.nativeAd.PAGNativeRequest
import com.bytedance.sdk.openadsdk.api.reward.PAGRewardItem
import com.bytedance.sdk.openadsdk.api.reward.PAGRewardedAd
import com.bytedance.sdk.openadsdk.api.reward.PAGRewardedAdInteractionListener
import com.bytedance.sdk.openadsdk.api.reward.PAGRewardedAdLoadListener
import com.bytedance.sdk.openadsdk.api.reward.PAGRewardedRequest
import com.dktech.panglelibrary.R
import com.dktech.panglelibrary.callback.PAGBannerCallback
import com.dktech.panglelibrary.callback.PAGInterCallback
import com.dktech.panglelibrary.callback.PAGRewardCallback
import com.dktech.panglelibrary.callback.PAGNativeCallback
import com.dktech.panglelibrary.holder.InterHolder
import com.dktech.panglelibrary.holder.InterRewardHolder
import com.dktech.panglelibrary.ultils.AdManagerHolder
import com.dktech.panglelibrary.ultils.NativeFuc
import com.dktech.panglelibrary.ultils.RitConstants
import com.facebook.shimmer.ShimmerFrameLayout

object PangleUtils {
    var isTest = false
    var isShowAds = false
    var isAdsShowing = false
    var dialogFullScreen: Dialog? = null
    fun initSdk(context: Context, appKey: String, testMod: Boolean, isShowAds : Boolean) {
        isTest = testMod
        this.isShowAds = isShowAds
        var appKey = appKey
        if (isTest) {
            appKey = RitConstants.AD_APP_ID
        }
        AdManagerHolder.doInitNewApi(context,appKey)
    }

    @JvmStatic
    fun isNetworkConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnected
    }

    fun loadBanner(
        context: Context,
        codeId: String,
        viewGroup: ViewGroup,
        callback: PAGBannerCallback
    ) {
        if (!isNetworkConnected(context) || !isShowAds) {
            callback.onAdFail("No internet connection")
            return
        }
        context as Activity
        var codeId = codeId
        if (isTest) {
            codeId = RitConstants.RIT_BANNER_320X50
        }
        val tagView = context.layoutInflater.inflate(R.layout.layoutbanner_loading, null, false)
        viewGroup.removeAllViews()
        viewGroup.addView(tagView, 0)
        val shimmerFrameLayout: ShimmerFrameLayout = tagView.findViewById(R.id.shimmer_view_container)
        shimmerFrameLayout.startShimmer()
        val bannerRequest = PAGBannerRequest(PAGBannerSize(728, 50))
        //step2:request ad
        PAGBannerAd.loadAd(codeId, bannerRequest, object : PAGBannerAdLoadListener {
            override fun onError(code: Int, message: String) {
                callback.onAdFail(message)
                shimmerFrameLayout.stopShimmer()
                viewGroup.removeAllViews()
            }

            override fun onAdLoaded(bannerAd: PAGBannerAd) {
                viewGroup.removeView(tagView)
                if (bannerAd == null) {
                    return
                }
                bannerAd.setAdInteractionListener(object : PAGBannerAdInteractionListener {
                    override fun onAdShowed() {
                        callback.onAdShowed()
                        shimmerFrameLayout.stopShimmer()
                        shimmerFrameLayout.hideShimmer()
                    }

                    override fun onAdClicked() {
                        callback.onAdClick()
                    }

                    override fun onAdDismissed() {

                    }
                })
                if (bannerAd != null) {
                    //step3:add ad view to view container
                    viewGroup.addView(bannerAd.bannerView)
                }
            }
        })
    }

    fun loadAndShowNative(context: Context, id: String, layout: Int, viewGroup: ViewGroup,size : Int,callback: PAGNativeCallback) {
        if (!isNetworkConnected(context) || !isShowAds) {
            callback.onAdFail("No internet connection")
            return
        }
        context as Activity
        var codeId = id
        if (isTest) {
            codeId = RitConstants.RIT_NATIVE
        }
        val tagView: View = if (size == NativeFuc.UNIFIED_MEDIUM) {
            context.layoutInflater.inflate(R.layout.layoutnative_loading_medium, null, false)
        } else {
            context.layoutInflater.inflate(R.layout.layoutnative_loading_small, null, false)
        }
        viewGroup.removeAllViews()
        viewGroup.addView(tagView)
        val shimmerFrameLayout: ShimmerFrameLayout = tagView.findViewById(R.id.shimmer_view_container)
        shimmerFrameLayout.startShimmer()
        val request = PAGNativeRequest()
        PAGNativeAd.loadAd(codeId, request, object : PAGNativeAdLoadListener {
            override fun onError(code: Int, message: String) {
                callback.onAdFail(message)
                shimmerFrameLayout.stopShimmer()
                shimmerFrameLayout.hideShimmer()
            }

            override fun onAdLoaded(pagNativeAd: PAGNativeAd) {
                callback.onAdShowed()
                val nativeAdView = LayoutInflater.from(context).inflate(layout, null)
                NativeFuc.populateAdView(context, pagNativeAd, nativeAdView)
                shimmerFrameLayout.stopShimmer()
                shimmerFrameLayout.hideShimmer()
                viewGroup.removeAllViews()
                viewGroup.addView(nativeAdView)
            }
        })
    }

    fun loadAndShowPAGInterstitial(
        context: Context,
        interHolder: InterHolder,
        callback: PAGInterCallback
    ) {
        if (!isNetworkConnected(context) || !isShowAds) {
            callback.onAdFail("No internet connection")
            return
        }
        var codeId = interHolder.ads
        if (isTest) {
            codeId = RitConstants.RIT_INTER_VERTICAL
        }

        dialogLoading(context as Activity)

        PAGInterstitialAd.loadAd(
            codeId,
            PAGInterstitialRequest(),
            object : PAGInterstitialAdLoadListener {
                override fun onError(code: Int, message: String) {
                    dismissAdDialog()
                    callback.onAdFail(message)
                }

                override fun onAdLoaded(pagInterstitialAd: PAGInterstitialAd) {
                    interHolder.inter = pagInterstitialAd
                    callback.onAdLoaded()
                    showPAGInterstitial(context, interHolder, callback)
                }
            })
    }

    fun showPAGInterstitial(
        context: Context,
        interHolder: InterHolder,
        callback: PAGInterCallback
    ) {
        if (interHolder.inter != null) {
            Handler(Looper.getMainLooper()).postDelayed({
                interHolder.inter!!.setAdInteractionListener(object :
                    PAGInterstitialAdInteractionListener {
                    override fun onAdShowed() {
                        dismissAdDialog()
                        callback.onAdShowed()
                    }

                    override fun onAdClicked() {

                    }

                    override fun onAdDismissed() {
                        callback.onEventClickAdClosed()
                    }
                })
                interHolder.inter!!.show(context as Activity)
                interHolder.inter = null
            }, 700)
        }else{
            callback.onAdFail("Ads null")
        }
    }


    fun loadAdPAGRewarded(context: Context, interHolder: InterRewardHolder, callback: PAGRewardCallback) {
        if (!isNetworkConnected(context) || !isShowAds) {
            callback.onAdFail("No internet connection")
            return
        }
        var codeId = interHolder.ads
        if (isTest) {
            codeId = RitConstants.RIT_REWARDED_VERTICAL
        }

        dialogLoading(context as Activity)

        PAGRewardedAd.loadAd(
            codeId,
            PAGRewardedRequest(),
            object : PAGRewardedAdLoadListener {
                override fun onError(code: Int, message: String) {
                    dismissAdDialog()
                    callback.onAdFail(message)
                }

                override fun onAdLoaded(ad: PAGRewardedAd) {
                    callback.onAdLoaded()
                    interHolder.inter = ad
                    showPAGRewardedAd(context, interHolder,callback)
                }
            })
    }

    fun showPAGRewardedAd(context: Context, interHolder: InterRewardHolder,callback : PAGRewardCallback) {
        if (interHolder.inter != null) {
            Handler(Looper.getMainLooper()).postDelayed({
                interHolder.inter!!.setAdInteractionListener(object : PAGRewardedAdInteractionListener {
                    override fun onAdShowed() {
                        dismissAdDialog()
                        callback.onAdShowed()
                    }

                    override fun onAdClicked() {

                    }

                    override fun onAdDismissed() {
                        callback.onEventClickAdClosed()
                    }

                    override fun onUserEarnedReward(p0: PAGRewardItem?) {
                        callback.onAdEarnedReward()
                    }

                    override fun onUserEarnedRewardFail(p0: Int, p1: String?) {
                        callback.onAdEarnedRewardFailed()
                    }
                })
                interHolder.inter!!.show(context as Activity)
                interHolder.inter = null
            },700)
        }else{
            callback.onAdFail("Ads null")
        }
    }

    fun dialogLoading(context: Activity) {
        isAdsShowing = true
        dialogFullScreen = Dialog(context)
        dialogFullScreen?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogFullScreen?.setContentView(R.layout.dialog_full_screen)
        dialogFullScreen?.setCancelable(false)
        dialogFullScreen?.window!!.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        dialogFullScreen?.window!!.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        val img = dialogFullScreen?.findViewById<LottieAnimationView>(R.id.imageView3)
        img?.setAnimation(R.raw.gifloading)
        try {
            if (!context.isFinishing && dialogFullScreen != null && dialogFullScreen?.isShowing == false) {
                dialogFullScreen?.show()
            }
        } catch (ignored: Exception) {
        }
    }

    @JvmStatic
    fun dismissAdDialog() {
        isAdsShowing = false
        try {
            if (dialogFullScreen != null && dialogFullScreen?.isShowing == true) {
                dialogFullScreen?.dismiss()
            }
        } catch (_: Exception) {

        }
    }

}