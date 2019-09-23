package com.example.myapplication.Ado;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

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
    private ProgressBar progressBar;
    private TableLayout tableLayout;
    private String mUrl;
    private boolean isComplete;

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
        Button forfeitButton = findViewById(R.id.forfeit);
        Button startButton = findViewById(R.id.start);
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        isComplete = intent.getBooleanExtra("isComplete", false);
        boolean isDdo = intent.getBooleanExtra("isDdo", false);
        mUrl = "http://13.235.100.235:8000/api/report-ado/8/";
        getDetails();
        if (isDdo) {
            addRowtoTable("Column1", "Column2", false);
            addRowtoTable("Data1", "Data2", true);
            addRowtoTable("Data3", "Data4", true);
            addRowtoTable("Data5", "Data6", true);
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
                            String khasraNo = rootObject.getString("farmer_code");
                            String name = rootObject.getString("farmer_name");
                            String fatherName = rootObject.getString("father_name");
                            String ownership = rootObject.getString("ownership");
                            String action = rootObject.getString("action");
                            String remarks = rootObject.getString("remarks");
                            String reason = rootObject.getString("incident_reason");
                            villCodeLeft.setText("Village Code");
                            villCodeRight.setText(villCode);
                            nameLeft.setText("Farmer Name");
                            nameRight.setText(name);
                            khasraLeft.setText("Khasra Number");
                            khasraRight.setText(khasraNo);
                            fatherNameLeft.setText("Father Name");
                            fatherNameRight.setText(fatherName);
                            ownLeaseLeft.setText("Ownership/Lease");
                            ownLeaseRight.setText(ownership);
                            actionLeft.setText("Action Taken");
                            actionRight.setText(action);
                            remarksLeft.setText("Remarks");
                            remarksRight.setText(remarks);
                            mobileLeft.setText("Mobile No");
                            reasonLeft.setText("Incident Reason");
                            reasonRight.setText(reason);
                            progressBar.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

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
}
