package com.example.myapplication.Dda;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
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


public class DdaselectAdo extends AppCompatActivity {
    private static final String TAG = "DdaselectAdo";
    private ArrayList<String> nameofado;
    private ArrayList<String> villagename;

    private String urlget = "http://13.235.100.235:8000/api/ado/";
    private String token;
    private DdaAdoListAdapter ddaAdoListAdapter;
    private String idtopass;
    private String adoid;
    public static boolean isAssigned = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ddaselect_ado);


        nameofado = new ArrayList<String>();
        villagename = new ArrayList<String>();

        ddaAdoListAdapter = new DdaAdoListAdapter(DdaselectAdo.this,nameofado,villagename);
        RecyclerView review = findViewById(R.id.RecyclerViewadolist);
        review.setAdapter(ddaAdoListAdapter);
        review.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        //getting location id coming from unassigned fragment to this activity
        Bundle extras = getIntent().getExtras();
        idtopass = extras.getString("Id_I_Need");
        Log.d(TAG, "onCreate: Id_I_Need="+idtopass);
        ddaAdoListAdapter.getlocationid(idtopass);


        Toast.makeText(this, "List of Ado's", Toast.LENGTH_SHORT).show();
        loadData(urlget);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("List of Ado's");        SharedPreferences preferences = getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
        token = preferences.getString("token", "");
        Log.d(TAG, "onCreateView: " + token);

    }

    private void loadData(String url){
        final RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject = new JSONObject(String.valueOf(response));
                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject c =jsonArray.getJSONObject(i);
                        adoid = c.getString("id");
                        ddaAdoListAdapter.getadoid(adoid);
                        nameofado.add(c.getString("name"));
                        villagename.add(c.getString("village_name"));
                    }
                    ddaAdoListAdapter.notifyDataSetChanged();
                }catch (JSONException e){
                    Log.d(TAG, "onResponse: "+e);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: "+error.getLocalizedMessage());
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

    //for back button on action bar
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.searchmenu,menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }


}
