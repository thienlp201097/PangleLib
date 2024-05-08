package com.dktech.panglelibrary.sdk

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
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
import com.dktech.panglelibrary.holder.InterHolder
import com.dktech.panglelibrary.holder.InterRewardHolder
import com.dktech.panglelibrary.ultils.AdManagerHolder
import com.dktech.panglelibrary.ultils.NativeFuc

object PangleUtils {
    fun initSdk(context: Context,appKey : String) {
        AdManagerHolder.doInitNewApi(context,appKey)
    }

    fun loadBanner(codeId: String, bannerSize: PAGBannerSize, viewGroup: ViewGroup) {
        viewGroup.removeAllViews()
        val bannerRequest = PAGBannerRequest(bannerSize)
        //step2:request ad
        PAGBannerAd.loadAd(codeId, bannerRequest, object : PAGBannerAdLoadListener {
            override fun onError(code: Int, message: String) {
                viewGroup.removeAllViews()
            }

            override fun onAdLoaded(bannerAd: PAGBannerAd) {
                if (bannerAd == null) {
                    return
                }
                bindAdListener(bannerAd)
                if (bannerAd != null) {
                    //step3:add ad view to view container
                    viewGroup.addView(bannerAd.bannerView)
                }
            }
        })
    }

    private fun bindAdListener(ad: PAGBannerAd) {
        ad.setAdInteractionListener(object : PAGBannerAdInteractionListener {
            override fun onAdShowed() {
            }

            override fun onAdClicked() {
            }

            override fun onAdDismissed() {
            }
        })
    }

    fun loadAndShowNative(context: Context, id: String, layout: Int, viewGroup: ViewGroup) {
        val request = PAGNativeRequest()
        PAGNativeAd.loadAd(id, request, object : PAGNativeAdLoadListener {
            override fun onError(code: Int, message: String) {
            }

            override fun onAdLoaded(pagNativeAd: PAGNativeAd) {
                val nativeAdView = LayoutInflater.from(context).inflate(layout, null)
                NativeFuc.populateAdView(context, pagNativeAd, nativeAdView)
                viewGroup.removeAllViews()
                viewGroup.addView(nativeAdView)
            }
        })
    }

    fun loadAndShowPAGInterstitial(context: Context, interHolder: InterHolder) {
        PAGInterstitialAd.loadAd(interHolder.ads,
            PAGInterstitialRequest(),
            object : PAGInterstitialAdLoadListener {
                override fun onError(code: Int, message: String) {

                }

                override fun onAdLoaded(pagInterstitialAd: PAGInterstitialAd) {
                    interHolder.inter = pagInterstitialAd
                    showPAGInterstitial(context, interHolder)
                }
            })
    }

    fun showPAGInterstitial(context: Context, interHolder: InterHolder) {
        if (interHolder.inter != null) {
            interHolder.inter!!.setAdInteractionListener(object :
                PAGInterstitialAdInteractionListener {
                override fun onAdShowed() {

                }

                override fun onAdClicked() {

                }

                override fun onAdDismissed() {

                }
            })
            interHolder.inter!!.show(context as Activity)
            interHolder.inter = null
        }
    }


    fun loadAdPAGRewarded(context: Context, interHolder: InterRewardHolder) {
        PAGRewardedAd.loadAd(interHolder.ads,
            PAGRewardedRequest(),
            object : PAGRewardedAdLoadListener {
                override fun onError(code: Int, message: String) {

                }

                override fun onAdLoaded(ad: PAGRewardedAd) {
                    interHolder.inter = ad
                    showPAGRewardedAd(context, interHolder)
                }
            })
    }

    fun showPAGRewardedAd(context: Context, interHolder: InterRewardHolder) {
        if (interHolder.inter != null) {
            interHolder.inter!!.setAdInteractionListener(object : PAGRewardedAdInteractionListener {
                override fun onAdShowed() {

                }

                override fun onAdClicked() {

                }

                override fun onAdDismissed() {

                }

                override fun onUserEarnedReward(p0: PAGRewardItem?) {

                }

                override fun onUserEarnedRewardFail(p0: Int, p1: String?) {

                }
            })
            interHolder.inter!!.show(context as Activity)
            interHolder.inter = null
        }
    }

}