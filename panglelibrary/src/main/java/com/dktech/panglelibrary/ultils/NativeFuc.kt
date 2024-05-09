package com.dktech.panglelibrary.ultils

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bytedance.sdk.openadsdk.api.nativeAd.PAGImageItem
import com.bytedance.sdk.openadsdk.api.nativeAd.PAGNativeAd
import com.bytedance.sdk.openadsdk.api.nativeAd.PAGNativeAdInteractionListener
import com.bytedance.sdk.openadsdk.api.nativeAd.PAGVideoAdListener
import com.bytedance.sdk.openadsdk.api.nativeAd.PAGVideoMediaView
import com.dktech.panglelibrary.R
import com.dktech.panglelibrary.ultils.AdManagerHolder.TAG


object NativeFuc {
    const val UNIFIED_MEDIUM = 0
    const val UNIFIED_SMALL = 1
    @SuppressLint("CutPasteId")
    fun populateAdView(mContext: Context, nativeAd : PAGNativeAd, nativeAdView : View) {
        val mTitle = nativeAdView.findViewById<View>(R.id.ad_title) as TextView
        val mDescription = nativeAdView.findViewById<View>(R.id.ad_desc) as TextView
        val mIcon = nativeAdView.findViewById<ImageView>(R.id.ad_icon)
        val mDislikeView = nativeAdView.findViewById<ImageView>(R.id.dislike_btn)
        val mCreativeButton = nativeAdView.findViewById<View>(R.id.creative_btn) as Button
        val mAdLogoView = nativeAdView.findViewById<ImageView>(R.id.ad_icon)
        val mImageOrVideoView = nativeAdView.findViewById<View>(R.id.ad_video) as FrameLayout
        val adData = nativeAd.nativeAdData
        //title
        mTitle.text = adData.title
        //description
        mDescription.text = adData.description
        //icon
        val icon: PAGImageItem = adData.icon
        if (icon.imageUrl != null) {
            Glide.with(mContext).load(icon.imageUrl).into(mIcon)
        }
        //set btn text
        mCreativeButton.text = if (TextUtils.isEmpty(adData.buttonText)) mContext.getString(R.string.tt_native_banner_download) else adData.buttonText
        //get ad view
        val video: View? = adData.mediaView
        if (video is PAGVideoMediaView) {
            video.setVideoAdListener(object : PAGVideoAdListener {
                override fun onVideoAdPlay() {

                }

                override fun onVideoAdPaused() {

                }

                override fun onVideoAdComplete() {

                }

                override fun onVideoError() {

                }
            })
        }
        if (video != null) {
            if (video.parent == null) {
                mImageOrVideoView.addView(video)
            }
        }else{
            Glide.with(mContext).load(adData.adLogoView).into(mAdLogoView)
        }
        //notice! This involves advertising billing and must be called correctly. convertView must use ViewGroup.
        //the views that can be clicked
        val clickViewList= ArrayList<View>()
        clickViewList.add(nativeAdView)
        clickViewList.add(mImageOrVideoView)
        clickViewList.add(mAdLogoView)
        val creativeViewList = ArrayList<View>()
        creativeViewList.add(mCreativeButton)
        nativeAd.registerViewForInteraction(
            nativeAdView as ViewGroup,
            clickViewList,
            creativeViewList,
            mDislikeView,
            object : PAGNativeAdInteractionListener {
                override fun onAdShowed() {
                    if (adData != null) {
                        Log.e(
                            TAG,
                            "ad title:" + adData.title + ",onAdShowed"
                        )
                    }
                }

                override fun onAdClicked() {
                    if (adData != null) {
                        Log.e(
                            TAG,
                            "ad title:" + adData.title + ",onAdClicked"
                        )
                    }
                }

                /**
                 * click dislike button ，remove ad
                 */
                override fun onAdDismissed() {
                    if (adData != null) {
                        Log.e(
                            TAG,
                            "ad title:" + adData.title + ",onAdDismissed"
                        )
                    }
                }
            })
    }
}