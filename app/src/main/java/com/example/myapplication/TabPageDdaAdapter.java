package com.example.myapplication;

import android.widget.FrameLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabPageDdaAdapter extends FragmentPagerAdapter {

    private int tabcount;

    public TabPageDdaAdapter(FragmentManager fm, int tabcount) {
        super(fm);
        this.tabcount = tabcount;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fr = null;
        switch (position){
            case 0:
                fr = new assignedfragment();
                break;
            case 1:
                fr = new notassignedfragment();
                break;
        }
        return fr;
    }

    @Override
    public int getCount() {
        return tabcount;
    }
}
