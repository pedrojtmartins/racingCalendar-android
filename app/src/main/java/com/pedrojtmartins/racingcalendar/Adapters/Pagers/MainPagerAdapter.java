package com.pedrojtmartins.racingcalendar.Adapters.Pagers;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;

import com.pedrojtmartins.racingcalendar.R;
import com.pedrojtmartins.racingcalendar.Views.Fragments.RaceListFragment;

/**
 * Pedro Martins
 * 12/02/2017
 */

public class MainPagerAdapter extends FragmentPagerAdapter {

    private final FragmentManager mFragmentManager;
    Fragment[] mFragments;
    private final Resources mResources;

    public MainPagerAdapter(FragmentManager fm, Resources resources) {
        super(fm);

        mFragmentManager = fm;
        mFragments = new Fragment[getCount()];
        mResources = resources;
    }

    @Override
    public int getItemPosition(Object object){
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 1:
                if (mFragments[1] == null) {
                    mFragments[1] = new RaceListFragment().newInstance(false);
                }
                return mFragments[1];

//            case 2:
//                if (mFragments[2] == null) {
//                    mFragments[2] = new SeriesListFragment();
//                }
//                return mFragments[2];

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
//            case 2:
//                return mResources.getString(R.string.maintab_series);
        }
        return null;
    }
}
