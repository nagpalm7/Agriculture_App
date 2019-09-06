package com.example.myapplication;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.example.myapplication.Admin.AdminLocationAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class OnGoingFragment extends Fragment {

    private String ongoingUrl = "http://13.235.100.235:8000/api/locations/ongoing";

    private ArrayList<String> mIdList;
    private ArrayList<String> mDateList;
    private ArrayList<String> mTimeList;
    private ArrayList<String> mLocationList;
    private AdminLocationAdapter adapter;
    private LinearLayoutManager layoutManager;
    private String token;
    private String next_ongoing_url;
    private ProgressBar progressBar;

    public OnGoingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_on_going, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.ongoing_recyclerview);
        progressBar = view.findViewById(R.id.locations_loading_ongoing);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        mIdList = new ArrayList<>();
        mDateList = new ArrayList<>();
        mTimeList = new ArrayList<>();
        mLocationList = new ArrayList<>();
        adapter = new AdminLocationAdapter(getActivity(), mIdList, mDateList, mTimeList, mLocationList);
        recyclerView.setAdapter(adapter);
        final SharedPreferences preferences = getActivity().getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
        token = preferences.getString("token", "");
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
                        progressBar.setVisibility(View.VISIBLE);
                        get_Ongoing();
                    }
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        return view;
    }

    private void getData() {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(ongoingUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject rootObject = new JSONObject(String.valueOf(response));
                            next_ongoing_url = rootObject.getString("next");
                            JSONArray resultsArray = rootObject.getJSONArray("results");
                            for (int i = 0; i < resultsArray.length(); i++) {
                                JSONObject singleObject = resultsArray.getJSONObject(i);
                                mIdList.add(singleObject.getString("id"));
                                mDateList.add(singleObject.getString("acq_date"));
                                mTimeList.add(singleObject.getString("acq_time"));
                                String location = singleObject.getString("village_name") + ", " + singleObject.getString("block_name") + ", "
                                        + singleObject.getString("district") + ", " + singleObject.getString("state");
                                mLocationList.add(location);
                            }
                            adapter.notifyDataSetChanged();
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
        requestQueue.add(jsonObjectRequest);
    }

    private void get_Ongoing() {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        if (next_ongoing_url != null || !next_ongoing_url.isEmpty()) {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(ongoingUrl, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONObject rootObject = new JSONObject(String.valueOf(response));
                                next_ongoing_url = rootObject.getString("next");
                                JSONArray resultsArray = rootObject.getJSONArray("results");
                                for (int i = 0; i < resultsArray.length(); i++) {
                                    JSONObject singleObject = resultsArray.getJSONObject(i);
                                    mIdList.add(singleObject.getString("id"));
                                    mDateList.add(singleObject.getString("acq_date"));
                                    mTimeList.add(singleObject.getString("acq_time"));
                                    String location = singleObject.getString("village_name") + ", " + singleObject.getString("block_name") + ", "
                                            + singleObject.getString("district") + ", " + singleObject.getString("state");
                                    mLocationList.add(location);
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
            requestQueue.add(jsonObjectRequest);
        }
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
