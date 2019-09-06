package com.example.myapplication.Admin;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.example.myapplication.R;
import com.example.myapplication.login_activity;
import com.google.android.material.navigation.NavigationView;

public class AdminActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final String TAG = "AdminActivity";
    private final String ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private final String ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private final String READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    private final String WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final int RESULT_CODE = 786;

    //var
    private Boolean PERMISSION_GRANTED = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getPermission();

        if (PERMISSION_GRANTED) {
            Log.d(TAG, "onCreate: permission" + PERMISSION_GRANTED);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new map_fragemnt()).commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void getPermission() {
        String[] permission = {ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                if (ContextCompat.checkSelfPermission(this.getApplicationContext(), READ_EXTERNAL_STORAGE) ==
                        PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this.getApplicationContext(), WRITE_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {
                        //all permission are granted
                        PERMISSION_GRANTED = true;
                    } else {
                        ActivityCompat.requestPermissions(this, permission, RESULT_CODE);
                    }
                } else {
                    ActivityCompat.requestPermissions(this, permission, RESULT_CODE);
                }
            } else {
                ActivityCompat.requestPermissions(this, permission, RESULT_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this, permission, RESULT_CODE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PERMISSION_GRANTED = false;

        switch (requestCode) {
            case RESULT_CODE: {
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        //user again denied the permission
                        Log.d(TAG, "onRequestPermissionsResult: user denied the permission");
                        PERMISSION_GRANTED = false;
                    } else {
                        //permission granted
                        Log.d(TAG, "onRequestPermissionsResult: permission granted");
                        PERMISSION_GRANTED = true;
                        //call a method
                        showMap();
                    }
                }


            }
        }
    }

    private void showMap() {
        Log.d(TAG, "showMap: getsupport");
        FragmentManager mfragmentmanager = getSupportFragmentManager();
        Log.d(TAG, "showMap: getsupport" + mfragmentmanager);
        mfragmentmanager.beginTransaction().replace(R.id.container, new map_fragemnt()).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            SharedPreferences.Editor editor = getSharedPreferences("tokenFile", MODE_PRIVATE).edit();
            editor.remove("token");
            editor.commit();
            Intent intent = new Intent(AdminActivity.this, login_activity.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_upload) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new upload_fragment()).commit();

        } else if (id == R.id.nav_ddo) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new ddo_fragment()).commit();


        } else if (id == R.id.nav_ado) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container,new ado_fragment()).commit();


        } else if (id == R.id.nav_location) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container,new location_fragment()).commit();


        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_home) {

            if (PERMISSION_GRANTED) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new map_fragemnt()).commit();
            } else {
                Toast.makeText(this, "ACCESS DENIED", Toast.LENGTH_SHORT).show();
            }
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}