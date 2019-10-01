package com.example.myapplication.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class detailsActivity extends AppCompatActivity {

    private String ado_name;
    private String dda_name;
    private RelativeLayout parent;
    private TextView noDetails_text;
    private String id;
    private String urlado;
    private String urldda;
    private String ado_id;
    private String dda_id;

    private String name;
    private String number;
    private String email;
    private String TAG="Detail class";
    private String token;

    private TextView aname;
    private TextView aemail;
    private TextView anumber;

    private TextView bname;
    private TextView bemail;
    private TextView bnumber;

    private boolean both = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Intent intent = getIntent();
        ado_name = intent.getStringExtra("ado_name");
        dda_name = intent.getStringExtra("dda_name");
        ado_id = intent.getStringExtra("ado_pk");
        dda_id = intent.getStringExtra("dda_pk");
        parent = findViewById(R.id.parent);
        noDetails_text = findViewById(R.id.nodetail_text);

        aname= findViewById(R.id.adodetail_name);
        aemail = findViewById(R.id.adodetail_email);
        anumber = findViewById(R.id.adodetail_phone);

        bname = findViewById(R.id.ddadetail_name);
        bemail = findViewById(R.id.ddadetail_email);
        bnumber = findViewById(R.id.ddadetail_phone);


        SharedPreferences prefs = getSharedPreferences("tokenFile", MODE_PRIVATE);
        token = prefs.getString("token", "");

        urlado = "http://13.235.100.235/api/user/" + ado_id + "/";
        urldda = "http://13.235.100.235/api/user/" + dda_id + "/";

        Log.d(TAG, "onCreate: "+ado_id);
        Log.d(TAG, "onCreate: "+dda_id);







        if(ado_name.equals("NOT ASSIGNED") && dda_name.equals("NOT ASSIGNED")){
            both = false;
            parent.setVisibility(View.GONE);
            noDetails_text.setVisibility(View.VISIBLE);
        }else if(ado_name.equals("NOT ASSIGNED") && !(dda_name.equals("NOT ASSIGNED"))){
            both = false;
            loadData(urldda,false);

        }else if(!(ado_name.equals("NOT ASSIGNED")) && dda_name.equals("NOT ASSIGNED")){
            both = false;
            loadData(urlado,true);
        }else if(!(ado_name.equals("NOT ASSIGNED")) && !(dda_name.equals("NOT ASSIGNED"))){
            both = true;
            loadData(urlado,true);
        }



    }

    private void loadData(String url , final boolean isado) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject rootObject = new JSONObject(String.valueOf(response));
                            name = rootObject.getString("name");
                            number = rootObject.getString("number");
                            email = rootObject.getString("email");

                            if(isado){

                                aname.setText(name);
                                anumber.setText(number);
                                aemail.setText(email);

                            }
                            else{
                                bname.setText(name);
                                bnumber.setText(number);
                                bemail.setText(email);


                            }


                            //fetchSpinnerData(spinnerUrl);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d(TAG, "onResponse: JSON EXCEPTION: getDetails " + e);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse: getDetails " + error);
                        Toast.makeText(detailsActivity.this, "Something went wrong, try again later!",
                                Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("Authorization", "Token " + token);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);

    }

    private void requestFinished(RequestQueue queue) {

        queue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {

            @Override
            public void onRequestFinished(Request<Object> request) {
                if(both){
                    loadData(urldda,false);
                }
            }
        });

    }


}
