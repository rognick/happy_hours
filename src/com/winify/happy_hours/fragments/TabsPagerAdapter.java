package com.winify.happy_hours.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.winify.happy_hours.constants.Extra;

public class TabsPagerAdapter extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                return new StatisticFragment(28800000, Extra.TODAY);
            case 1:
                return new StatisticFragment(201600000, Extra.THIS_WEEK);
            case 2:
                return new StatisticFragment(576000000, Extra.THIS_MONTH);
        }

        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

}