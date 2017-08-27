package com.pedrojtmartins.racingcalendar.interfaces.fragments;

/**
 * Pedro Martins
 * 25/03/2017
 */

public interface IRecyclerViewFragment {
    void smoothScrollToTop();
    boolean isOnTop();
    void itemsReloaded(int count);
    void resetScrollPos();

    int getScrollPosition();
    void setScrollPos(int seriesScrollPosition);
}
