package com.example.myapplication.Admin;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class pending_fragment extends Fragment {

    //vars
    private ArrayList<String> mDdaName;
    private ArrayList<String> mAdaName;
    private ArrayList<String> mAddress;
    private String token;
    private String villagename;
    private String blockname;
    private String district;
    private String state;

    //tags
    private static final String TAG = "pending_fragment";
    private String url_unassigned = "http://13.235.100.235:8000/api/locations/unassigned";
    private String url_assigned = "http://13.235.100.235:8000/api/locations/assigned";
    private String next_unassigned_url;
    private String next_assigned_url;
    private LinearLayoutManager layoutManager;
    private AdminLocationAdapter recyclerViewAdater;
    private ProgressBar progressBar;
    private int NEXT_LOCATION_COUNT = 1;
    private boolean isNextBusy;
    private View view;

    public pending_fragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.pending_fragment, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewpending);
        progressBar = view.findViewById(R.id.locations_loading);
        mDdaName = new ArrayList<>();
        mAdaName = new ArrayList<>();
        mAddress = new ArrayList<>();
        recyclerViewAdater = new AdminLocationAdapter(getActivity(), mDdaName, mAdaName, mAddress);
        recyclerView.setAdapter(recyclerViewAdater);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        final SharedPreferences preferences = getActivity().getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
        token = preferences.getString("token", "");
        Log.d(TAG, "onCreateView: " + token);

        Log.d(TAG, "onCreateView: inflated fragment_ongoing");

        final RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        final JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest(Request.Method.GET, url_assigned, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject = new JSONObject(String.valueOf(response));
                    next_assigned_url = jsonObject.getString("next");
                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    if(jsonArray.length()== 0){
                        recyclerViewAdater.mShowShimmer = false;
                        recyclerViewAdater.notifyDataSetChanged();

                        view.setBackground(getActivity().getResources().getDrawable(R.drawable.no_entry_background));
                        //view.getView().setBackground(getActivity().getResources().getDrawable(R.drawable.no_entry_background));
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject c = jsonArray.getJSONObject(i);
                        villagename = c.getString("village_name");
                        blockname = c.getString("block_name");
                        district = c.getString("district");
                        state = c.getString("state");
                        JSONObject mDdaObject = c.getJSONObject("dda");
                        String ddaName = mDdaObject.getString("name");
                        mDdaName.add(ddaName);
                        Log.d(TAG, "onResponse: DDA NAME " + ddaName);
                        try {
                            JSONObject mAdoObject = c.getJSONObject("ado");
                            Log.d(TAG, "onResponse: try block");
                            String adoName = mAdoObject.getString("name");
                            Log.d(TAG, "onResponse: adoname " + adoName);
                            mAdaName.add(adoName);
                        } catch (JSONException e) {
                            mAdaName.add("Not Assigned");
                            Log.d(TAG, "exception: ");
                        }
                        mAddress.add(villagename + "," + blockname + "," + district + "," + state);
                        Log.d(TAG, "onResponse: next");
                        recyclerViewAdater.mShowShimmer = false;
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

        final JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(Request.Method.GET, url_unassigned, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject = new JSONObject(String.valueOf(response));
                    next_unassigned_url = jsonObject.getString("next");
                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject c = jsonArray.getJSONObject(i);
                        villagename = c.getString("village_name");
                        blockname = c.getString("block_name");
                        district = c.getString("district");
                        state = c.getString("state");
                        JSONObject mDdaObject = c.getJSONObject("dda");
                        String ddaName = mDdaObject.getString("name");
                        mDdaName.add(ddaName);
                        Log.d(TAG, "onResponse: DDA NAME " + ddaName);
                        try {
                            JSONObject mAdoObject = c.getJSONObject("ado");
                            String adoName = mAdoObject.getString("name");
                            mAdaName.add(adoName);
                        } catch (JSONException e) {
                            mAdaName.add("Not Assigned");
                        }
                        mAddress.add(villagename + "," + blockname + "," + district + "," + state);
                    }
                    requestQueue.add(jsonObjectRequest2);
                } catch (JSONException e) {
                    Log.e(TAG, "onResponse: " + e.getLocalizedMessage());
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NoConnectionError)
                    Toast.makeText(getActivity(), "Check Your Internt Connection Please!", Toast.LENGTH_SHORT).show();
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

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int totalCount, pastItemCount, visibleItemCount;

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    totalCount = layoutManager.getItemCount();
                    pastItemCount = layoutManager.findFirstVisibleItemPosition();
                    visibleItemCount = layoutManager.getChildCount();
                    if ((pastItemCount + visibleItemCount) >= totalCount) {
                        if (!isNextBusy)
                            loadNextLocations();
                    }
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        return view;
    }

    private void loadNextLocations() {
        switch (NEXT_LOCATION_COUNT) {
            case 1:
                if (!next_unassigned_url.equals("null"))
                    get_Unassigned();
                NEXT_LOCATION_COUNT = 2;
                break;
            case 2:
                if (!next_assigned_url.equals("null"))
                    get_Assigned();
                NEXT_LOCATION_COUNT = 1;
                break;
        }

    }

    private void get_Unassigned() {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        progressBar.setVisibility(View.VISIBLE);
        isNextBusy = true;
            final JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(Request.Method.GET, next_unassigned_url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONObject jsonObject = new JSONObject(String.valueOf(response));
                        JSONArray jsonArray = jsonObject.getJSONArray("results");
                        next_unassigned_url = jsonObject.getString("next");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject c = jsonArray.getJSONObject(i);
                            villagename = c.getString("village_name");
                            blockname = c.getString("block_name");
                            district = c.getString("district");
                            state = c.getString("state");
                            JSONObject mDdaObject = c.getJSONObject("dda");
                            String ddaName = mDdaObject.getString("name");
                            mDdaName.add(ddaName);
                            try {
                                JSONObject mAdoObject = c.getJSONObject("ado");
                                String adoName = mAdoObject.getString("name");
                                mAdaName.add(adoName);
                            } catch (JSONException e) {
                                mAdaName.add("Not Assigned");
                            }
                            mAddress.add(villagename + "," + blockname + "," + district + "," + state);
                            recyclerViewAdater.notifyDataSetChanged();
                            isNextBusy = false;
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, "onResponse: " + e.getLocalizedMessage());
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error instanceof NoConnectionError)
                        Toast.makeText(getActivity(), "Check Your Internt Connection Please!", Toast.LENGTH_SHORT).show();
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
        requestFinished(requestQueue);
    }

    private void get_Assigned() {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        progressBar.setVisibility(View.VISIBLE);
            final JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest(Request.Method.GET, next_assigned_url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONObject jsonObject = new JSONObject(String.valueOf(response));
                        JSONArray jsonArray = jsonObject.getJSONArray("results");
                        next_assigned_url = jsonObject.getString("next");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject c = jsonArray.getJSONObject(i);
                            villagename = c.getString("village_name");
                            blockname = c.getString("block_name");
                            district = c.getString("district");
                            state = c.getString("state");
                            JSONObject mDdaObject = c.getJSONObject("dda");
                            String ddaName = mDdaObject.getString("name");
                            mDdaName.add(ddaName);
                            try {
                                JSONObject mAdoObject = c.getJSONObject("ado");
                                String adoName = mAdoObject.getString("name");
                                mAdaName.add(adoName);
                            } catch (JSONException e) {
                                mAdaName.add("Not Assigned");
                            }
                            mAddress.add(villagename + "," + blockname + "," + district + "," + state);
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
                    if (error instanceof NoConnectionError)
                        Toast.makeText(getActivity(), "Check Your Internt Connection Please!", Toast.LENGTH_SHORT).show();
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
            requestQueue.add(jsonObjectRequest2);
        requestFinished(requestQueue);
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

