package com.dktech.panglelibrary.holder

import androidx.lifecycle.MutableLiveData
import com.bytedance.sdk.openadsdk.api.interstitial.PAGInterstitialAd

open class InterHolder(var ads: String) {
    var inter: PAGInterstitialAd? = null
    val mutable: MutableLiveData<PAGInterstitialAd> = MutableLiveData(null)
    var check = false
}