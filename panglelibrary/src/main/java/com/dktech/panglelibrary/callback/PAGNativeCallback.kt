package com.dktech.panglelibrary.callback

interface PAGNativeCallback {
    fun onAdShowed()
    fun onAdFail(error: String?)
}