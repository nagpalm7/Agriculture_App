package com.example.myapplication.Dda;

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
import com.example.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DdaOngoingFragment extends Fragment {
    // TODO: Rename and change types and number of parameters
    private static final String TAG = "DdaOngoingFragment";
    private ArrayList<String> Date;
    private ArrayList<String> Time;
    private ArrayList<String> Address;
    private ArrayList<String> Id;
    private DdaongoingAdapter ddaongoingAdapter;
    private String url_get_ongoing = "http://13.235.100.235:8000/api/locations/dda/ongoing";
    private String next_url_get_ongoing;
    private String token;
    private String villagename;
    private String blockname;
    private String district;
    private String state;
    private ProgressBar progressBar;
    private LinearLayoutManager layoutManager;
    private boolean isNextBusy = false;
//    private Toolbar toolbar;

    public DdaOngoingFragment() {
        Date = new ArrayList<String>(3);
        Time = new ArrayList<String>(3);
        Address = new ArrayList<String>(3);
        Id = new ArrayList<String>(3);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ongoing,container,false);
        RecyclerView review = view.findViewById(R.id.recyclerViewongoing);
        ddaongoingAdapter = new DdaongoingAdapter(getActivity(),Date,Time,Address,Id);
        review.setAdapter(ddaongoingAdapter);
        layoutManager = new LinearLayoutManager(getActivity());
        review.setLayoutManager(layoutManager);
        progressBar = view.findViewById(R.id.ongoinglocationsloading);

        //showing title in action bar
//        toolbar = view.findViewById(R.id.toolbar);
//        getActivity().setTitle("Ongoing Locations");

        SharedPreferences preferences = getActivity().getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
        token = preferences.getString("token","");
        Log.d(TAG, "onCreateView: "+token);
        getRequestData();

        review.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int totalCount, pastCount, visibleCount;
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    totalCount = layoutManager.getItemCount();
                    pastCount = layoutManager.findFirstVisibleItemPosition();
                    visibleCount = layoutManager.getChildCount();
                    if ((pastCount + visibleCount) >= totalCount) {
                        if (!next_url_get_ongoing.equals("null") && !isNextBusy)
                            get_ddaongoing();
                        Log.d(TAG, "onScrolled:");
                    }
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        return view;
    }

    private void getRequestData(){
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url_get_ongoing, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject = new JSONObject(String.valueOf(response));
                    next_url_get_ongoing = jsonObject.getString("next");
                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject c = jsonArray.getJSONObject(i);
                        Id.add(c.getString("id"));
                        villagename = c.getString("village_name");
                        blockname = c.getString("block_name");
                        district = c.getString("district");
                        state = c.getString("state");
                        Date.add(c.getString("acq_date"));
                        Time.add(c.getString("acq_time"));
                        Address.add(villagename+", "+blockname+", "+district+", "+state);
                        Log.d(TAG, "onResponse: hello in for loop of urlgetongoing");
                        ddaongoingAdapter.notifyDataSetChanged();
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

        requestQueue.add(jsonObjectRequest);
    }

    private void get_ddaongoing(){
        RequestQueue requestQueue1 = Volley.newRequestQueue(getActivity());
        isNextBusy = true;
        progressBar.setVisibility(View.VISIBLE);
            JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(next_url_get_ongoing, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONObject rootObject = new JSONObject(String.valueOf(response));
                                next_url_get_ongoing = rootObject.getString("next");
                                JSONArray resultsArray = rootObject.getJSONArray("results");
                                for (int i = 0; i < resultsArray.length(); i++) {
                                    JSONObject singleObject = resultsArray.getJSONObject(i);
                                    Id.add(singleObject.getString("id"));
                                    Date.add(singleObject.getString("acq_date"));
                                    Time.add(singleObject.getString("acq_time"));
                                    String location = singleObject.getString("village_name") + ", " + singleObject.getString("block_name") + ", "
                                            + singleObject.getString("district") + ", " + singleObject.getString("state");
                                    Address.add(location);
                                    ddaongoingAdapter.notifyDataSetChanged();
                                    isNextBusy = false;
                                    Log.d(TAG, "onResponse: in next url");
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
            requestQueue1.add(jsonObjectRequest1);
        requestDdaOngoingFinished(requestQueue1);
    }

    private void requestDdaOngoingFinished(RequestQueue queue) {

        queue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {

            @Override
            public void onRequestFinished(Request<Object> request) {
                progressBar.setVisibility(View.GONE);
            }
        });

    }

}
