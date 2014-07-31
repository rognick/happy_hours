package com.winify.happy_hours.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.winify.happy_hours.R;

public class TabsPagerAdapter extends FragmentPagerAdapter {
    private Context context;

    public TabsPagerAdapter(FragmentManager fm, Context c) {
        super(fm);
        context=c;
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                return new StatisticFragment(28800000, context.getResources().getString(R.string.today));
            case 1:
                return new StatisticFragment(201600000, context.getResources().getString(R.string.this_week));
            case 2:
                return new StatisticFragment(576000000, context.getResources().getString(R.string.this_month));
        }

        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

}