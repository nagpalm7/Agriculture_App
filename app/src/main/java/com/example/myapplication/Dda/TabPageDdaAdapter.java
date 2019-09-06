package com.example.myapplication.Dda;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.myapplication.Dda.assignedfragment;
import com.example.myapplication.Dda.notassignedfragment;

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

    @Nullable
    @Override
    public CharSequence getPageTitle(int fr) {
        String a = null;
        if (fr == 0)
           a = "NOT ASSIGNED" ;
        else if(fr == 1)
           a = "ASSIGNED" ;

          return a;}
}