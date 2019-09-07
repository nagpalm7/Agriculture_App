package com.example.myapplication.Admin.DdoActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

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
        Intent intent = getIntent();
        String ddoId = intent.getStringExtra("ddoId");
        Log.d("ddoId", "onCreate: " + ddoId);
        TabLayout tabLayout = findViewById(R.id.ddo_activity_tablayout);
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        ViewPager viewPager = findViewById(R.id.ddo_activity_viewpager);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Ddo Name");
        adapter = new DdoActivityPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new DdoPending(ddoId));
        adapter.addFragment(new DdoOngoing(ddoId));
        adapter.addFragment(new DdoCompleted(ddoId));
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
