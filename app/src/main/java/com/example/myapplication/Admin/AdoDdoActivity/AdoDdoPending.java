package com.example.myapplication.Admin.AdoDdoActivity;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
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
import com.example.myapplication.Ado.AdoListAdapter;
import com.example.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdoDdoPending extends Fragment {

    private String mDdoId;
    private ArrayList<String> locationNames;
    private ArrayList<String> locationAddresses;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private LinearLayoutManager layoutManager;
    private AdoListAdapter adapter;
    private int NEXT_LOCATION_COUNT = 1;
    private String nextAssignedUrl;
    private String nextUnAssignedUrl;
    private String nextPendingUrl;  //for ADO
    private boolean isDdo;
    private String token;
    private View view;
    private String TAG="adoddopending";

    public AdoDdoPending() {
        // Required empty public constructor
    }

    public AdoDdoPending(String mDdoId, boolean isDdo) {
        this.mDdoId = mDdoId;
        this.isDdo = isDdo;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_ddo_pending, container, false);
        progressBar = view.findViewById(R.id.Ddo_pending_loading);
        recyclerView = view.findViewById(R.id.Ddo_pending_recyclerview);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        locationNames = new ArrayList<>();
        locationAddresses = new ArrayList<>();
        adapter = new AdoListAdapter(getActivity(), locationNames, locationAddresses);
        recyclerView.setAdapter(adapter);
        SharedPreferences prefs = getActivity().getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
        token = prefs.getString("token", "");
        String role;
        if (isDdo) {
            role = "dda";
            String mUrlAssigned = "http://13.235.100.235:8000/api/admin/" + role + "/" + mDdoId + "/assigned";
            String mUrlUnAssigned = "http://13.235.100.235:8000/api/admin/" + role + "/" + mDdoId + "/unassigned";
            Log.d("url", "onCreateView: pending" + mUrlAssigned);
            Log.d("url", "onCreateView: pending" + mUrlUnAssigned);
            getData(mUrlAssigned, true);

            getData(mUrlUnAssigned, false);
        } else {
            role = "ado";
            String mUrlPending = "http://13.235.100.235:8000/api/admin/" + role + "/" + mDdoId + "/pending";
            Log.d("url", "onCreateView: pending" + mUrlPending);
            getData(mUrlPending, true); //just passed true and Set it accordingly in the function called
        }

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                int totalCount, pastItemCount, visibleItemCount;
                if (dy > 0) {
                    totalCount = layoutManager.getItemCount();
                    pastItemCount = layoutManager.findFirstVisibleItemPosition();
                    visibleItemCount = layoutManager.getChildCount();
                    if ((pastItemCount + visibleItemCount) >= totalCount) {
                        loadNextLocations();
                    }
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        return view;
    }

    private void getData(String url, final boolean isAssigned) {
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject rootObject = new JSONObject(String.valueOf(response));
                            String nextUrl = rootObject.getString("next");
                            if (isAssigned) {
                                if (isDdo)
                                    nextAssignedUrl = nextUrl;
                                else
                                    nextPendingUrl = nextUrl;
                            }
                            else {nextUnAssignedUrl = nextUrl;}

                            JSONArray resultsArray = rootObject.getJSONArray("results");
                            Log.d(TAG, "onResponse: "+resultsArray.length());

                            for (int i = 0; i < resultsArray.length(); i++) {
                                JSONObject singleObject = resultsArray.getJSONObject(i);
                                String locName = singleObject.getString("village_name");
                                String locAdd = singleObject.getString("block_name") + ", " +
                                        singleObject.getString("block_name") + ", " + singleObject.getString("state");
                                locationNames.add(locName);
                                locationAddresses.add(locAdd);
                            }
                            if(resultsArray.length()== 0 && !(isAssigned)){
                                adapter.mshowshimmer = false;
                                adapter.notifyDataSetChanged();

                                view.setBackground(getActivity().getResources().getDrawable(R.drawable.no_entry_background));
                                //view.getView().setBackground(getActivity().getResources().getDrawable(R.drawable.no_entry_background));
                            }
                            adapter.mshowshimmer = false;
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("pending", "onErrorResponse: " + error);
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("Authorization", "Token " + token);
                return map;
            }
        };
        queue.add(jsonObjectRequest);
    }

    private void loadNextLocations() {
        if (isDdo) {
            switch (NEXT_LOCATION_COUNT) {
                case 1:
                    makeRequest(nextAssignedUrl);
                    NEXT_LOCATION_COUNT = 2;
                    break;
                case 2:
                    makeRequest(nextUnAssignedUrl);
                    NEXT_LOCATION_COUNT = 1;
                    break;
            }
        } else {
            makeRequest(nextPendingUrl);
        }
    }

    private void makeRequest(String url) {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        if (!url.isEmpty()) {
            progressBar.setVisibility(View.VISIBLE);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONObject rootObject = new JSONObject(String.valueOf(response));
                                JSONArray resultsArray = rootObject.getJSONArray("results");
                                for (int i = 0; i < resultsArray.length(); i++) {
                                    JSONObject singleObject = resultsArray.getJSONObject(i);
                                    String locName = singleObject.getString("village_name");
                                    String locAdd = singleObject.getString("block_name") +
                                            singleObject.getString("block_name") + singleObject.getString("state");
                                    locationNames.add(locName);
                                    locationAddresses.add(locAdd);
                                    adapter.notifyDataSetChanged();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("Authorization", "Token " + token);
                    return map;
                }
            };
            queue.add(jsonObjectRequest);
            requestFinished(queue);
        }

    }

    private void requestFinished(RequestQueue queue) {

        queue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {

            @Override
            public void onRequestFinished(Request<Object> request) {
                progressBar.setVisibility(View.GONE);
            }
        });

    }

}
