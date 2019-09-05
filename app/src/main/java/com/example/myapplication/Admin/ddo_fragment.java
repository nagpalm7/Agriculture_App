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
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ddo_fragment extends Fragment {

    private ArrayList<String> username;
    private ArrayList<String> userinfo;
    private String urlpost = "http://13.235.100.235:8000/api/user/";
    private final String TAG = "ddo_fragment";
    private RecyclerViewAdater recyclerViewAdater;
    private String token;

    public ddo_fragment(){
        username = new ArrayList<String>();
        userinfo = new ArrayList<String>();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: check1check");
        View view = inflater.inflate(R.layout.ddo_fragment, container, false);
        recyclerViewAdater = new RecyclerViewAdater(getActivity(),username,userinfo);
        RecyclerView Rview = view.findViewById(R.id.recyclerViewddo);
        Rview.setAdapter(recyclerViewAdater);
        Rview.setLayoutManager( new LinearLayoutManager(getActivity()));
        SharedPreferences preferences = getActivity().getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
        token = preferences.getString("token","");
        Log.d(TAG, "onCreateView: "+token);

        final RequestQueue requestQueue = Volley.newRequestQueue(getActivity());


        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlpost, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {
                    JSONArray jsonArray = new JSONArray(String.valueOf(response));
                    for(int i=0; i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String type_of_user= jsonObject.getString("typeOfUser");
                        if(type_of_user.equals("dda") ){
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
                Log.e(TAG, "onErrorResponse: " + error );
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



        return view;
    }


}
