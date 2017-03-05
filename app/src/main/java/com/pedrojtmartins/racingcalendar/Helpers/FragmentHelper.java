package com.pedrojtmartins.racingcalendar.Helpers;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Pedro Martins
 * 04/03/2017
 */

public class FragmentHelper {
    public static void replaceFragments(FragmentManager fragmentManager, int fragmentContainer, Fragment newFragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(fragmentContainer, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
