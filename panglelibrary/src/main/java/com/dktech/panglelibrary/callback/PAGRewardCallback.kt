package com.dktech.panglelibrary.callback

interface PAGRewardCallback {
    fun onEventClickAdClosed()
    fun onAdShowed()
    fun onAdLoaded()
    fun onAdEarnedReward()
    fun onAdEarnedRewardFailed()
    fun onAdFail(error: String?)
}