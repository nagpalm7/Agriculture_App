package com.example.myapplication.Dda;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
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

public class adounderddo extends Fragment {

    RecyclerView recyclerView;
    ArrayList<String> ado_names;
    private String urlget = "http://13.235.100.235/api/ado/";
    private adounderddoadapter adapter;
    private final String TAG ="adouderddo";
    private String token;
    private boolean isNextBusy = false;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.adounderddo_list,container,false);
        ado_names = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recyclerViewadounderddo);
        adapter  = new adounderddoadapter(getContext(),ado_names);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        DividerItemDecoration divider = new DividerItemDecoration(getActivity(), linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(divider);

        SharedPreferences preferences = getActivity().getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
        token = preferences.getString("token", "");
        loadData(urlget);

        return view;
    }

    private void loadData(String url){
        final RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        isNextBusy = true;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject = new JSONObject(String.valueOf(response));

                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject c =jsonArray.getJSONObject(i);

                        ado_names.add(c.getString("name"));
                        JSONArray villageArray = c.getJSONArray("village");

                    }
                    isNextBusy = false;
                    adapter.notifyDataSetChanged();
                }catch (JSONException e){
                    Log.d(TAG, "onResponse: "+e);
                    isNextBusy = false;

                    Log.d(TAG, "onResponse: JSON EXCEPTION " + e);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: "+error.getLocalizedMessage());
                if (error instanceof NoConnectionError)
                    Toast.makeText(getActivity(), "Please Check your internet connection",
                            Toast.LENGTH_LONG).show();
                isNextBusy = false;

            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("Authorization", "Token " + token);
                return map;
            }
        };

        requestQueue.add(jsonObjectRequest);

    }
}
