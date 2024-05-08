package com.dktech.panglelibrary.callback

interface PAGBannerCallback {
    fun onAdShowed()
    fun onAdClick()
    fun onAdFail(error: String?)
}