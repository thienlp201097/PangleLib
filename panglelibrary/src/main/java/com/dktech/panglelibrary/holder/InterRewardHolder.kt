package com.dktech.panglelibrary.holder

import androidx.lifecycle.MutableLiveData
import com.bytedance.sdk.openadsdk.api.interstitial.PAGInterstitialAd
import com.bytedance.sdk.openadsdk.api.reward.PAGRewardedAd

open class InterRewardHolder(var ads: String) {
    var inter: PAGRewardedAd? = null
    val mutable: MutableLiveData<PAGRewardedAd> = MutableLiveData(null)
    var check = false
}