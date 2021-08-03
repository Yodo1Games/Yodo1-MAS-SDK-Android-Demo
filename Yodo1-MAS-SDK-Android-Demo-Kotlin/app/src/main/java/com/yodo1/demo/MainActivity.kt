package com.yodo1.demo

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.ads.mediationtestsuite.MediationTestSuite
import com.google.android.material.switchmaterial.SwitchMaterial
import com.yodo1.mas.Yodo1Mas
import com.yodo1.mas.Yodo1Mas.RewardListener
import com.yodo1.mas.error.Yodo1MasError
import com.yodo1.mas.event.Yodo1MasAdEvent
import java.util.*

class MainActivity : AppCompatActivity() {
    private var progressDialog: ProgressDialog? = null
    private val timer = Timer()
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
        val gdpr = findViewById<SwitchMaterial>(R.id.yodo1_demo_gdpr)
        gdpr.isChecked = Yodo1Mas.getInstance().isGDPRUserConsent
        gdpr.setOnCheckedChangeListener { buttonView: CompoundButton, isChecked: Boolean ->
            setGDPR(
                buttonView,
                isChecked
            )
        }
        val coppa = findViewById<SwitchMaterial>(R.id.yodo1_demo_coppa)
        coppa.isChecked = Yodo1Mas.getInstance().isCOPPAAgeRestricted
        coppa.setOnCheckedChangeListener { buttonView: CompoundButton, isChecked: Boolean ->
            setCOPPA(
                buttonView,
                isChecked
            )
        }
        val ccpa = findViewById<SwitchMaterial>(R.id.yodo1_demo_ccpa)
        ccpa.isChecked = Yodo1Mas.getInstance().isCCPADoNotSell
        ccpa.setOnCheckedChangeListener { buttonView: CompoundButton, isChecked: Boolean ->
            setCCPA(
                buttonView,
                isChecked
            )
        }
        val timerTask: TimerTask = object : TimerTask() {
            override fun run() {
                val isBannerLoad = Yodo1Mas.getInstance().isBannerAdLoaded
                if (isBannerLoad) {
                    Yodo1Mas.getInstance().showBannerAd(this@MainActivity)
                    timer.cancel()
                }
            }
        }
        progressDialog = ProgressDialog(this)
        progressDialog!!.setTitle("sdk init...")
        progressDialog!!.setCancelable(false)
        progressDialog!!.show()
        Yodo1Mas.getInstance().init(this, "Your App Id", object : Yodo1Mas.InitListener {
            override fun onMasInitSuccessful() {
                progressDialog!!.dismiss()
                Toast.makeText(this@MainActivity, "sdk init successful", Toast.LENGTH_SHORT).show()
                timer.schedule(timerTask, 0, 10000)
            }

            override fun onMasInitFailed(error: Yodo1MasError) {
                progressDialog!!.dismiss()
                Toast.makeText(this@MainActivity, error.message, Toast.LENGTH_SHORT).show()
            }
        })
        Yodo1Mas.getInstance().setRewardListener(rewardListener)
        Yodo1Mas.getInstance().setInterstitialListener(interstitialListener)
        Yodo1Mas.getInstance().setBannerListener(bannerListener)
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
         * banner alignment, align = vertical | horizontal
         *              vertical:
         *              Yodo1Mas.BannerTop
         *              Yodo1Mas.BannerBottom
         *              Yodo1Mas.BannerVerticalCenter
         *              horizontal:
         *              Yodo1Mas.BannerLeft
         *              Yodo1Mas.BannerRight
         */
        val align = Yodo1Mas.BannerBottom or Yodo1Mas.BannerHorizontalCenter

        val offsetX =
            0 // horizontal offset, offsetX > 0, the banner will move to the right. offsetX < 0, the banner will move to the left. if align = Yodo1Mas.BannerLeft, offsetX < 0 is invalid

        val offsetY =
            0 // vertical offset, offsetY > 0, the banner will move to the bottom. offsetY < 0, the banner will move to the top.if align = Yodo1Mas.BannerTop, offsetY < 0 is invalid

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

    private fun setGDPR(buttonView: CompoundButton, isChecked: Boolean) {
        Yodo1Mas.getInstance().setGDPR(isChecked)
    }

    private fun setCOPPA(buttonView: CompoundButton, isChecked: Boolean) {
        Yodo1Mas.getInstance().setCOPPA(isChecked)
    }

    private fun setCCPA(buttonView: CompoundButton, isChecked: Boolean) {
        Yodo1Mas.getInstance().setCCPA(isChecked)
    }
}