package com.yodo1.demo

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.ads.mediationtestsuite.MediationTestSuite
import com.yodo1.mas.Yodo1Mas
import com.yodo1.mas.Yodo1Mas.RewardListener
import com.yodo1.mas.error.Yodo1MasError
import com.yodo1.mas.event.Yodo1MasAdEvent
import com.yodo1.mas.helper.model.Yodo1MasAdBuildConfig

class MainActivity : AppCompatActivity() {
    private var progressDialog: ProgressDialog? = null
    private val rewardListener: RewardListener = object : RewardListener() {
        override fun onAdOpened(event: Yodo1MasAdEvent) {}
        override fun onAdvertRewardEarned(event: Yodo1MasAdEvent) {}
        override fun onAdError(event: Yodo1MasAdEvent, error: Yodo1MasError) {
            Toast.makeText(this@MainActivity, error.toString(), Toast.LENGTH_SHORT).show()
        }
    }
    private val interstitialListener: Yodo1Mas.InterstitialListener =
        object : Yodo1Mas.InterstitialListener() {
            override fun onAdOpened(event: Yodo1MasAdEvent) {}
            override fun onAdError(event: Yodo1MasAdEvent, error: Yodo1MasError) {
                Toast.makeText(this@MainActivity, error.toString(), Toast.LENGTH_SHORT).show()
            }

            override fun onAdClosed(event: Yodo1MasAdEvent) {}
        }
    private val bannerListener: Yodo1Mas.BannerListener = object : Yodo1Mas.BannerListener() {
        override fun onAdOpened(event: Yodo1MasAdEvent) {}
        override fun onAdError(event: Yodo1MasAdEvent, error: Yodo1MasError) {
            Toast.makeText(this@MainActivity, error.toString(), Toast.LENGTH_SHORT).show()
        }

        override fun onAdClosed(event: Yodo1MasAdEvent) {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.yodo1_demo_video).setOnClickListener { v: View -> showVideo(v) }
        findViewById<View>(R.id.yodo1_demo_interstitial).setOnClickListener { v: View ->
            showInterstitial(
                v
            )
        }
        findViewById<View>(R.id.yodo1_demo_banner).setOnClickListener { v: View -> showBanner(v) }
        //        findViewById(R.id.yodo1_applovin_mediation_debugger).setOnClickListener(this::showAppLovinMediationDebugger);
//        findViewById(R.id.yodo1_admob_mediation_test).setOnClickListener(this::showAdMobMediationTestSuite);
        progressDialog = ProgressDialog(this)
        progressDialog!!.setTitle("sdk init...")
        progressDialog!!.setCancelable(false)
        progressDialog!!.show()

        val config = Yodo1MasAdBuildConfig.Builder()
            .enableAdaptiveBanner(true)
            .enableUserPrivacyDialog(true)
            .build()
        Yodo1Mas.getInstance().setAdBuildConfig(config)

        Yodo1Mas.getInstance().init(this, "Your AppKey", object : Yodo1Mas.InitListener {
            override fun onMasInitSuccessful() {
                progressDialog!!.dismiss()
                Toast.makeText(this@MainActivity, "sdk init successful", Toast.LENGTH_SHORT).show()
            }

            override fun onMasInitFailed(error: Yodo1MasError) {
                progressDialog!!.dismiss()
                Toast.makeText(this@MainActivity, error.message, Toast.LENGTH_SHORT).show()
            }
        })
        Yodo1Mas.getInstance().setRewardListener(rewardListener)
        Yodo1Mas.getInstance().setInterstitialListener(interstitialListener)
        Yodo1Mas.getInstance().setBannerListener(bannerListener)
        Yodo1Mas.getInstance().showBannerAd(this@MainActivity, "mas_test")
    }

    private fun showVideo(v: View) {
        if (!Yodo1Mas.getInstance().isRewardedAdLoaded) {
            Toast.makeText(this, "Rewarded video ad has not been cached.", Toast.LENGTH_SHORT)
                .show()
            return
        }
        Yodo1Mas.getInstance().showRewardedAd(this)
    }

    private fun showInterstitial(v: View) {
        if (!Yodo1Mas.getInstance().isInterstitialAdLoaded) {
            Toast.makeText(this, "Interstitial ad has not been cached.", Toast.LENGTH_SHORT).show()
            return
        }
        Yodo1Mas.getInstance().showInterstitialAd(this)
    }

    private fun showBanner(v: View) {
        if (!Yodo1Mas.getInstance().isBannerAdLoaded) {
            Toast.makeText(this, "Banner ad has not been cached.", Toast.LENGTH_SHORT).show()
            return
        }
        val placement = "placementId"

        /**
         * 'align' will determine the general position of the banner, such as:
         * - top horizontal center
         * - bottom horizontal center
         * - left vertical center
         * - right vertical center
         * - horizontal vertical center
         * The above 5 positions can basically meet most of the needs
         *
         * align = vertical | horizontal
         * vertical:
         * Yodo1Mas.BannerTop
         * Yodo1Mas.BannerBottom
         * Yodo1Mas.BannerVerticalCenter
         * horizontal:
         * Yodo1Mas.BannerLeft
         * Yodo1Mas.BannerRight
         */
        val align = Yodo1Mas.BannerBottom or Yodo1Mas.BannerHorizontalCenter

        /**
         * 'offset' will adjust the position of the banner on the basis of 'align'
         * If 'align' cannot meet the demand, you can adjust it by 'offset'
         *
         * horizontal offset:
         * offsetX > 0, the banner will move to the right.
         * offsetX < 0, the banner will move to the left.
         * if align = Yodo1Mas.BannerLeft, offsetX < 0 is invalid
         *
         * vertical offset:
         * offsetY > 0, the banner will move to the bottom.
         * offsetY < 0, the banner will move to the top.
         * if align = Yodo1Mas.BannerTop, offsetY < 0 is invalid
         *
         * Click here to see more details: https://developers.yodo1.com/knowledge-base/android-banner-configuration/
         */
        val offsetX = 0
        val offsetY = 0
        Yodo1Mas.getInstance().showBannerAd(this, placement, align, offsetX, offsetY)
    }

    private fun showAppLovinMediationDebugger(v: View) {
        try {
            val applovinSdkClass = Class.forName("com.applovin.sdk.AppLovinSdk")
            val instanceMethod =
                applovinSdkClass.getDeclaredMethod("getInstance", Context::class.java)
            val obj = instanceMethod.invoke(applovinSdkClass, this@MainActivity)
            val debuggerMethod = applovinSdkClass.getMethod("showMediationDebugger")
            debuggerMethod.invoke(obj)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showAdMobMediationTestSuite(v: View) {
        MediationTestSuite.launch(this)
    }
}