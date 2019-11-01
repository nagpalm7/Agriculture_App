package com.example.myapplication.Admin;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.androidnetworking.AndroidNetworking;
import com.example.myapplication.R;
import com.example.myapplication.login_activity;
import com.google.android.material.navigation.NavigationView;
import com.obsez.android.lib.filechooser.ChooserDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final String TAG = "AdminActivity";
    private final String ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private final String ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private final String READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    private final String WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final int RESULT_CODE = 786;
    private NavigationView navigationView;
    private boolean doubleBackToExitPressedOnce = false;
    private View header;
    private CircleImageView profile_pic;
    private String imagePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        AndroidNetworking.initialize(getApplicationContext());
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationView = findViewById(R.id.nav_view);
        header = navigationView.getHeaderView(0);
        profile_pic = header.findViewById(R.id.profile_pic);


        if (getPermission()) {
            navigationView.setCheckedItem(R.id.nav_home);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new map_fragemnt(), "MAP FRAGMENT").commit();
            getSupportActionBar().setTitle("HOME");
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        TextView user_name = headerView.findViewById(R.id.nameOfUserLoggedIn);
        SharedPreferences pref = getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
        String username = pref.getString("Name", "");
        user_name.setText(username.toUpperCase());
        getSupportActionBar().setTitle("HOME");
    }

    private boolean getPermission() {
        List<String> Permission = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Permission.add(ACCESS_FINE_LOCATION);
        }

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Permission.add(ACCESS_COARSE_LOCATION);
        }

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Permission.add(READ_EXTERNAL_STORAGE);
        }

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Permission.add(WRITE_EXTERNAL_STORAGE);
        }

        if (!Permission.isEmpty()) {
            String[] permissions = Permission.toArray(new String[Permission.size()]);
            ActivityCompat.requestPermissions(this, permissions, RESULT_CODE);
            return false;
        } else
            return true;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == RESULT_CODE) {
            HashMap<String, Integer> permissionResults = new HashMap<>();
            int deniedCount = 0;

            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    permissionResults.put(permissions[i], grantResults[i]);
                    deniedCount++;
                }
            }

            if (deniedCount == 0) {
                navigationView.setCheckedItem(R.id.nav_home);
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new map_fragemnt(), "MAP FRAGMENT").commit();

            } else {
                for (Map.Entry<String, Integer> entry : permissionResults.entrySet()) {
                    String permName = entry.getKey();
                    int permResult = entry.getValue();

                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, permName)) {
                        showDialog("", "This app needs location and files permissions to work without any problems.",
                                "Yes, Grant permissions",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        getPermission();
                                    }
                                },
                                "No, Exit app",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        finish();
                                    }
                                }, false);
                    } else {
                        showDialog("",
                                "You have denied some permissions. Allow all the permissions at [Setting] > [Permissions]",
                                "Go to Settings",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                                Uri.fromParts("package", getPackageName(), null));
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }, "No, Exit App",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        finish();
                                    }
                                }, false);
                        break;
                    }
                }
            }
        }
    }

    private AlertDialog showDialog(String title, String msg, String positiveLabel, DialogInterface.OnClickListener positiveOnclick,
                                   String negativeLabel, DialogInterface.OnClickListener negativeOnclick,
                                   boolean isCancelable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton(positiveLabel, positiveOnclick);
        builder.setNegativeButton(negativeLabel, negativeOnclick);
        builder.setCancelable(isCancelable);
        AlertDialog alert = builder.create();
        alert.show();
        return alert;
    }

    private void showMap() {
        Log.d(TAG, "showMap: getsupport");
        FragmentManager mfragmentmanager = getSupportFragmentManager();
        Log.d(TAG, "showMap: getsupport" + mfragmentmanager);
        mfragmentmanager.beginTransaction().replace(R.id.container, new map_fragemnt(), "MAP FRAGMENT").commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else
            if(!doubleBackToExitPressedOnce) {
                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "Please click BACK again to exit.", Toast.LENGTH_LONG).show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 3600);
            }
            else {
                super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_admin, menu);
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
            editor.clear();
            editor.commit();
            Intent intent = new Intent(AdminActivity.this, login_activity.class);
            startActivity(intent);
            finish();
            return true;
        }

        if(id == R.id.set_profilepic){
            openimagepicker();
        }

        if (id == R.id.download_report) {
            Intent intent = new Intent(this, DownloadReportActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void openimagepicker() {
        File file = Environment.getExternalStorageDirectory();
        String start = file.getAbsolutePath();
        new ChooserDialog(this)
                .withStartFile(start)
                .withChosenListener(new ChooserDialog.Result() {
                    @Override
                    public void onChoosePath(String s, File file) {
                        imagePath = file.getAbsolutePath();
                        setProfileImage();
                    }
                })
                .withOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        dialogInterface.cancel();
                    }
                })
                .build()
                .show();

    }

    private void setProfileImage() {

        Bitmap myBitmap = BitmapFactory.decodeFile(imagePath);
        profile_pic.setImageBitmap(myBitmap);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_upload) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new upload_fragment()).commit();
            getSupportActionBar().setTitle("UPLOAD");

        } else if (id == R.id.nav_ddo) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new ddo_fragment()).commit();
            getSupportActionBar().setTitle("DDA");

        } else if (id == R.id.nav_ado) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new ado_fragment()).commit();
            getSupportActionBar().setTitle("ADO");

        } else if (id == R.id.nav_location) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new location_fragment()).commit();
            getSupportActionBar().setTitle("LOCATIONS");

        } else if (id == R.id.nav_home) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new map_fragemnt(), "MAP FRAGMENT").commit();
            getSupportActionBar().setTitle("HOME");
        }


            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }


    @Override
    protected void onResume() {
        super.onResume();
    }
}