package com.example.myapplication.Ado;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.ClientError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.example.myapplication.R;
import com.obsez.android.lib.filechooser.ChooserDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
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
    private EditText villageEditText;
    private EditText farmerEditText;
    private EditText mobileEditText;
    private static int IMAGE_CAPTURE_RC = 191;
    private Bitmap imageBitmap = null;
    private ArrayList<File> mImages;
    private ArrayList<String> mImagesPath;
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
    private String farmerDetailsUrl = "http://117.240.196.238:8080/api/CRM/getFarmerDetail";

    //arraylist
    private ArrayList<String> farmerNames;
    private ArrayList<String> farmerFatherNames;

    //spinner
    private Spinner name;
    private Spinner fname;
    private ToggleButton ownerlease;
    private ToggleButton action;
    private boolean isRequestFinished = false;
    private boolean isTextChanged = false;
    private boolean isBusy = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkin_form);
//        intent = getIntent();
//        String id = intent.getStringExtra("id");
//        locationId = Integer.parseInt(id);
        SharedPreferences prefs = getSharedPreferences("tokenFile", MODE_PRIVATE);
        token = prefs.getString("token", "");
        getSupportActionBar().setTitle("Report Filing");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Log.d(TAG, "onCreate: TOKEN " + token);
        recyclerView = findViewById(R.id.pics_recyclerview);
        khasraNo = findViewById(R.id.khasra_no);
        remarkText = findViewById(R.id.ado_report_remarks);
        incidentText = findViewById(R.id.incident_reason);
        pickImageButton = findViewById(R.id.pick_photo);
        submitButton = findViewById(R.id.submit_report_ado);
        villageEditText = findViewById(R.id.village_code);
        mobileEditText = findViewById(R.id.mobile_no);


        // reference to spinners
        name = findViewById(R.id.sname);
        fname = findViewById(R.id.sfname);
        //reference to toggle button
        ownerlease = findViewById(R.id.toggleol);
        action = findViewById(R.id.toggleaction);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mImages = new ArrayList<>();
        mImagesPath = new ArrayList<>();
        adapter = new ReportImageRecyAdapter(this, mImagesPath);
        recyclerView.setAdapter(adapter);
        pickImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImagePicker();
            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mImages.isEmpty())
                    Toast.makeText(CheckInActivity.this, "Please add atleast one picture!", Toast.LENGTH_SHORT).show();
                else
                    submitReport();
            }
        });

        villageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                isTextChanged = true;
                isRequestFinished = false;
            }
        });
        name.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.d(TAG, "onTouch: outside " + isRequestFinished + " " + isTextChanged);
                if (!isTextChanged)
                    Toast.makeText(CheckInActivity.this, "Please fill the Village Code first", Toast.LENGTH_SHORT).show();
                else if (!isRequestFinished && motionEvent.getAction() == MotionEvent.ACTION_DOWN && !isBusy) {
                    Log.d(TAG, "onTouch: ");
                    getFarmerDetails();

                    ArrayAdapter<String> adaptername = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, farmerNames);
                    adaptername.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    ArrayAdapter<String> adapterfname = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, farmerFatherNames);
                    adapterfname.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    name.setAdapter(adaptername);
                    fname.setAdapter(adapterfname);
                }
                return false;
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void openImagePicker() {
        File file = Environment.getExternalStorageDirectory();
        String start = file.getAbsolutePath();
        new ChooserDialog(this)
                .withStartFile(start)
                .withChosenListener(new ChooserDialog.Result() {
                    @Override
                    public void onChoosePath(String s, File file) {
                        mImages.add(file);
                        mImagesPath.add(s);
                        Log.d(TAG, "onChoosePath: rectest"+mImages+mImagesPath);
                        adapter.notifyDataSetChanged();
                    }
                })
                .withOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        dialogInterface.cancel();
                    }
                })
                .build()
                .show();
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
                String farmername = (String) name.getSelectedItem();
                String fathername = (String) name.getSelectedItem();
                String rtype = (String) ownerlease.getText();
                String actiontype = (String) action.getText();

                postParams.put("farmer_name", farmername);
                postParams.put("father_name", fathername);
                postParams.put("ownership", rtype);
                postParams.put("action", actiontype);
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
                                uploadPhotos();
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
        } else
            uploadPhotos();
    }

    private void uploadPhotos() {
        for (int pos = 0; pos < mImages.size(); pos++) {
            final int finalPos = pos;
            AndroidNetworking.upload(imageUploadUrl)
                    .addHeaders("Authorization", "Token " + token)
                    .addHeaders("report", reportId)
                    .addMultipartFile("", mImages.get(pos))
                    .setTag("Upload Images")
                    .setPriority(Priority.HIGH)
                    .build()
                    .setUploadProgressListener(new UploadProgressListener() {
                        @Override
                        public void onProgress(long bytesUploaded, long totalBytes) {
                            Log.d(TAG, "onProgress: " + bytesUploaded + "files uploaded: " + finalPos);
                        }
                    })
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, "onResponse: " + response);
                            Toast.makeText(CheckInActivity.this, "Photos Uploaded", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(ANError anError) {
                            Log.d(TAG, "onError: " + anError.getErrorBody());
                        }
                    });

        }
    }

    private void getFarmerDetails() {
        isBusy = true;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        farmerNames = new ArrayList<>();
        farmerFatherNames = new ArrayList<>();
        final String villageCode = villageEditText.getText().toString().trim();
        String finalUrl = farmerDetailsUrl + "?key=agriHr@CRM&vCode=" + villageCode;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, finalUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject rootObject = new JSONObject(String.valueOf(response));
                            JSONArray dataArray = rootObject.getJSONArray("data");
                            if (dataArray.length() == 0) {
                                Toast toast = Toast.makeText(CheckInActivity.this, "No Data Found for Village Code "
                                        + villageCode, Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                                isBusy = false;
                            }
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject singleObject = dataArray.getJSONObject(i);
                                String farmerName = singleObject.getString("FarmerName");
                                farmerNames.add(farmerName);
                                String farmerFatherName = singleObject.getString("father_name");
                                farmerFatherNames.add(farmerFatherName);
                                isRequestFinished = true;
                                isBusy = false;
                            }
                            Log.d(TAG, "onResponse: FARMER" + farmerNames.size());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse: FARMER " + error.networkResponse + "  " + error);
                        isBusy = false;
                        if (error instanceof ClientError) {
                            Toast toast = Toast.makeText(CheckInActivity.this, "Invalid Village Code", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                    }
                }) {
        };
        requestQueue.add(jsonObjectRequest);
    }

}

