package com.pedrojtmartins.racingcalendar.admob;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.pedrojtmartins.racingcalendar.R;
import com.pedrojtmartins.racingcalendar._settings.Settings;

/**
 * Pedro Martins
 * 24/04/2017
 */

public class AdmobHelper {
    private final static int SHOW_NOTIF_AD_EVERY = 5;

    private InterstitialAd mInterstitialAd;
    private boolean mLoadNextInterstitial;

    private static AdmobHelper admobHelper;

    public static AdmobHelper getInstance() {
        if (admobHelper == null)
            admobHelper = new AdmobHelper();


        return admobHelper;
    }

    public void showBannerAd(final Context context, final Resources resources, final AdView adView) {
        if (!Settings.PRO_VERSION) {
            MobileAds.initialize(context, resources.getString(R.string.admob_app_id));
            AdRequest adRequest = getAdRequest();

            adView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    adView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAdFailedToLoad(int i) {
                    super.onAdFailedToLoad(i);
                    // TODO: 20/04/2017 set network state listener

                }
            });
            adView.loadAd(adRequest);
        } else {
            adView.setVisibility(View.GONE);
        }
    }

    public void readyInterstitialAd(Context context, Resources resources) {
        showInterstitialAd(context, resources, -1, null);
    }

    public boolean showInterstitialAd(Context context, Resources resources, int count, final Handler.Callback callback) {
        if (Settings.PRO_VERSION) {
            if (callback != null) {
                callback.handleMessage(new Message());
            }

            return false;
        }

        if (mInterstitialAd == null) {
            mInterstitialAd = new InterstitialAd(context);
            mInterstitialAd.setAdUnitId(resources.getString(R.string.admob_interstitial_ad_id));
            requestNewInterstitial();
        }

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                requestNewInterstitial();

                if (callback != null) {
                    callback.handleMessage(new Message());
                }
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                if (mLoadNextInterstitial) {
                    mLoadNextInterstitial = false;
                    mInterstitialAd.show();
                }
            }
        });

        if (count > 0 && count % SHOW_NOTIF_AD_EVERY == 0) {
            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            } else {
                mLoadNextInterstitial = true;
            }

            return true;
        } else {
            if (callback != null) {
                callback.handleMessage(new Message());
            }
        }

        return false;
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = getAdRequest();
        mInterstitialAd.loadAd(adRequest);
    }

    private AdRequest getAdRequest() {
        return new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice(Settings.TEST_DEVICE_ID)
                .build();
    }
}
