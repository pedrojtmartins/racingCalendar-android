package com.pedrojtmartins.racingcalendar.Helpers;

import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Pedro Martins
 * 12/02/2017
 */

public class SnackBarHelper {
    public static void display(View view, int stringResourceId) {
        display(view, stringResourceId, Snackbar.LENGTH_LONG);
    }

    public static void display(View view, int stringResourceId, int duration) {
        if (view != null)
            Snackbar.make(view, stringResourceId, duration).show();
    }

    public static void displayWithAction(final View view,
                                         final int stringResourceId,
                                         final int stringActionResourceId,
                                         final View.OnClickListener clickListener) {
        displayWithAction(view, stringResourceId, stringActionResourceId, clickListener, Snackbar.LENGTH_LONG);
    }

    public static void displayWithAction(final View view,
                                         final int stringResourceId,
                                         final int stringActionResourceId,
                                         final View.OnClickListener clickListener,
                                         final int duration) {
        if (view != null) {
            Snackbar snackBar = Snackbar.make(view, stringResourceId, duration);

            if (clickListener != null)
                snackBar.setAction(stringActionResourceId, clickListener);

            snackBar.show();
        }
    }
}
