package com.winify.happy_hours.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                return new StatisticFragment(28800000,"today");
            case 1:
                return new StatisticFragment(201600000,"this week");
            case 2:
                return new StatisticFragment(576000000,"this month");
        }

        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

}