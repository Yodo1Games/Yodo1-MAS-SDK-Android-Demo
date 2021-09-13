package com.yodo1.demo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.ads.mediationtestsuite.MediationTestSuite;
import com.yodo1.mas.Yodo1Mas;
import com.yodo1.mas.error.Yodo1MasError;
import com.yodo1.mas.event.Yodo1MasAdEvent;
import com.yodo1.mas.helper.model.Yodo1MasAdBuildConfig;

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
//        findViewById(R.id.yodo1_applovin_mediation_debugger).setOnClickListener(this::showAppLovinMediationDebugger);
//        findViewById(R.id.yodo1_admob_mediation_test).setOnClickListener(this::showAdMobMediationTestSuite);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("sdk init...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Yodo1MasAdBuildConfig config = new Yodo1MasAdBuildConfig.Builder()
                .enableAdaptiveBanner(true)
                .enableUserPrivacyDialog(true)
                .build();
        Yodo1Mas.getInstance().setAdBuildConfig(config);

        Yodo1Mas.getInstance().init(this, "Your AppKey", new Yodo1Mas.InitListener() {
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

        Yodo1Mas.getInstance().showBannerAd(MainActivity.this, "mas_test");
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
        String placement = "placementId";

        /**
         * 'align' will determine the general position of the banner, such as:
         *       - top horizontal center
         *       - bottom horizontal center
         *       - left vertical center
         *       - right vertical center
         *       - horizontal vertical center
         *        The above 5 positions can basically meet most of the needs
         *
         * align = vertical | horizontal
         *              vertical:
         *              Yodo1Mas.BannerTop
         *              Yodo1Mas.BannerBottom
         *              Yodo1Mas.BannerVerticalCenter
         *              horizontal:
         *              Yodo1Mas.BannerLeft
         *              Yodo1Mas.BannerRight
         */
        int align = Yodo1Mas.BannerBottom | Yodo1Mas.BannerHorizontalCenter;

        /**
         * 'offset' will adjust the position of the banner on the basis of 'align'
         *  If 'align' cannot meet the demand, you can adjust it by 'offset'
         *
         *  horizontal offset:
         *  offsetX > 0, the banner will move to the right.
         *  offsetX < 0, the banner will move to the left.
         *  if align = Yodo1Mas.BannerLeft, offsetX < 0 is invalid
         *
         *  vertical offset:
         *  offsetY > 0, the banner will move to the bottom.
         *  offsetY < 0, the banner will move to the top.
         *  if align = Yodo1Mas.BannerTop, offsetY < 0 is invalid
         *
         *  Click here to see more details: https://developers.yodo1.com/knowledge-base/android-banner-configuration/
         */
        int offsetX = 0;
        int offsetY = 0;
        Yodo1Mas.getInstance().showBannerAd(this, placement, align, offsetX, offsetY);
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

}