package com.example.myapplication.Ado;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
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

public class ado_pending_fragment extends Fragment {

    private ArrayList<String> mtextview1;
    private ArrayList<String> mtextview2;
    private RecyclerView recyclerView;
    private AdoListAdapter adoListAdapter;
    private ArrayList<String> longitude;
    private ArrayList<String> latitude;
    private String url="http://13.235.100.235:8000/api/locations/ado/pending";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ado_pending_fragment,container,false);
        mtextview1 = new ArrayList<>();
        mtextview2 = new ArrayList<>();
        longitude = new ArrayList<>();
        latitude = new ArrayList<>();

        //add data in the array with load data
        getData();

        recyclerView = view.findViewById(R.id.ado_pending_rv);
        adoListAdapter = new AdoListAdapter(getContext(),mtextview1,mtextview2);
        DividerItemDecoration divider = new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL);
        recyclerView.addItemDecoration(divider);
        recyclerView.setAdapter(adoListAdapter);
        recyclerView.setLayoutManager( new LinearLayoutManager(getActivity()));


        return view;
    }


    private void getData() {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject rootObject = new JSONObject(String.valueOf(response));
                            JSONArray resultsArray = rootObject.getJSONArray("results");
                            for (int i = 0; i < resultsArray.length(); i++) {
                                JSONObject singleObject = resultsArray.getJSONObject(i);

                                String location_name = singleObject.getString("village_name");
                                String location_address = singleObject.getString("block_name")+singleObject.getString("district")+singleObject.getString("state");
                                String slongitude = singleObject.getString("longitude");
                                String slatitude = singleObject.getString("latitude");
                                mtextview1.add(location_name);
                                mtextview2.add(location_address);
                                longitude.add(slongitude);
                                latitude.add(slatitude);
                                adoListAdapter.sendPostion(longitude,latitude);



                            }

                            adoListAdapter.mshowshimmer = false;
                            adoListAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Fragment fragment = getFragmentManager().findFragmentById(R.id.rootView);
                            fragment.getView().setBackground(getActivity().getResources().getDrawable(R.drawable.no_entry_background));


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
                SharedPreferences prefs = getActivity().getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
                String token = prefs.getString("token", "");
                map.put("Authorization", "Token " + token);
                return map;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }
}
