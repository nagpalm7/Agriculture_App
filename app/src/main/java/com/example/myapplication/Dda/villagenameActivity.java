package com.example.myapplication.Dda;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.DatabaseHelper;
import com.example.myapplication.R;
import com.example.myapplication.login_activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class villagenameActivity extends AppCompatActivity {
    private static String TAG = "villagenameActivity";
    private String token;
    private ArrayList<String> villagesNames;
    private ArrayList<Integer> villageIds;
    private ArrayList<Integer> currentVillages;
    private String nextUrl;
    private VillagesUnderDistrictAdapter adapter;
    private LinearLayoutManager layoutManager;
    private ProgressBar progressBar;
    private String mAdoId;
    private boolean flag = true;
    private SearchView searchView;
    private SQLiteDatabase mDatabase;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_villagename);
        getSupportActionBar().setTitle("Villages List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        villagesNames = new ArrayList<>();
        villageIds = new ArrayList<>();
        RecyclerView recyclerView = findViewById(R.id.villages_list_recy);
        progressBar = findViewById(R.id.village_list_loading);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new VillagesUnderDistrictAdapter(this, villagesNames, villageIds);
        recyclerView.setAdapter(adapter);
        Intent intent = getIntent();
        String districtId = intent.getStringExtra("districtId");
        mAdoId = intent.getStringExtra("adoId");
        currentVillages = intent.getIntegerArrayListExtra("currentVillages");
        Log.d(TAG, "onCreate: " + currentVillages);
        String url = "http://18.224.202.135/api/villages-list/district/" + districtId + "/";
        Log.d(TAG, "onCreate: URL " + url);
        SharedPreferences preferences = getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
        token = preferences.getString("token", "");
        boolean isSqlDatabaseAvail = preferences.getBoolean("isSqlDatabaseAvail", false);
        databaseHelper = new DatabaseHelper(this);
        mDatabase = databaseHelper.getWritableDatabase();
//        if (!isSqlDatabaseAvail)
        loadData(url, 0);
        /*else
        {
            Cursor res = databaseHelper.getAllData();
            if (res.getCount() == 0)
            {
                Log.d(TAG, "onCreate: SQL NO DATA");
            }
            else
            {   int k = 0;
                while (res.moveToNext())
                {
                    int villageId = res.getInt(0);
                    villageIds.add(villageId);
                    villagesNames.add(res.getString(1));
                    if (currentVillages.contains(villageId)) {
                        Log.d(TAG, "onResponse: loaddata " + currentVillages + k);
                        adapter.addtoCurrentVillagesPos(k);
                    }
                    k++;
                }
                adapter.notifyDataSetChanged();
            }
        }*/
        /*recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int totalCount, pastCount, visibleCount;

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    totalCount = layoutManager.getItemCount();
                    pastCount = layoutManager.findFirstVisibleItemPosition();
                    visibleCount = layoutManager.getChildCount();
                    if ((pastCount + visibleCount) >= totalCount) {
                        if (!nextUrl.equals("null") && !isNextBusy)
                            loadData(nextUrl);
                        Log.d(TAG, "onScrolled:");
                    }
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_village_list, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        search(searchView);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.done) {
            saveChanges();
            return true;
        }

        return false;
    }

    private void search(SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                adapter.getFilter().filter(s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.isEmpty()) {
                    adapter.getFilter().filter(s);
                    return true;
                }
                return false;
            }
        });
    }

    private void loadData(String url, final int j) {
        if (!url.equals("null")) {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                int k = j;
                                JSONObject rootObject = new JSONObject(String.valueOf(response));
                                nextUrl = rootObject.getString("next");
                                JSONArray resultsArray = rootObject.getJSONArray("results");
                                for (int i = 0; i < resultsArray.length(); i++, k++) {
                                    JSONObject villageObject = resultsArray.getJSONObject(i);
                                    String villageName = villageObject.getString("village");
                                    villagesNames.add(villageName);
                                    int villageId = villageObject.getInt("id");
                                    villageIds.add(villageId);
                                    boolean result = databaseHelper.insertData(villageId, villageName);
                                    SharedPreferences.Editor editor = getSharedPreferences("tokenFile",
                                            MODE_PRIVATE).edit();
                                    if (result) {
                                        editor.putBoolean("isSqlDatabaseAvail", true);
                                        editor.apply();
                                    } else {
                                        editor.remove("isSqlDatabaseAvail");
                                        editor.apply();
                                    }
                                    if (currentVillages.contains(villageId)) {
                                        adapter.addtoCurrentVillagesPos(k);
                                        Log.d(TAG, "onResponse: loaddata " + currentVillages + k);
                                    }
                                }
//                                if (flag) {
//                                    flag = false;
//                                    loadData(nextUrl);
//                                }
                                loadData(nextUrl, k);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(villagenameActivity.this, "Please try again",
                                        Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "onResponse: JSON EXCEPTION " + e);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error instanceof NoConnectionError)
                                Toast.makeText(villagenameActivity.this, "Please check your Internet Connection!",
                                        Toast.LENGTH_LONG).show();
                            else
                                Toast.makeText(villagenameActivity.this, "Something went wrong, please try again!",
                                        Toast.LENGTH_LONG).show();
                            Log.d(TAG, "onErrorResponse: loadData " + error);
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
            jsonObjectRequest.setRetryPolicy(new RetryPolicy() {
                @Override
                public int getCurrentTimeout() {
                    return 50000;
                }

                @Override
                public int getCurrentRetryCount() {
                    return 50000;
                }

                @Override
                public void retry(VolleyError error) throws VolleyError {

                }
            });
            requestQueue.add(jsonObjectRequest);
        } else {
            adapter.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);
        }
    }

    private void saveChanges() {
        SparseArray villagesIdSparse = adapter.getSelectedVillageIds();
        Log.d(TAG, "saveChanges: " + villagesIdSparse);
        if (villagesIdSparse.size() != 0) {
            ArrayList<Integer> selectedIds = new ArrayList<>();
            for (int i = 0; i < villagesNames.size(); i++) {
                Object ob = villagesIdSparse.get(i);
                if (ob != null)
                    selectedIds.add(Integer.parseInt(villagesIdSparse.get(i).toString()));
            }
            JSONArray villagesIdArray = new JSONArray(selectedIds);
            JSONObject paramObject = new JSONObject();
            try {
                paramObject.put("village", villagesIdArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "saveChanges: " + paramObject);
            String url = "http://18.224.202.135/api/user/" + mAdoId + "/";
            JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.PUT, url, paramObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONObject jsonObject = new JSONObject(String.valueOf(response));
                                Log.d(TAG, "onResponse: " + jsonObject);
                                Toast.makeText(villagenameActivity.this, "Villages Successfully Assigned!",
                                        Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(villagenameActivity.this, login_activity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.d(TAG, "onResponse: JSON EXCEPTION " + e);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error instanceof NoConnectionError)
                                Toast.makeText(villagenameActivity.this, "Please check your Internet Connection!",
                                        Toast.LENGTH_LONG).show();
                            else
                                Toast.makeText(villagenameActivity.this, "Something went wrong, please try again!",
                                        Toast.LENGTH_LONG).show();
                            Log.d(TAG, "onErrorResponse: loadData " + error);
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("Authorization", "Token " + token);
                    return map;
                }

                @Override
                public String getBodyContentType() {
                    return "application/json";
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            jsonArrayRequest.setRetryPolicy(new RetryPolicy() {
                @Override
                public int getCurrentTimeout() {
                    return 50000;
                }

                @Override
                public int getCurrentRetryCount() {
                    return 50000;
                }

                @Override
                public void retry(VolleyError error) throws VolleyError {

                }
            });
            requestQueue.add(jsonArrayRequest);
        } else
            Toast.makeText(this, "No village Selected",
                    Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified())
            searchView.setIconified(true);
        else
            super.onBackPressed();
    }
}
