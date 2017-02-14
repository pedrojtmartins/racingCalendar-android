package com.pedrojtmartins.racingcalendar.Helpers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;

/**
 * Pedro Martins
 * 14/02/2017
 */

public class AlertDialogHelper {
    public static boolean displayYesNoDialog(Context context, int msg, int positive, int negative, final Handler handler) {
        if (context == null || handler == null)
            return false;

        new AlertDialog.Builder(context).setMessage(msg)
                .setPositiveButton(positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        handler.sendEmptyMessage(1);
                    }
                })
                .setNegativeButton(negative, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        handler.sendEmptyMessage(0);
                    }
                }).show();

        return true;
    }
}
