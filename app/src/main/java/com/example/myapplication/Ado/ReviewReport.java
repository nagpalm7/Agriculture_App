package com.example.myapplication.Ado;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

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
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReviewReport extends AppCompatActivity {
    private TextView fatherNameRight;
    private TextView fatherNameLeft;
    private TextView nameLeft;
    private TextView nameRight;
    private TextView villCodeRight;
    private TextView villCodeLeft;
    private TextView mobileLeft;
    private TextView mobileRight;
    private TextView khasraLeft;
    private TextView khasraRight;
    private TextView ownLeaseLeft;
    private TextView ownLeaseRight;
    private TextView remarksLeft;
    private TextView remarksRight;
    private TextView reasonLeft;
    private TextView reasonRight;
    private TextView actionLeft;
    private TextView actionRight;
    private static String TAG = "ReviewReport";
    private TextView chalaanLeft;
    private ProgressBar progressBar;
    private TableLayout tableLayout;
    private String mUrl;
    private boolean isComplete;
    private boolean isAdmin;
    private TextView chalaanRight;
    private RecyclerView recyclerView;
    private ReviewPicsRecyclerviewAdapter adapter;
    private ArrayList<String> mImagesUrl;
    private String farmerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_report);
        tableLayout = findViewById(R.id.table);
        getSupportActionBar().setTitle("Report Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fatherNameRight = findViewById(R.id.fatherNameRight);
        fatherNameLeft = findViewById(R.id.fatherNameLeft);
        nameLeft = findViewById(R.id.nameLeft);
        nameRight = findViewById(R.id.nameRight);
        villCodeRight = findViewById(R.id.villCodeRight);
        villCodeLeft = findViewById(R.id.villCodeLeft);
        mobileLeft = findViewById(R.id.mobileLeft);
        mobileRight = findViewById(R.id.mobileRight);
        khasraLeft = findViewById(R.id.khasraLeft);
        khasraRight = findViewById(R.id.khasraRight);
        ownLeaseLeft = findViewById(R.id.own_lease_Left);
        ownLeaseRight = findViewById(R.id.own_lease_Right);
        remarksLeft = findViewById(R.id.remarksLeft);
        remarksRight = findViewById(R.id.remarksRight);
        reasonLeft = findViewById(R.id.reasonLeft);
        reasonRight = findViewById(R.id.reasonRight);
        progressBar = findViewById(R.id.progressBar);
        actionLeft = findViewById(R.id.actionLeft);
        actionRight = findViewById(R.id.actionRight);
        chalaanLeft = findViewById(R.id.chalaan_amount_left);
        chalaanRight = findViewById(R.id.chalaan_amount_right);
        recyclerView = findViewById(R.id.review_pics_recyclerview);
        mImagesUrl = new ArrayList<>();
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new ReviewPicsRecyclerviewAdapter(this, mImagesUrl);
        recyclerView.setAdapter(adapter);
        Button forfeitButton = findViewById(R.id.forfeit);
        Button startButton = findViewById(R.id.start);
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        Log.d(TAG, "onCreate: ID " + id);
        isComplete = intent.getBooleanExtra("isComplete", false);
        isAdmin = intent.getBooleanExtra("isAdmin", false);
        boolean isDdo = intent.getBooleanExtra("isDdo", false);
        mUrl = "http://13.235.100.235:8000/api/report-ado/" + id + "/";
        getDetails();
        if (isDdo) {
            addRowtoTable("Column1", "Column2", false);
            addRowtoTable("Data1", "Data2", true);
            addRowtoTable("Data3", "Data4", true);
            addRowtoTable("Data5", "Data6", true);
            tableLayout.setVisibility(View.VISIBLE);
            if (isComplete)
                startButton.setVisibility(View.VISIBLE);
            else
                forfeitButton.setVisibility(View.VISIBLE);
            startButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            forfeitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void getDetails() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, mUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject rootObject = new JSONObject(String.valueOf(response));
                            String villCode = rootObject.getString("village_code");
                            farmerId = rootObject.getString("farmer_code");
                            String name = rootObject.getString("farmer_name");
                            String fatherName = rootObject.getString("father_name");
                            String ownership = rootObject.getString("ownership");
                            String action = rootObject.getString("action");
                            String remarks = rootObject.getString("remarks");
                            String reason = rootObject.getString("incident_reason");
                            //String number = rootObject.getString("number");
                            JSONArray imagesArray = rootObject.getJSONArray("images");
                            for (int i = 0; i < imagesArray.length(); i++) {
                                JSONObject imageObject = imagesArray.getJSONObject(i);
                                String imageUrl = imageObject.getString("image");
                                mImagesUrl.add(imageUrl);
                                adapter.notifyDataSetChanged();
                            }
                            Log.d(TAG, "onResponse: ACTION " + action);
                            villCodeLeft.setText("Village Code");
                            villCodeRight.setText(villCode);
                            nameLeft.setText("Farmer Name");
                            nameRight.setText(name);
                            //khasraLeft.setText("Khasra Number");
                            //khasraRight.setText(khasraNo);
                            khasraLeft.setVisibility(View.GONE);
                            khasraRight.setVisibility(View.GONE);
                            fatherNameLeft.setText("Father Name");
                            fatherNameRight.setText(fatherName);
                            ownLeaseLeft.setText("Ownership/Lease");
                            ownLeaseRight.setText(ownership);
                            actionLeft.setText("Action Taken");
                            actionRight.setText(action);
                            if (action.equals("chalaan")) {
                                chalaanLeft.setText("Chalaan Amount");
                                String chalaanAmount = rootObject.getString("amount");
                                chalaanRight.setText(chalaanAmount);
                            } else {
                                chalaanLeft.setVisibility(View.GONE);
                                chalaanRight.setVisibility(View.GONE);
                            }
                            remarksLeft.setText("Remarks");
                            remarksRight.setText(remarks);
                            //mobileLeft.setText("Mobile No");
                            //mobileRight.setText(number);
                            mobileLeft.setVisibility(View.GONE);
                            mobileRight.setVisibility(View.GONE);
                            reasonLeft.setText("Incident Reason");
                            reasonRight.setText(reason);
                            progressBar.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d(TAG, "onResponse: JSON EXCEPTION " + e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse: " + error);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                SharedPreferences prefs = getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
                String token = prefs.getString("token", "");
                map.put("Authorization", "Token " + token);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

    private void addRowtoTable(String col1, String col2, boolean isDataRow) {
        TableRow tableRow1 = new TableRow(this);
        TableRow.LayoutParams params = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        TableRow.LayoutParams params2 = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        params.setMargins(5, 0, 5, 5);
        params2.setMargins(0, 0, 5, 5);
        TextView colName1 = new TextView(this);
        colName1.setText(col1);
        colName1.setLayoutParams(params);
        colName1.setGravity(Gravity.CENTER);
        TextView colName2 = new TextView(this);
        colName2.setText(col2);
        colName2.setLayoutParams(params2);
        colName2.setGravity(Gravity.CENTER);
        tableRow1.setWeightSum(2f);
        if (isDataRow) {
            colName1.setBackgroundColor(Color.parseColor("#ffffff"));
            colName2.setBackgroundColor(Color.parseColor("#ffffff"));
            colName2.setTextSize(16f);
            colName1.setTextSize(16f);
        } else {
            colName2.setTextSize(18f);
            colName1.setTextSize(18f);
        }
        tableRow1.addView(colName1);
        tableRow1.addView(colName2);
        tableLayout.addView(tableRow1);
    }

    //flag = 1 for Add in BlockedList and 0 for remove from Blocked List
    private void actionOnFarmerSub(int flag) {
        String url = "http://117.240.196.238:8080/api/CRM/setFarmerID?key=agriHr@CRM&fID="
                + farmerId + "&flag=" + flag;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject singleObject = new JSONObject(String.valueOf(response));
                            String message = singleObject.getString("message");
                            Toast.makeText(ReviewReport.this, message, Toast.LENGTH_SHORT).show();
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d(TAG, "onResponse: JSON blockFarmerSub " + e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof NoConnectionError)
                            Toast.makeText(ReviewReport.this, "Check your internet connection",
                                    Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(ReviewReport.this, "Something went wrong, " +
                                    "please try again!", Toast.LENGTH_LONG).show();
                        Log.d(TAG, "onErrorResponse: blockFarmerSub " + error);
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

}
