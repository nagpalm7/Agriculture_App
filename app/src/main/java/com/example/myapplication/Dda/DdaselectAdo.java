package com.example.myapplication.Dda;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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
import java.util.HashMap;
import java.util.Map;

public class DdaselectAdo extends AppCompatActivity {
    private static final String TAG = "DdaselectAdo";
    private ArrayList<String> Name;
    private DdaAdolistAdapter DdaAdolistAdapter;
    private String urlget = "http://13.235.100.235:8000/api/ado/";
    private String token;

    public DdaselectAdo(){
        Name = new ArrayList<String>(3);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ddaselect_ado);
        Toast.makeText(this,"List of Ado's",Toast.LENGTH_LONG).show();

        RecyclerView review = (RecyclerView) findViewById(R.id.recyclerViewAdoList);
        DdaAdolistAdapter = new DdaAdolistAdapter(this,Name);
        review.setAdapter(DdaAdolistAdapter);
        //make your own adapter harsh in dda fragment

      /*  RecyclerView review = (RecyclerView) findViewById(R.id.recyclerViewAdoList);
        adoListAdapter = new AdoListAdapter(this,Name);
        review.setAdapter(adoListAdapter);
>>>>>>> 00346294bdf7c96a3f3a823fca3f9e00875e1134
        review.setLayoutManager(new LinearLayoutManager(this));

        SharedPreferences preferences = getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
        token = preferences.getString("token","");
        Log.d(TAG, "onCreateView: "+token);

        final RequestQueue adolistrequestqueue = Volley.newRequestQueue(this);
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlget, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject = new JSONObject(String.valueOf(response));
                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject c = jsonArray.getJSONObject(i);
                        Name.add(c.getString("name"));
                        DdaAdolistAdapter.notifyDataSetChanged();
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
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("Authorization", "Token " + token);
                return map;
            }

        };

        adolistrequestqueue.add(jsonObjectRequest);
*/
    }
}
