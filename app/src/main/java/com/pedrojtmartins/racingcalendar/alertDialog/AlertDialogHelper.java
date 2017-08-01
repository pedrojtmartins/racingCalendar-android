package com.pedrojtmartins.racingcalendar.alertDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.pedrojtmartins.racingcalendar.R;

/**
 * Pedro Martins
 * 14/02/2017
 */

public class AlertDialogHelper {
    public static boolean displayYesNoDialog(Context context, int msg, int positive, int negative, final Handler handler) {
        if (context == null || handler == null)
            return false;

        new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AlertDialogCustom))
                .setMessage(msg)
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
                })
                .show();

        return true;
    }

    public static boolean displayOkDialog(Context context, int msg) {
        return displayOkDialog(context, msg, null);
    }

    public static boolean displayOkDialog(Context context, int msg, final Handler handler) {
        if (context == null)
            return false;

        new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AlertDialogCustom))
                .setMessage(msg)
                .setPositiveButton("Ok", null)
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if (handler != null)
                            handler.sendEmptyMessage(0);
                    }
                })
                .show();

        return true;
    }

    public static boolean displayNewSeriesDialog(Context context, Resources resources, int msg, String series) {
        if (context == null || resources == null)
            return false;

        String m = resources.getString(msg) + series;
        new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AlertDialogCustom))
                .setMessage(m)
                .setPositiveButton("Ok", null)
                .show();

        return true;
    }

    public static void displayNewNotificationDialog(
            final Context context,
            final LayoutInflater inflater,
            final String notificationMinutesBefore,
            final Handler handler) {
        displayNewNotificationDialog(context, inflater, notificationMinutesBefore, true, handler);
    }

    public static void displayNewNotificationDialog(
            final Context context,
            final LayoutInflater inflater,
            final String notificationMinutesBefore,
            final boolean withRememberOption,
            final Handler handler) {

        final View v = inflater.inflate(R.layout.alert_dialog_notification, null);
        final EditText et = (EditText) v.findViewById(R.id.alert_dialog_minutes_before);
        et.setText(notificationMinutesBefore);

        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AlertDialogCustom));
        builder.setTitle("");
        builder.setCancelable(false);
        builder.setView(v);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                newNotificationsDialogResponse(et.getText().toString(), handler, 1);
            }
        });

        builder.setNegativeButton(R.string.cancel, null);

        if (withRememberOption) {
            builder.setNeutralButton(R.string.remember, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    newNotificationsDialogResponse(et.getText().toString(), handler, 2);
                }
            });
        }

        builder.create();
        builder.show();
    }

    private static void newNotificationsDialogResponse(String minutesBefore, Handler handler, int what) {
        Bundle b = new Bundle();
        b.putString("timeBefore", minutesBefore);

        Message msg = new Message();
        msg.setData(b);
        msg.what = what;
        handler.sendMessage(msg);
    }


}
