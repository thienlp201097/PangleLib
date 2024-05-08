package com.dktech.panglelibrary.callback

interface PAGInterCallback {
    fun onEventClickAdClosed()
    fun onAdShowed()
    fun onAdLoaded()
    fun onAdFail(error: String?)
}