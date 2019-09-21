package com.example.myapplication.Ado;

import android.app.IntentService;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;


class GeofenceTransitionService extends IntentService {

    public GeofenceTransitionService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);

        if(geofencingEvent.hasError()){
            String error= String.valueOf(geofencingEvent.getErrorCode());
            Toast.makeText(getApplicationContext(),"error code:"+error,Toast.LENGTH_LONG);
            return;
        }

        int geofencingTransition = geofencingEvent.getGeofenceTransition();

        if (geofencingTransition == Geofence.GEOFENCE_TRANSITION_ENTER){

            ado_map_activity obj = new ado_map_activity();
            obj.getStatus(true);
        }

    }
}
