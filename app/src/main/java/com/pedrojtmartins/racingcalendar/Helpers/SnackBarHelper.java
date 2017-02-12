package com.pedrojtmartins.racingcalendar.Helpers;

import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Pedro Martins
 * 12/02/2017
 */

public class SnackBarHelper {
    public static void display(View view, int stringResourceId) {
        if (view != null)
            Snackbar.make(view, stringResourceId, Snackbar.LENGTH_LONG).show();
    }
}
