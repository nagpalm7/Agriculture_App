package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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


public class pending_fragment extends Fragment {

    //vars
    private ArrayList<String> Id;
    private ArrayList<String> Date;
    private ArrayList<String> Time;
    private ArrayList<String> Address;
    private String token;
    private String villagename;
    private String blockname;
    private String district;
    private String state;

    //tags
    private static final String TAG = "pending_fragment";
    private String url_unassigned = "http://13.235.100.235:8000/api/locations/unassigned";
    private String url_assigned = "http://13.235.100.235:8000/api/locations/assigned";
    private String url_ongoing = "http://13.235.100.235:8000/api/locations/ongoing";

    public pending_fragment() {
        Id = new ArrayList<String>();
        Date = new ArrayList<String>();
        Time = new ArrayList<String>();
        Address = new ArrayList<String>();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pending_fragment, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewpending);
        final DdapendingAdapter recyclerViewAdater = new DdapendingAdapter(getContext(), Id, Date, Time, Address);
        recyclerView.setAdapter(recyclerViewAdater);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        SharedPreferences preferences = getActivity().getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
        token = preferences.getString("token", "");
        Log.d(TAG, "onCreateView: " + token);

        Log.d(TAG, "onCreateView: inflated fragment_ongoing");

        final RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        final JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(Request.Method.GET, url_unassigned, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject = new JSONObject(String.valueOf(response));
                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject c = jsonArray.getJSONObject(i);
                        villagename = c.getString("village_name");
                        blockname = c.getString("block_name");
                        district = c.getString("district");
                        state = c.getString("state");
                        Id.add(c.getString("id"));
                        Date.add(c.getString("acq_date"));
                        Time.add(c.getString("acq_time"));
                        Address.add(villagename + "," + blockname + "," + district + "," + state);

                        recyclerViewAdater.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "onResponse: " + e.getLocalizedMessage());
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: " + error);
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("Authorization", "Token " + token);
                return map;
            }
        };

        final JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest(Request.Method.GET, url_assigned, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject = new JSONObject(String.valueOf(response));
                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject c = jsonArray.getJSONObject(i);
                        villagename = c.getString("village_name");
                        blockname = c.getString("block_name");
                        district = c.getString("district");
                        state = c.getString("state");
                        Id.add(c.getString("id"));
                        Date.add(c.getString("acq_date"));
                        Time.add(c.getString("acq_time"));
                        Address.add(villagename + "," + blockname + "," + district + "," + state);

                        recyclerViewAdater.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "onResponse: " + e.getLocalizedMessage());
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: " + error);
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("Authorization", "Token " + token);
                return map;
            }
        };

        final JsonObjectRequest jsonObjectRequest3 = new JsonObjectRequest(Request.Method.GET, url_ongoing, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject = new JSONObject(String.valueOf(response));
                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject c = jsonArray.getJSONObject(i);
                        villagename = c.getString("village_name");
                        blockname = c.getString("block_name");
                        district = c.getString("district");
                        state = c.getString("state");
                        Id.add(c.getString("id"));
                        Date.add(c.getString("acq_date"));
                        Time.add(c.getString("acq_time"));
                        Address.add(villagename + "," + blockname + "," + district + "," + state);

                        recyclerViewAdater.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "onResponse: " + e.getLocalizedMessage());
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: " + error);
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("Authorization", "Token " + token);
                return map;
            }
        };


        requestQueue.add(jsonObjectRequest1);
        requestQueue.add(jsonObjectRequest2);
        requestQueue.add(jsonObjectRequest3);

        return view;
    }
}

