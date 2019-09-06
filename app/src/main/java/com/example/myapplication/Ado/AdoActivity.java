package com.example.myapplication.Ado;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.Switch;
import android.widget.Toast;

import com.example.myapplication.R;
import com.google.android.material.navigation.NavigationView;

public class AdoActivity extends AppCompatActivity {


    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ado);
        Toast.makeText(this, "Ado successfully logged in..", Toast.LENGTH_LONG).show();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportFragmentManager().beginTransaction().replace(R.id.container_ado, new ado_pending_fragment());


        NavigationView navigationView = findViewById(R.id.nav_view);
        drawer = findViewById(R.id.drawer_layout1);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {

                    case R.id.nav_home1:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container_ado, new ado_pending_fragment()).commit();
                        break;
                    case R.id.nav_gallery1:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container_ado,new ado_ongoing_fragment()).commit();
                        break;
                    case R.id.nav_slideshow1:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container_ado, new ado_complete_fragment()).commit();
                        break;
                }

                drawer.closeDrawer(GravityCompat.START);


                return true;
            }
        });

        ActionBarDrawerToggle toogle= new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toogle);
        toogle.syncState();



    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }


    }
}
