package com.pedrojtmartins.racingcalendar.helpers;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Pedro Martins
 * 28/08/2017
 */

public class InternetConnectionTest extends AsyncTask<Void, Void, String> {
    private final Handler.Callback callback;

    public InternetConnectionTest(Handler.Callback callback) {
        this.callback = callback;
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            InetAddress inetAddress = InetAddress.getByName("google.com");
            return inetAddress.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String ip) {
        if (callback != null) {
            Message msg = new Message();
            msg.what = ip == null || ip.isEmpty() ? 0 : 1;
            callback.handleMessage(msg);
        }

        super.onPostExecute(ip);
    }
}
