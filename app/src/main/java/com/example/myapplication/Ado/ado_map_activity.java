package com.example.myapplication.Ado;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.myapplication.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ado_map_activity extends AppCompatActivity {

    private final String ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private final String ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private final String CAMERA_PERMISSION = Manifest.permission.CAMERA;
    private double longitude;
    private double latitude;
    private String id;
    private Boolean showmap;

    private final int RESULT_CODE = 786;
    private GoogleMap map=null;
    private final String TAG= "ado_map_activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ado_map_activity);
        Intent intent = getIntent();
        longitude = Double.parseDouble(intent.getStringExtra("longitude"));
        latitude = Double.parseDouble(intent.getStringExtra("latitude"));
        id = intent.getStringExtra("id");
        String title = intent.getStringExtra("title");
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Log.d(TAG, "onCreate: "+latitude+" "+longitude);



        if(getPermission()){
            SupportMapFragment mapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.ado_map));

            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    Log.d("inside onMapReady", "onMapReady: ");
                    map=googleMap;

                    //get latlong for corners for specified city

                    LatLng one = new LatLng(7.798000, 68.14712);
                    LatLng two = new LatLng(37.090000, 97.34466);

                    LatLngBounds.Builder builder = new LatLngBounds.Builder();

                    //add them to builder
                    builder.include(one);
                    builder.include(two);

                    LatLngBounds bounds = builder.build();

                    //get width and height to current display screen
                    int width = getResources().getDisplayMetrics().widthPixels;
                    int height = getResources().getDisplayMetrics().heightPixels;

                    // 20% padding
                    int padding = (int) (width * 0.20);

                    //set latlong bounds
                    map.setLatLngBoundsForCameraTarget(bounds);

                    //move camera to fill the bound to screen
                    map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding));

                    //set zoom to level to current so that you won't be able to zoom out viz. move outside bounds
                    map.setMinZoomPreference(map.getCameraPosition().zoom);

                    //marking the position
                    map.addMarker(new MarkerOptions().position(new LatLng(latitude,longitude)).title("edar aaa").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

                }



            });
        }




    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private boolean getPermission() {
        List<String> Permission = new ArrayList();
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Permission.add(ACCESS_FINE_LOCATION);
        }

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Permission.add(ACCESS_COARSE_LOCATION);
        }

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), CAMERA_PERMISSION)
                != PackageManager.PERMISSION_GRANTED) {
            Permission.add(CAMERA_PERMISSION);
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
                SupportMapFragment mapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.ado_map));

                mapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        Log.d("inside onMapReady", "onMapReady: ");
                        map = googleMap;

                        //get latlong for corners for specified city

                        LatLng one = new LatLng(7.798000, 68.14712);
                        LatLng two = new LatLng(37.090000, 97.34466);

                        LatLngBounds.Builder builder = new LatLngBounds.Builder();

                        //add them to builder
                        builder.include(one);
                        builder.include(two);

                        LatLngBounds bounds = builder.build();

                        //get width and height to current display screen
                        int width = getResources().getDisplayMetrics().widthPixels;
                        int height = getResources().getDisplayMetrics().heightPixels;

                        // 20% padding
                        int padding = (int) (width * 0.20);

                        //set latlong bounds
                        map.setLatLngBoundsForCameraTarget(bounds);

                        //move camera to fill the bound to screen
                        map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding));

                        //set zoom to level to current so that you won't be able to zoom out viz. move outside bounds
                        map.setMinZoomPreference(map.getCameraPosition().zoom);

                        //marking the position
                        map.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("edar aaa").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

                    }


                });
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


    public void onClickNavigation(View view) {
        String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%f,%f (%s)", latitude, longitude, "Where the party is at");
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        startActivity(intent);
    }

    public void onClickCheckIn(View view) {
        Intent intent = new Intent(this, CheckInActivity.class);
        intent.putExtra("id", id);
        Log.d(TAG, "onClickCheckIn: " + id);
        startActivity(intent);
    }
}

