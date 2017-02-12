package com.pedrojtmartins.racingcalendar.Adapters.Pagers;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.pedrojtmartins.racingcalendar.R;
import com.pedrojtmartins.racingcalendar.Views.Fragments.RaceListFragment;

/**
 * Pedro Martins
 * 12/02/2017
 */

public class MainPagerAdapter extends FragmentPagerAdapter {

    Fragment[] mFragments;
    private final Resources mResources;

    public MainPagerAdapter(FragmentManager fm, Resources resources) {
        super(fm);

        mFragments = new Fragment[getCount()];
        mResources = resources;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 1:
                if (mFragments[1] == null) {
                    mFragments[1] = new RaceListFragment().newInstance(false);
                }
                return mFragments[1];

            case 0:
            default:
                if (mFragments[0] == null) {
                    mFragments[0] = new RaceListFragment().newInstance(true);
                }
                return mFragments[0];
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return mResources.getString(R.string.maintab_favourites);
            case 1:
                return mResources.getString(R.string.maintab_all);
        }
        return null;
    }
}
