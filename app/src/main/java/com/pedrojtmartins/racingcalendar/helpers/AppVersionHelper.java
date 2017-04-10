package com.pedrojtmartins.racingcalendar.helpers;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import com.pedrojtmartins.racingcalendar.BuildConfig;

/**
 * Pedro Martins
 * 18/03/2017
 */

public class AppVersionHelper {
    public static int getCurrentAppVersionCode() {
        return BuildConfig.VERSION_CODE;
    }

    public static Intent getGooglePlayIntent(final String appPackageName, final PackageManager packageManager) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName));
        if (intent.resolveActivity(packageManager) != null) {
            return intent;
        } else {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName));
            return intent;
        }
    }
}
