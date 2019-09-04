package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class notassignedfragment extends Fragment {

    private static final String TAG = "notassignedfragment";
    private ArrayList<String>locationid;
    private ArrayList<String>latitude;
    private ArrayList<String>longitude;
    private ArrayList<String> address;
    private DdapendingAdapter notassignedDdapendingadapter;

    public notassignedfragment(){
        locationid = new ArrayList<String>(3);
        latitude = new ArrayList<String>(3);
        longitude = new ArrayList<String>(3);
        address = new ArrayList<String>(3);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG, "onCreateView: in notassignedfragment");
        View view = inflater.inflate(R.layout.fragment_notassignedfragment, container, false);
            locationid.add("L Id:");
            latitude.add("Latitude:");
            longitude.add("Longitude:");
            address.add("Address:");
        notassignedDdapendingadapter = new DdapendingAdapter(getActivity(),locationid,latitude,longitude, address);
        RecyclerView notassignedreview = view.findViewById(R.id.recyclerViewnotassigned);
        notassignedreview.setAdapter(notassignedDdapendingadapter);
        notassignedreview.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }
}
