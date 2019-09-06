package com.example.myapplication.Admin.DdoActivity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.myapplication.R;
import com.google.android.material.tabs.TabLayout;

public class DdoActivity extends AppCompatActivity {
    private DdoActivityPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ddo);
        TabLayout tabLayout = findViewById(R.id.ddo_activity_tablayout);
        ViewPager viewPager = findViewById(R.id.ddo_activity_viewpager);
        adapter = new DdoActivityPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new DdoPending());
        adapter.addFragment(new DdoOngoing());
        adapter.addFragment(new DdoCompleted());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
