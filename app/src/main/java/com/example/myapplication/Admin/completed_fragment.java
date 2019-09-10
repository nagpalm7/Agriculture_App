package com.example.myapplication.Admin;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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

public class completed_fragment extends Fragment {
    private RecyclerView recyclerView;
    private ArrayList<String> mDdaNames;
    private ArrayList<String> mAdoNames;
    private ArrayList<String> mAddresses;
    private AdminLocationAdapter adapter;
    private LinearLayoutManager layoutManager;
    private String completedUrl = "http://13.235.100.235:8000/api/locations/completed";
    private String nextUrl;
    private String token;
    private ProgressBar progressBar;
    private boolean isNextBusy;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.completed_fragment, container, false);
        recyclerView = view.findViewById(R.id.completed_recyclerview);
        progressBar = view.findViewById(R.id.locations_loading_completed);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        mDdaNames = new ArrayList<>();
        mAdoNames = new ArrayList<>();
        mAddresses = new ArrayList<>();
        adapter = new AdminLocationAdapter(getActivity(), mDdaNames, mAdoNames, mAddresses);
        recyclerView.setAdapter(adapter);
        SharedPreferences prefs = getActivity().getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
        token = prefs.getString("token", "");
        getData();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int totalCount, pastItemCount, visibleItemCount;

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {

                if (dy > 0) {
                    totalCount = layoutManager.getItemCount();
                    pastItemCount = layoutManager.findFirstVisibleItemPosition();
                    visibleItemCount = layoutManager.getChildCount();
                    if ((pastItemCount + visibleItemCount) >= totalCount) {
                        if (!isNextBusy && nextUrl.equals("null"))
                            getNextLocations();
                    }
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        return view;
    }

    private void getData() {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(completedUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject rootObject = new JSONObject(String.valueOf(response));
                            nextUrl = rootObject.getString("next");
                            JSONArray resultsArray = rootObject.getJSONArray("results");
                            for (int i = 0; i < resultsArray.length(); i++) {
                                JSONObject singleObject = resultsArray.getJSONObject(i);
                                mDdaNames.add(singleObject.getString("dda"));
                                String adoName = singleObject.getString("ado");
                                if (adoName.isEmpty())
                                    mAdoNames.add("Not Assigned");
                                else
                                    mAdoNames.add(adoName);
                                String location = singleObject.getString("village_name") + ", " + singleObject.getString("block_name") + ", "
                                        + singleObject.getString("district") + ", " + singleObject.getString("state");
                                mAddresses.add(location);

                            }
                            adapter.mShowShimmer = false;
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof NoConnectionError)
                            Toast.makeText(getActivity(), "Check Your Internt Connection Please!", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("Authorization", "Token " + token);
                return map;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    private void getNextLocations() {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        isNextBusy = true;
        progressBar.setVisibility(View.VISIBLE);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(nextUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject rootObject = new JSONObject(String.valueOf(response));
                            nextUrl = rootObject.getString("next");
                            JSONArray resultsArray = rootObject.getJSONArray("results");
                            for (int i = 0; i < resultsArray.length(); i++) {
                                JSONObject singleObject = resultsArray.getJSONObject(i);
                                mDdaNames.add(singleObject.getString("dda"));
                                String adoName = singleObject.getString("ado");
                                if (adoName.isEmpty())
                                    mAdoNames.add("Not Assigned");
                                else
                                    mAdoNames.add(adoName);
                                String location = singleObject.getString("village_name") + ", " + singleObject.getString("block_name") + ", "
                                        + singleObject.getString("district") + ", " + singleObject.getString("state");
                                mAddresses.add(location);
                            }
                            adapter.notifyDataSetChanged();
                            isNextBusy = false;

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof NoConnectionError)
                            Toast.makeText(getActivity(), "Check Your Internt Connection Please!", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("Authorization", "Token " + token);
                return map;
            }
        };
        requestQueue.add(jsonObjectRequest);
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
