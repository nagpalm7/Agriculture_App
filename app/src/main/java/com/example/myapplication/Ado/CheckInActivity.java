package com.example.myapplication.Ado;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class CheckInActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private EditText khasraNo;
    private EditText remarkText;
    private EditText incidentText;
    private Button pickImageButton;
    private Button submitButton;
    private int IMAGE_CAPTURE_RC;
    private Bitmap imageBitmap = null;
    private ArrayList<Bitmap> imagesBitmap;
    private ReportImageRecyAdapter adapter;
    private String reportSubmitUrl = "http://13.235.100.235:8000/api/report-ado/add/";
    private String imageUploadUrl = "http://13.235.100.235:8000/api/upload/images/";
    private Intent intent;
    private int locationId;
    private String reportId;
    private String token;
    private AlertDialog reportSubmitLoading;
    private boolean isReportSubmitted = false;
    private String TAG = "CheckInActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkin_form);
        intent = getIntent();
        String id = intent.getStringExtra("id");
        locationId = Integer.parseInt(id);
        SharedPreferences prefs = getSharedPreferences("tokenFile", MODE_PRIVATE);
        token = prefs.getString("token", "");
        Log.d(TAG, "onCreate: TOKEN " + token);
        recyclerView = findViewById(R.id.pics_recyclerview);
        khasraNo = findViewById(R.id.khasra_no);
        remarkText = findViewById(R.id.ado_report_remarks);
        incidentText = findViewById(R.id.incident_reason);
        pickImageButton = findViewById(R.id.pick_photo);
        submitButton = findViewById(R.id.submit_report_ado);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        imagesBitmap = new ArrayList<>();
        adapter = new ReportImageRecyAdapter(this, imagesBitmap);
        recyclerView.setAdapter(adapter);
        pickImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, IMAGE_CAPTURE_RC);
            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageBitmap == null)
                    Toast.makeText(CheckInActivity.this, "Please add atleast one picture!", Toast.LENGTH_SHORT).show();
                else
                    submitReport();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == IMAGE_CAPTURE_RC) {
            if (resultCode == RESULT_OK) {
                imageBitmap = (Bitmap) data.getExtras().get("data");
                imagesBitmap.add(imageBitmap);
                adapter.notifyDataSetChanged();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void submitReport() {
        reportSubmitLoading = new SpotsDialog.Builder().setContext(this).setMessage("Submitting Report").setCancelable(false)
                .build();
        reportSubmitLoading.show();
        if (!isReportSubmitted) {
            RequestQueue requestQueue = Volley.newRequestQueue(this);

            JSONObject postParams = new JSONObject();
            try {
                String remarks = remarkText.getText().toString();
                String incidentReason = incidentText.getText().toString();
                postParams.put("location", locationId);
                postParams.put("remarks", remarks);
                postParams.put("incident_reason", incidentReason);
            } catch (JSONException e) {
                Toast.makeText(this, "Something went wrong, please try again!", Toast.LENGTH_SHORT).show();
                reportSubmitLoading.dismiss();
                Log.d(TAG, "submitReport: " + e);
            }

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, reportSubmitUrl, postParams,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONObject singleObject = new JSONObject(String.valueOf(response));
                                reportId = singleObject.getString("id");
                                Log.d(TAG, "onResponse: " + singleObject);
                                isReportSubmitted = true;
                                //uploadPhotos();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                reportSubmitLoading.dismiss();
                                Log.d(TAG, "jsonexception: reportSubmitRequest " + e);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error instanceof NoConnectionError)
                                Toast.makeText(CheckInActivity.this, "Check your internet connection!", Toast.LENGTH_LONG).show();
                            else {
                                Toast.makeText(CheckInActivity.this, "Something went wrong, Please try again!", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "onErrorResponse: reportSubmitRequest " + error);
                            }
                            reportSubmitLoading.dismiss();
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("Authorization", "Token " + token);
                    return map;
                }
            };
            requestQueue.add(jsonObjectRequest);
        } /*else
            uploadPhotos();*/
    }

    private void uploadPhotos() {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, imageUploadUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(CheckInActivity.this, "Image Successfully upload!", Toast.LENGTH_SHORT).show();
                        try {
                            JSONObject singleObejct = new JSONObject(String.valueOf(response));
                            Log.d(TAG, "onResponse: " + singleObejct);
                            reportSubmitLoading.dismiss();
                        } catch (JSONException e) {
                            Log.d(TAG, "jsonexception: uploadImage" + e);
                            reportSubmitLoading.dismiss();
                        }
                        reportSubmitLoading.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse: imageUpload" + error);
                        Toast.makeText(CheckInActivity.this, "onErrorResponse: imageUpload" + error, Toast.LENGTH_LONG).show();
                        Log.d(TAG, "networkResponse: imageUpload" + error.networkResponse);
                        reportSubmitLoading.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> imageMap = new HashMap<>();
                imageMap.put("report", reportId);
                String image = getStringImage(imageBitmap);
                imageMap.put("image", image);
                return imageMap;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> tokenMap = new HashMap<>();
                tokenMap.put("Authorization", "Token " + token);
                return tokenMap;
            }
        };
        queue.add(stringRequest);
    }

    private String getStringImage(Bitmap bitmap) {
        ByteArrayOutputStream ba = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, ba);
        byte[] imageByte = ba.toByteArray();
        String encode = Base64.encodeToString(imageByte, Base64.DEFAULT);
        return encode;
    }
}

