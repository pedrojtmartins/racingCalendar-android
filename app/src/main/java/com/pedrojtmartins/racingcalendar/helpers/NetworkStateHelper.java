package com.pedrojtmartins.racingcalendar.helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Handler;

import com.pedrojtmartins.racingcalendar.alertDialog.AlertDialogHelper;

import java.io.IOException;

/**
 * Pedro Martins
 * 28/08/2017
 */

public class NetworkStateHelper {
    public static boolean isInternetAvailable(final Context context, final int msg, final Handler handler) {
        if (context == null)
            return false;

        boolean available = isNetworkAvailable(context) && isInternetAvailable();
        if (!available) {
            AlertDialogHelper.displayOkDialog(context, msg, handler);
        }

        return available;
    }

    private static boolean isNetworkAvailable(Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    private static boolean isInternetAvailable() {
        final String command = "ping -c 1 google.com";

        try {
            return (Runtime.getRuntime().exec(command).waitFor() == 0);
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }

        return false;
    }
}
