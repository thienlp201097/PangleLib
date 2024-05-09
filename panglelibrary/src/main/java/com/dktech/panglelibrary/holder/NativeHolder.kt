package com.dktech.panglelibrary.holder

import androidx.lifecycle.MutableLiveData
import com.bytedance.sdk.openadsdk.api.nativeAd.PAGNativeAd

open class NativeHolder(var ads: String){
    var nativeAd : PAGNativeAd?= null
    var isLoad = false
    var native_mutable: MutableLiveData<PAGNativeAd> = MutableLiveData()
}