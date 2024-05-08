package com.rikatech.panglelib

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bytedance.sdk.openadsdk.api.banner.PAGBannerSize
import com.dktech.panglelibrary.callback.PAGBannerCallback
import com.dktech.panglelibrary.callback.PAGInterCallback
import com.dktech.panglelibrary.callback.PAGNativeCallback
import com.dktech.panglelibrary.callback.PAGRewardCallback
import com.dktech.panglelibrary.holder.InterHolder
import com.dktech.panglelibrary.holder.InterRewardHolder
import com.dktech.panglelibrary.sdk.PangleUtils
import com.dktech.panglelibrary.ultils.NativeFuc
import com.rikatech.panglelib.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    val interHolder by lazy { InterHolder("980088188") }
    val interReward by lazy { InterRewardHolder("980088192") }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.showBanner.setOnClickListener {
            PangleUtils.loadBanner(this,"980088196",binding.frBanner,object : PAGBannerCallback{
                override fun onAdShowed() {
                    
                }

                override fun onAdClick() {
                    
                }

                override fun onAdFail(error: String?) {
                    
                }
            })
        }

        binding.showNative.setOnClickListener {
            PangleUtils.loadAndShowNative(this,"980088216",R.layout.ad_template_medium,binding.frNative,NativeFuc.UNIFIED_MEDIUM,object : PAGNativeCallback {
                override fun onAdShowed() {
                    
                }

                override fun onAdFail(error: String?) {
                    
                }
            })
        }

        binding.loadAndShowInter.setOnClickListener {
            PangleUtils.loadAndShowPAGInterstitial(this,interHolder, object : PAGInterCallback {
                override fun onEventClickAdClosed() {
                    nextActivity()
                }

                override fun onAdShowed() {
                    
                }

                override fun onAdLoaded() {
                    
                }

                override fun onAdFail(error: String?) {
                    nextActivity()
                }
            })
        }

        binding.loadAndShowReward.setOnClickListener {
            PangleUtils.loadAdPAGRewarded(this,interReward,object : PAGRewardCallback{
                override fun onEventClickAdClosed() {
                    nextActivity()
                }

                override fun onAdShowed() {
                    
                }

                override fun onAdLoaded() {
                    
                }

                override fun onAdEarnedReward() {
                    
                }

                override fun onAdEarnedRewardFailed() {
                    
                }

                override fun onAdFail(error: String?) {
                    nextActivity()
                }

            })
        }
    }

    fun nextActivity(){
        startActivity(Intent(this,OtherScreen::class.java))
    }
}