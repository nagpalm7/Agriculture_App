package com.example.myapplication.Admin;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.myapplication.Admin.completed_fragment;
import com.example.myapplication.Admin.pending_fragment;

public class TabPageAdapter extends FragmentPagerAdapter {

    private int tabcount;
    public TabPageAdapter(FragmentManager fm,int tabcount) {
        super(fm);
        this.tabcount=tabcount;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment frag=null;
        switch(position){
            case 0:
                frag= new pending_fragment();
                break;
            case 1:
                frag= new completed_fragment();
                break;
        }

        return frag;
    }

    @Override
    public int getCount() {
        return tabcount;
    }
}
