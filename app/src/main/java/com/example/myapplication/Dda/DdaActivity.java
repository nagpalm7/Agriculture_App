package com.example.myapplication.Dda;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.login_activity;
import com.google.android.material.navigation.NavigationView;

public class DdaActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    login_activity logg = new login_activity();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dda);
        Toast.makeText(this,"Dda successfully logged in..",Toast.LENGTH_LONG).show();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DrawerLayout drawer = findViewById(R.id.drawer_layout_dda);
        NavigationView navigationView = findViewById(R.id.navofdda);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        if(savedInstanceState==null){
            getSupportFragmentManager().beginTransaction().replace(R.id.container,new DdaOngoingFragment()).commit();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        if(id==R.id.ongoing_item){
            getSupportFragmentManager().beginTransaction().replace(R.id.container,new DdaOngoingFragment()).commit();
            Toast.makeText(this,"Ongoing",Toast.LENGTH_LONG).show();
        }else if(id==R.id.completed_item){
            getSupportFragmentManager().beginTransaction().replace(R.id.container,new DdaCompletedFragment()).commit();
            Toast.makeText(this,"Completed",Toast.LENGTH_LONG).show();
        }else if(id==R.id.pending_item){
            getSupportFragmentManager().beginTransaction().replace(R.id.container,new DdaPendingFragment()).commit();
            Toast.makeText(this,"Pending",Toast.LENGTH_LONG).show();
        }else if(id==R.id.logout){
            {
                SharedPreferences.Editor editor = getSharedPreferences("tokenFile", Context.MODE_PRIVATE).edit();
                editor.clear();
                editor.apply();
                Intent intent = new Intent(this,login_activity.class);
                startActivity(intent);
            }
        } else {

        }

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout_dda);
        drawerLayout.closeDrawer(GravityCompat.START);

        return false;
    }
}
