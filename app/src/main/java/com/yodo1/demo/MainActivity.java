package com.yodo1.demo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.android.ads.mediationtestsuite.MediationTestSuite;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.yodo1.mas.Yodo1Mas;
import com.yodo1.mas.error.Yodo1MasError;
import com.yodo1.mas.event.Yodo1MasAdEvent;

import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;

    private final Yodo1Mas.RewardListener rewardListener = new Yodo1Mas.RewardListener() {
        @Override
        public void onAdOpened(@NonNull Yodo1MasAdEvent event) {

        }

        @Override
        public void onAdvertRewardEarned(@NonNull Yodo1MasAdEvent event) {

        }

        @Override
        public void onAdError(@NonNull Yodo1MasAdEvent event, @NonNull Yodo1MasError error) {
            Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
        }
    };

    private final Yodo1Mas.InterstitialListener interstitialListener = new Yodo1Mas.InterstitialListener() {
        @Override
        public void onAdOpened(@NonNull Yodo1MasAdEvent event) {
        }

        @Override
        public void onAdError(@NonNull Yodo1MasAdEvent event, @NonNull Yodo1MasError error) {
            Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAdClosed(@NonNull Yodo1MasAdEvent event) {
        }
    };

    private final Yodo1Mas.BannerListener bannerListener = new Yodo1Mas.BannerListener() {
        @Override
        public void onAdOpened(@NonNull Yodo1MasAdEvent event) {
        }

        @Override
        public void onAdError(@NonNull Yodo1MasAdEvent event, @NonNull Yodo1MasError error) {
            Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAdClosed(@NonNull Yodo1MasAdEvent event) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.yodo1_demo_video).setOnClickListener(this::showVideo);
        findViewById(R.id.yodo1_demo_interstitial).setOnClickListener(this::showInterstitial);
        findViewById(R.id.yodo1_demo_banner).setOnClickListener(this::showBanner);
        findViewById(R.id.yodo1_applovin_mediation_debugger).setOnClickListener(this::showAppLovinMediationDebugger);
        findViewById(R.id.yodo1_admob_mediation_test).setOnClickListener(this::showAdMobMediationTestSuite);

        SwitchMaterial gdpr = findViewById(R.id.yodo1_demo_gdpr);
        gdpr.setChecked(Yodo1Mas.getInstance().isGDPRUserConsent());
        gdpr.setOnCheckedChangeListener(this::setGDPR);
        SwitchMaterial coppa = findViewById(R.id.yodo1_demo_coppa);
        coppa.setChecked(Yodo1Mas.getInstance().isCOPPAAgeRestricted());
        coppa.setOnCheckedChangeListener(this::setCOPPA);
        SwitchMaterial ccpa = findViewById(R.id.yodo1_demo_ccpa);
        ccpa.setChecked(Yodo1Mas.getInstance().isCCPADoNotSell());
        ccpa.setOnCheckedChangeListener(this::setCCPA);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("sdk init...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Yodo1Mas.getInstance().init(this, "Your App Id", new Yodo1Mas.InitListener() {
            @Override
            public void onMasInitSuccessful() {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "sdk init successful", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMasInitFailed(@NonNull Yodo1MasError error) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        Yodo1Mas.getInstance().setRewardListener(rewardListener);
        Yodo1Mas.getInstance().setInterstitialListener(interstitialListener);
        Yodo1Mas.getInstance().setBannerListener(bannerListener);
    }


    private void showVideo(View v) {
        if (!Yodo1Mas.getInstance().isRewardedAdLoaded()) {
            Toast.makeText(this, "Rewarded video ad has not been cached.", Toast.LENGTH_SHORT).show();
            return;
        }
        Yodo1Mas.getInstance().showRewardedAd(this);
    }

    private void showInterstitial(View v) {
        if (!Yodo1Mas.getInstance().isInterstitialAdLoaded()) {
            Toast.makeText(this, "Interstitial ad has not been cached.", Toast.LENGTH_SHORT).show();
            return;
        }
        Yodo1Mas.getInstance().showInterstitialAd(this);
    }

    private void showBanner(View v) {
        if (!Yodo1Mas.getInstance().isBannerAdLoaded()) {
            Toast.makeText(this, "Banner ad has not been cached.", Toast.LENGTH_SHORT).show();
            return;
        }
        Yodo1Mas.getInstance().showBannerAd(this);
    }

    private void showAppLovinMediationDebugger(View v) {
        try {
            Class<?> applovinSdkClass = Class.forName("com.applovin.sdk.AppLovinSdk");
            Method instanceMethod = applovinSdkClass.getDeclaredMethod("getInstance", Context.class);
            Object obj = instanceMethod.invoke(applovinSdkClass, MainActivity.this);
            Method debuggerMethod = applovinSdkClass.getMethod("showMediationDebugger");
            debuggerMethod.invoke(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAdMobMediationTestSuite(View v) {
        MediationTestSuite.launch(this);
    }

    private void setGDPR(CompoundButton buttonView, boolean isChecked) {
        Yodo1Mas.getInstance().setGDPR(isChecked);
    }

    private void setCOPPA(CompoundButton buttonView, boolean isChecked) {
        Yodo1Mas.getInstance().setCOPPA(isChecked);
    }

    private void setCCPA(CompoundButton buttonView, boolean isChecked) {
        Yodo1Mas.getInstance().setCCPA(isChecked);
    }
}