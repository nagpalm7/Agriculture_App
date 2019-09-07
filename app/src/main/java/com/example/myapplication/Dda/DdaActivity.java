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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.login_activity;
import com.google.android.material.navigation.NavigationView;

public class DdaActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private TextView textView;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dda);
        Toast.makeText(this,"Dda successfully logged in..",Toast.LENGTH_LONG).show();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        DrawerLayout drawer = findViewById(R.id.drawer_layout_dda);
        navigationView = findViewById(R.id.navofdda);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        //problem in these below 5 lines the name is not visible in the header
        View view = LayoutInflater.from(this).inflate(R.layout.nav_header_main,null,false);
        textView = view.findViewById(R.id.nameOfUserLoggedIn);

        final SharedPreferences preferences = getSharedPreferences("tokenFile",Context.MODE_PRIVATE);
        final String nameOfUser = preferences.getString("nameOfUser","");
        Toast.makeText(this,nameOfUser,Toast.LENGTH_LONG).show();
        textView.setText(nameOfUser);
        //close

        if(savedInstanceState==null){
            getSupportFragmentManager().beginTransaction().replace(R.id.container,new DdaOngoingFragment()).commit();
            navigationView.getMenu().getItem(0).setChecked(true);
            getSupportActionBar().setTitle("Ongoing Locations");
        }
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        if(id==R.id.ongoing_item){
            getSupportFragmentManager().beginTransaction().replace(R.id.container,new DdaOngoingFragment()).commit();
            navigationView.getMenu().getItem(0).setChecked(true);
            getSupportActionBar().setTitle("Ongoing Locations");
            Toast.makeText(this,"Ongoing",Toast.LENGTH_LONG).show();
        }else if(id==R.id.completed_item){
            getSupportFragmentManager().beginTransaction().replace(R.id.container,new DdaCompletedFragment()).commit();
            navigationView.getMenu().getItem(1).setChecked(true);
            getSupportActionBar().setTitle("Completed Locations");
            Toast.makeText(this,"Completed",Toast.LENGTH_LONG).show();
        }else if(id==R.id.pending_item){
            getSupportFragmentManager().beginTransaction().replace(R.id.container,new DdaPendingFragment()).commit();
            navigationView.getMenu().getItem(2).setChecked(true);
            getSupportActionBar().setTitle("Pending Locations");
            Toast.makeText(this,"Pending",Toast.LENGTH_LONG).show();
        }else if(id==R.id.logout){
            SharedPreferences.Editor editor = getSharedPreferences("tokenFile", Context.MODE_PRIVATE).edit();
            editor.clear();
            editor.apply();
            Intent intent = new Intent(this, login_activity.class);
            startActivity(intent);
        }


        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout_dda);
        drawerLayout.closeDrawer(GravityCompat.START);

        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout_dda);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}