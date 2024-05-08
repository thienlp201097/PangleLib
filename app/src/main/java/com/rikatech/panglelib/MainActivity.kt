package com.rikatech.panglelib

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bytedance.sdk.openadsdk.api.banner.PAGBannerSize
import com.dktech.panglelibrary.holder.InterHolder
import com.dktech.panglelibrary.holder.InterRewardHolder
import com.dktech.panglelibrary.sdk.PangleUtils
import com.rikatech.panglelib.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    val interHolder by lazy { InterHolder("980088188") }
    val interReward by lazy { InterRewardHolder("980088192") }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.showBanner.setOnClickListener {
            PangleUtils.loadBanner("980088196", PAGBannerSize.BANNER_W_320_H_50,binding.frBanner)
        }

        binding.showNative.setOnClickListener {
            PangleUtils.loadAndShowNative(this,"980088216",R.layout.ad_template_medium,binding.frNative)
        }

        binding.loadAndShowInter.setOnClickListener {
            PangleUtils.loadAndShowPAGInterstitial(this,interHolder)
        }

        binding.loadAndShowReward.setOnClickListener {
            PangleUtils.loadAdPAGRewarded(this,interReward)
        }
    }
}