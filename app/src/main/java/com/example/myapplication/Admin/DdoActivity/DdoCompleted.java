package com.example.myapplication.Admin.DdoActivity;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

/**
 * A simple {@link Fragment} subclass.
 */
public class DdoCompleted extends Fragment {

    private String mDdoId;
    private ArrayList<String> locationNames;
    private ArrayList<String> locationAddresses;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private LinearLayoutManager layoutManager;
    private AdoListAdapter adapter;
    private String nextUrl;


    public DdoCompleted() {
        // Required empty public constructor
    }

    public DdoCompleted(String mDdoId) {
        this.mDdoId = mDdoId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ddo_completed, container, false);
        String mUrl = "http://13.235.100.235:8000/api/admin/dda/" + mDdoId + "/completed";
        progressBar = view.findViewById(R.id.Ddo_completed_loading);
        recyclerView = view.findViewById(R.id.Ddo_completed_recyclerview);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        locationNames = new ArrayList<>();
        locationAddresses = new ArrayList<>();
        adapter = new AdoListAdapter(getActivity(), locationNames, locationAddresses);
        recyclerView.setAdapter(adapter);
        getData(mUrl);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                int totalCount, pastItemCount, visibleItemCount;
                if (dy > 0) {
                    totalCount = layoutManager.getItemCount();
                    pastItemCount = layoutManager.findFirstVisibleItemPosition();
                    visibleItemCount = layoutManager.getChildCount();
                    if ((pastItemCount + visibleItemCount) >= totalCount) {
                        loadNextLocations(nextUrl);
                    }
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        return view;
    }

    private void getData(String url) {
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject rootObject = new JSONObject(String.valueOf(response));
                            nextUrl = rootObject.getString("next");
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
                });
        queue.add(jsonObjectRequest);
    }

    private void loadNextLocations(String url) {
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
                    });
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
