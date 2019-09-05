package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class notassignedfragment extends Fragment {

    private static final String TAG = "notassignedfragment";
    private ArrayList<String>Date;
    private ArrayList<String>Time;
    private ArrayList<String> Address;
    private DdapendingAdapter notassignedDdapendingadapter;
    private String urlget = "http://13.235.100.235:8000/api/locations/dda/unassigned";
    private String villagename;
    private String blockname;
    private String district;
    private String state;
    private String token;

    public notassignedfragment(){
        Date = new ArrayList<String>(3);
        Time = new ArrayList<String>(3);
        Address = new ArrayList<String>(3);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG, "onCreateView: in notassignedfragment");
        View view = inflater.inflate(R.layout.fragment_notassignedfragment, container, false);
        notassignedDdapendingadapter = new DdapendingAdapter(getActivity(),Date,Time, Address);
        RecyclerView notassignedreview = view.findViewById(R.id.recyclerViewnotassigned);
        notassignedreview.setAdapter(notassignedDdapendingadapter);
        notassignedreview.setLayoutManager(new LinearLayoutManager(getActivity()));

        SharedPreferences preferences = getActivity().getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
        token = preferences.getString("token","");
        Log.d(TAG, "onCreateView: "+token);

        final RequestQueue unassignedrequestqueue = Volley.newRequestQueue(getActivity());

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlget, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject = new JSONObject(String.valueOf(response));
                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject c = jsonArray.getJSONObject(i);
                        villagename = c.getString("village_name");
                        blockname = c.getString("block_name");
                        district = c.getString("district");
                        state = c.getString("state");
                        Date.add(c.getString("acq_date"));
                        Time.add(c.getString("acq_time"));
                        Address.add(villagename+","+blockname+","+district+","+state);

                        notassignedDdapendingadapter.notifyDataSetChanged();
                    }
                }catch (JSONException e){
                    Log.e(TAG, "onResponse: " + e.getLocalizedMessage());
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: " + error );
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("Authorization", "Token " + token);
                return map;
            }
        };

        unassignedrequestqueue.add(jsonObjectRequest);

        return view;
    }
}
