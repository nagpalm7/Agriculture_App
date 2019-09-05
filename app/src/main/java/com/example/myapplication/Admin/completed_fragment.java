package com.example.myapplication.Admin;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Admin.AdminLocationAdapter;
import com.example.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class completed_fragment extends Fragment {
    private RecyclerView recyclerView;
    private ArrayList<String> mIdList;
    private ArrayList<String> mDateList;
    private ArrayList<String> mTimeList;
    private ArrayList<String> mLocationList;
    private AdminLocationAdapter adapter;
    private String unassignedUrl = "http://13.235.100.235:8000/api/locations/unassigned";
    private String assignedUrl = "http://13.235.100.235:8000/api/locations/assigned";

    private String completedUrl = "http://13.235.100.235:8000/api/locations/completed";
    private String nextUrl;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.completed_fragment,container,false);
        recyclerView = view.findViewById(R.id.completed_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mIdList = new ArrayList<>();
        mDateList = new ArrayList<>();
        mTimeList = new ArrayList<>();
        mLocationList = new ArrayList<>();
        adapter = new AdminLocationAdapter(getActivity(), mIdList, mDateList, mTimeList, mLocationList);
        recyclerView.setAdapter(adapter);
        getData();
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
                SharedPreferences prefs = getActivity().getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
                String token = prefs.getString("token", "");
                map.put("Authorization", "Token " + token);
                return map;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }
}
