package com.noorsy.lightbulb.debatetimer;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.View;

/**
 * Created by Noor Syed on 2/4/2017.
 */
class CustomPagerAdapter extends FragmentPagerAdapter {

    public CustomPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int pos) {

        switch(pos) {

            case 0: return TimerFragment.newInstance("0");
            case 1: return TimerFragment.newInstance("1");
            case 2: return TimerFragment.newInstance("2");
            case 3: return TimerFragment.newInstance("3");
            case 4: return TimerFragment.newInstance("4");
            case 5: return TimerFragment.newInstance("5");
            case 6: return TimerFragment.newInstance("6");
            default: return TimerFragment.newInstance("0");
        }
        //Fragment newFragment = TimerFragment.newInstance("0");


    }

    @Override
    public int getCount() {
        return 7;
    }




}
//testing dicks