package com.pedrojtmartins.racingcalendar.helpers;

import android.content.res.Resources;
import android.os.Build;


/**
 * Pedro Martins
 * 29/04/2017
 */

public class APIHelper {
    public static int getColor(Resources resources, int resId) {
        if (resources == null || resId <= 0)
            return 0;

        try {
            return Build.VERSION.SDK_INT >= 23 ? resources.getColor(resId, null) : resources.getColor(resId);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
