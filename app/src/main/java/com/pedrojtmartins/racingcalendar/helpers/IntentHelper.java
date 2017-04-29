package com.pedrojtmartins.racingcalendar.helpers;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

/**
 * Pedro Martins
 * 25/04/2017
 */

public class IntentHelper {
    public static void composeEmail(Context context, String[] addresses, String subject) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }

    public static boolean canResolveIntent(Intent intent, PackageManager packageManager) {
        return intent.resolveActivity(packageManager) != null;
    }
}
