package com.example.myapplication.Admin;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ado_fragment extends Fragment {
    private ArrayList<String> username;
    private ArrayList<String> userinfo;
    private String urlpost = "http://13.235.100.235:8000/api/user/";
    private String tokenurl = "http://13.235.100.235:8000/api-token-auth/";
    private RecyclerViewAdater recyclerViewAdater;
    private String token;

    private final String TAG = "ado_fragment";

    public ado_fragment(){
        username = new ArrayList<String>();
        userinfo = new ArrayList<String>();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ado_fragment,container,false);
        recyclerViewAdater = new RecyclerViewAdater(getActivity(), username, userinfo);
        RecyclerView Rview = view.findViewById(R.id.recyclerViewado);
        Rview.setAdapter(recyclerViewAdater);
        Rview.setLayoutManager( new LinearLayoutManager(getActivity()));

        final RequestQueue requestQueue = Volley.newRequestQueue(getActivity());


        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlpost, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {
                    JSONArray jsonArray = new JSONArray(String.valueOf(response));
                    for(int i=0; i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String type_of_user= jsonObject.getString("typeOfUser");
                        if(type_of_user.equals("ado") ){
                            username.add(jsonObject.getString("name"));
                            userinfo.add(String.valueOf(jsonObject.getInt("id")));
                        }
                    }
                    Log.d(TAG, "onResponse: "+username);
                    recyclerViewAdater.notifyDataSetChanged();


                } catch (JSONException e) {
                    Log.e(TAG, "onResponse: " + e.getLocalizedMessage());
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("Authorization", "Token " + token);
                return map;
            }
        };

        requestQueue.add(jsonArrayRequest);

        JSONObject postparams = new JSONObject();
        try {
            postparams.put("username", "admin");
            postparams.put("password", "root");
            Log.d(TAG, "params: ");

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, tokenurl, postparams,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            //retrieve the token from server
                            try {
                                JSONObject jsonObject = new JSONObject(String.valueOf(response));
                                token = jsonObject.getString("token");
                                requestQueue.add(jsonArrayRequest);
                                Log.d(TAG, "onResponse: Token:" + token);
                                SharedPreferences.Editor editor = getActivity().getSharedPreferences("tokenFile", Context.MODE_PRIVATE).edit();
                                editor.putString("token",token);
                                editor.apply();
                            } catch (JSONException e) {
                                Log.d(TAG, "onResponse: error in post catch block: " + e);
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(TAG, "onErrorResponse: some error in post: " + error);
//                                error.printStackTrace();
                        }
                    });

            requestQueue.add(jsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }
}
