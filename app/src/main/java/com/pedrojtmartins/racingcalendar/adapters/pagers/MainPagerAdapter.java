package com.pedrojtmartins.racingcalendar.adapters.pagers;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import com.pedrojtmartins.racingcalendar.R;
import com.pedrojtmartins.racingcalendar.interfaces.fragments.IRecyclerViewFragment;
import com.pedrojtmartins.racingcalendar.models.Series;
import com.pedrojtmartins.racingcalendar.views.fragments.RaceListFragment;
import com.pedrojtmartins.racingcalendar.views.fragments.SeriesListFragment;

/**
 * Pedro Martins
 * 12/02/2017
 */

public class MainPagerAdapter extends FragmentStatePagerAdapter {

    private static final int TOTAL_PAGES = 3;
    public static final int PAGE_ALL = 0;
    public static final int PAGE_FAVOURITES = 1;
    public static final int PAGE_SERIES = 2;

    private Fragment[] mFragments;
    private final Resources mResources;

    // This adapter will provide fragment transition capabilities
    // To control it we'll need a couple of temp variables.
    // NOTE: For now it will only have one fragment that can change
    //       If more than one is needed in the future we need to use an array of temps
    private Fragment mTempFragment; //This will be used to store any fragment that must be replaced

    //In case we have transitions on multiple fragments we'll need this variable
    //private int[] tempFragmentPosition; //This int will be used to check if we need to undo the transition

    public MainPagerAdapter(FragmentManager fm, Resources resources) {
        super(fm);

        mFragments = new Fragment[getCount()];
        mResources = resources;
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case PAGE_ALL:
                if (mFragments[PAGE_ALL] == null) {
                    mFragments[PAGE_ALL] = new RaceListFragment().newInstance(false);

                }
                return mFragments[PAGE_ALL];

            case PAGE_SERIES:
                if (mFragments[PAGE_SERIES] == null) {
                    mFragments[PAGE_SERIES] = new SeriesListFragment();
                }
                return mFragments[PAGE_SERIES];

            case PAGE_FAVOURITES:
            default:
                if (mFragments[PAGE_FAVOURITES] == null) {
                    mFragments[PAGE_FAVOURITES] = new RaceListFragment().newInstance(true);
                }
                return mFragments[PAGE_FAVOURITES];
        }
    }

    @Override
    public int getCount() {
        return TOTAL_PAGES;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case PAGE_FAVOURITES:
                return mResources.getString(R.string.maintab_favourites);
            case PAGE_ALL:
                return mResources.getString(R.string.maintab_all);
            case PAGE_SERIES:
                return mResources.getString(R.string.maintab_series);
        }
        return null;
    }

    /**
     * Replaces the current series list fragment for a race list
     *
     * @param series what series will be loaded
     */
    public void replaceSeriesWithRaces(Series series) {
        mTempFragment = mFragments[PAGE_SERIES];
        mFragments[PAGE_SERIES] = new RaceListFragment().newInstance(series);
        notifyDataSetChanged();
    }

    /**
     * Undoes the fragment transition if one is in place
     *
     * @param currPos the position where the viewPager is currently on
     * @return true if an undo was completed. false otherwise
     */
    public boolean undoFragmentReplace(int currPos) {
        if (mTempFragment != null && currPos == PAGE_SERIES) {
            mFragments[PAGE_SERIES] = mTempFragment;
            mTempFragment = null;
            notifyDataSetChanged();
            return true;
        }

        return false;
    }

    public void smoothScrollToTop(int tab) {
        if (tab < 0 || tab > mFragments.length)
            return;

        Fragment fragment = mFragments[tab];
        if (fragment instanceof IRecyclerViewFragment) {
            ((IRecyclerViewFragment) fragment).smoothScrollToTop();
        }
    }

    public void previousItemsLoaded(int tab, int count) {
        if (tab < 0 || tab > mFragments.length)
            return;

        Fragment fragment = mFragments[tab];
        if (fragment instanceof IRecyclerViewFragment) {
            ((IRecyclerViewFragment) fragment).itemsReloaded(count);
        }
    }

    public boolean isOnTop(int tab) {
        if (tab < 0 || tab > mFragments.length)
            return false;

        Fragment fragment = mFragments[tab];
        if (fragment instanceof IRecyclerViewFragment) {
            return ((IRecyclerViewFragment) fragment).isOnTop();
        }

        return false;
    }

    public void resetFavouritesScrollPos() {
        Fragment fragment = mFragments[PAGE_FAVOURITES];
        if (fragment instanceof IRecyclerViewFragment) {
            ((IRecyclerViewFragment) fragment).resetScrollPos();
        }
    }
}
