package com.pedrojtmartins.racingcalendar.helpers;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;

import com.pedrojtmartins.racingcalendar.R;

/**
 * Pedro Martins
 * 25/04/2017
 */

public class IntentHelper {
    public static void sendFeedback(Context context) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{context.getString(R.string.devEmail)});
        intent.putExtra(Intent.EXTRA_SUBJECT, "How to improve");
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }

    public static boolean canResolveIntent(Intent intent, PackageManager packageManager) {
        return intent.resolveActivity(packageManager) != null;
    }

    public static boolean openUrl(Context context, String url, boolean inApp) {
        if (url == null) {
            return false;
        }

        if (!url.startsWith("http")) {
            url = "http://" + url;
        }

        if (inApp) {
            // Open default web browser directly
            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            if (IntentHelper.canResolveIntent(webIntent, context.getPackageManager())) {
                context.startActivity(webIntent);
                return true;
            }
        } else {
            // Open chrome custom tab. In case it is not available
            // the default web browser will be auto called.
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            builder.setToolbarColor(APIHelper.getColor(context.getResources(), R.color.primary));
            builder.setSecondaryToolbarColor(APIHelper.getColor(context.getResources(), R.color.primary_dark));
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.launchUrl(context, Uri.parse(url));
            return true;
        }

        return false;
    }
}
