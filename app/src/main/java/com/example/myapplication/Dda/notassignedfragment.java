package com.example.myapplication.Dda;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

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

public class notassignedfragment extends Fragment {

    private static final String TAG = "notassignedfragment";
    private ArrayList<String>Id;
    private ArrayList<String>Date;
    private ArrayList<String>Time;
    private ArrayList<String> Address;
    private DdapendingUnassignedAdapter ddapendingUnassignedAdapter;
    private String urlget = "http://13.235.100.235:8000/api/locations/dda/unassigned";
    private String villagename;
    private String blockname;
    private String district;
    private String state;
    private String token;
    private View view;
    private View v;

    SwipeRefreshLayout swipeRefreshLayout;

    public notassignedfragment(){
        Id = new ArrayList<String>(3);
        Date = new ArrayList<String>(3);
        Time = new ArrayList<String>(3);
        Address = new ArrayList<String>(3);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_notassignedfragment, container, false);
        v = inflater.inflate(R.layout.notassignedlist,container,false);
        swipeRefreshLayout = v.findViewById(R.id.swiperefresh);
        ddapendingUnassignedAdapter = new DdapendingUnassignedAdapter(getActivity(),Id,Date,Time, Address);
        RecyclerView notassignedreview = view.findViewById(R.id.recyclerViewnotassigned);
        notassignedreview.setAdapter(ddapendingUnassignedAdapter);
        notassignedreview.setLayoutManager(new LinearLayoutManager(getActivity()));

        SharedPreferences preferences = getActivity().getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
        token = preferences.getString("token","");
        Log.d(TAG, "onCreateView: "+token);
        view = getdata();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                view = getdata();
            }
        });

        return view;
    }
    private View getdata(){
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
                        Id.add(c.getString("id"));
                        Date.add(c.getString("acq_date"));
                        Time.add(c.getString("acq_time"));
                        Address.add(villagename+","+blockname+","+district+","+state);

                        ddapendingUnassignedAdapter.notifyDataSetChanged();
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
        if(swipeRefreshLayout.isRefreshing()){
            swipeRefreshLayout.setRefreshing(false);
        }
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_refresh:
                Log.i(TAG, "onOptionsItemSelected: Refresh menu item selected");
                swipeRefreshLayout.setRefreshing(true);
                getdata();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
