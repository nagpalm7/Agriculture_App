package com.example.myapplication.Admin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

public class EditActivity extends AppCompatActivity {
    private static String TAG = "EditActivity";
    private EditText nameEditText;
    private EditText mobileEditText;
    private EditText emailEditText;
    private Button saveButton;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        getSupportActionBar().setTitle("Editing");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        nameEditText = findViewById(R.id.edit_name);
        mobileEditText = findViewById(R.id.edit_number);
        emailEditText = findViewById(R.id.edit_email);
        saveButton = findViewById(R.id.save_changes_button);
        SharedPreferences prefs = getSharedPreferences("tokenFile", MODE_PRIVATE);
        token = prefs.getString("token", "");
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        final String url = "http://13.235.100.235:8000/api/user/" + id + "/";
        getDetails(url);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveChanges(url);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void getDetails(String url) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject rootObject = new JSONObject(String.valueOf(response));
                            String name = rootObject.getString("name");
                            String number = rootObject.getString("number");
                            String email = rootObject.getString("email");
                            nameEditText.setText(name);
                            mobileEditText.setText(number);
                            emailEditText.setText(email);
                            JSONObject districtObject = rootObject.getJSONObject("district");
                            String district = districtObject.getString("district");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d(TAG, "onResponse: JSON EXCEPTION: " + e);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse: " + error);
                        Toast.makeText(EditActivity.this, "Something went wrong, try again later!",
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

    private void saveChanges(String url) {
        JSONObject params = new JSONObject();
        String name = nameEditText.getText().toString();
        String number = mobileEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        try {
            params.put("name", name);
            params.put("number", number);
            params.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "saveChanges: PARAMS EXCEPTION " + e);
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse: " + response);
                        Toast.makeText(EditActivity.this, "Changes Saved Successfully!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse: " + error);
                        Toast.makeText(EditActivity.this, "Something went wrong, try again later!",
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
}
