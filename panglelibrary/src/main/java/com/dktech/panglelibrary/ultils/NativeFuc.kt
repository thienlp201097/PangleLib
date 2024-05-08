package com.dktech.panglelibrary.ultils

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bytedance.sdk.openadsdk.api.nativeAd.PAGImageItem
import com.bytedance.sdk.openadsdk.api.nativeAd.PAGNativeAd
import com.bytedance.sdk.openadsdk.api.nativeAd.PAGVideoAdListener
import com.bytedance.sdk.openadsdk.api.nativeAd.PAGVideoMediaView
import com.dktech.panglelibrary.R


object NativeFuc {
    const val UNIFIED_MEDIUM = 0
    const val UNIFIED_SMALL = 1
    @SuppressLint("CutPasteId")
    fun populateAdView(mContext: Context, nativeAd : PAGNativeAd, nativeAdView : View) {
        val mTitle = nativeAdView.findViewById<View>(R.id.ad_title) as TextView
        val mDescription = nativeAdView.findViewById<View>(R.id.ad_desc) as TextView
        val mIcon = nativeAdView.findViewById<ImageView>(R.id.ad_icon)
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
    }
}